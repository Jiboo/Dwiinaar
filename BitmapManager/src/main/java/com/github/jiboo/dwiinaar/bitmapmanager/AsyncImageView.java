/**
 * @see https://github.com/Jiboo/Dwiinaar for updates
 */

package com.github.jiboo.dwiinaar.bitmapmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Asynchronous version of ImageView.
 * Care:
 * If you don't specify (hardcode) any width/height in LayoutParams, your view will probably return a 0/0 size after the first measure pass
 * as no drawable may be attached at that time. In a ListView, for example, this might be a problem as the ListView will try to create
 * as many 0/0 items.
 * <p/>
 * Usage:
 * Use it as a normal ImageView, setImageURL/File/Resource/Uri will load your image asynchronously.
 * You can override transformDrawable to wrap the BitmapDrawable that will be set when it's loaded.
 * In XML use the attribute loadingDrawable to set a default drawable will you are loading.
 */
public class AsyncImageView extends ImageView implements BitmapCache.Listener {
    protected Drawable dDefault;
    protected BitmapCache.Key dKey;
    protected final BitmapCache.Options dOptions = new BitmapCache.Options();

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AsyncImageView, defStyle, 0);
        dDefault = a.getDrawable(R.styleable.AsyncImageView_loadingDrawable);
        a.recycle();
    }

    public AsyncImageView(Context ctx, int defaultResID) {
        super(ctx);
        setImageDrawable(dDefault = ctx.getResources().getDrawable(defaultResID));
    }

    /////////////////////////////////////
    // Cache events
    /////////////////////////////////////

    @Override
    public void onBitmapLoaded(@NonNull BitmapCache.Key key, @NonNull Bitmap value) {
        if (key.equals(dKey)) {
            safeSetDrawable(transformDrawable(value));
        }
    }

    @Override
    public void onBitmapEvicted(@NonNull BitmapCache.Key key, boolean evicted, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        if (key.equals(dKey)) {
            if (evicted) {
                // Bitmap evicted but we still need it
                safeSetDrawable(dDefault);
            } else {
                safeSetDrawable(transformDrawable(newValue));
            }
        }
    }

    @Override
    public void onBitmapDecodingError(@NonNull BitmapCache.Key key, @NonNull Throwable error) {
        if (key.equals(dKey)) {
            safeSetDrawable(dDefault);
            if (error.getMessage() != null)
                Log.e("AsyncImageView", error.getMessage());
        }
    }

    /////////////////////////////////////
    // View lifecycle
    /////////////////////////////////////

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (dKey != null) {
            BitmapCache.subscribe(dKey, this);
            BitmapCache.asyncDecode(dKey, dOptions);
        }
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        pause();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pause();
        if (dKey != null) {
            BitmapCache.unsubscribe(dKey, this);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() == dDefault && dKey != null) {
            BitmapCache.asyncDecode(dKey, dOptions); // Trick used to issue a get on LruCache and increase priority for drawn entries
        }
    }

    /////////////////////////////////////
    // Utils
    /////////////////////////////////////

    protected void safeSetDrawable(@Nullable final Drawable drawable) {
        // The callbacks from the cache are called from an other thread, in this case we'll need to post the setImageDrawable
        // This method is just for convenience
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setImageDrawable(drawable);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    setImageDrawable(drawable);
                }
            });
        }
    }

    /////////////////////////////////////
    // Public API
    /////////////////////////////////////

    /**
     * Override this to intercept when the cache loading event and wrap the bitmap returned by the cache.
     *
     * @param value the loaded bitmap
     * @return a drawable that will be set to this ImageView
     */
    protected
    @NonNull
    Drawable transformDrawable(@NonNull Bitmap value) {
        return new BitmapDrawable(getResources(), value);
    }

    /**
     * Load a new aribtrary key, prefer setImageURL/File/Resource/Uri if you don't know what you're doing.
     */
    public void load(@NonNull BitmapCache.Key key) {
        if (!key.equals(dKey)) {
            safeSetDrawable(dDefault);
            if (dKey != null) {
                BitmapCache.unsubscribe(dKey, this);
            }
            BitmapCache.subscribe(dKey = key, this);
            BitmapCache.asyncDecode(key, dOptions);
        }
    }

    /**
     * Force the reload of the associated cache key (if set).
     */
    public void load() {
        if (dKey != null) {
            load(dKey);
        }
    }

    /**
     * Stop using the loaded bitmap.
     */
    public void pause() {
        dOptions.requestCancelDecode();
        BitmapCache.cancelPending(dKey);
        safeSetDrawable(dDefault);
    }

    /**
     * @param file to load asynchronously
     */
    public void setImageFile(@NonNull File file) {
        load(BitmapCache.getKey(file));
    }

    /**
     * @param url to load asynchronously (if the user has a SD card, the file will be cached on disk)
     *            TODO Be able to disable disk cache
     */
    public void setImageURL(@NonNull URL url) {
        load(BitmapCache.getKey(url));
    }

    /**
     * @param resId to load asynchronously (will be loaded from getContext().getResources())
     */
    @Override
    public void setImageResource(@DrawableRes int resId) {
        load(BitmapCache.getKey(getContext().getResources(), resId));
    }

    /**
     * @param uri to load (can be a local file, a url, or an android resource uri)
     */
    @Override
    public void setImageURI(@NonNull Uri uri) {
        try {
            BitmapCache.Key key = BitmapCache.getKey(getContext(), uri);
            if (key != null)
                load(key);
        } catch (FileNotFoundException e) {
            Log.w("AsyncImageView", "Unable to open content: " + uri, e);
        }
    }
}
