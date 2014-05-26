package com.github.jiboo.dwiinaar.bitmapmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.net.URL;

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

    @Override
    public void onBitmapLoaded(@NonNull BitmapCache.Key key, @NonNull Bitmap value) {
        safeSetDrawable(new BitmapDrawable(getResources(), value));
    }

    @Override
    public void onBitmapEvicted(@NonNull BitmapCache.Key key, boolean evicted, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        if(evicted)
            safeSetDrawable(dDefault);
        else
            safeSetDrawable(new BitmapDrawable(getResources(), newValue));
    }

    @Override
    public void onBitmapDecodingError(@NonNull BitmapCache.Key key, @NonNull Throwable error) {
        safeSetDrawable(dDefault);
        Log.e("AsyncImageView", error.getMessage());
    }

    protected void pause() {
        dOptions.requestCancelDecode();
        BitmapCache.cancelPending(dKey);
        safeSetDrawable(dDefault);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(dKey != null)
            BitmapCache.asyncDecode(dKey, dOptions);
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
        if(dKey != null)
            BitmapCache.unsubscribe(dKey, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getDrawable() == dDefault && dKey != null) {
            BitmapCache.asyncDecode(dKey, dOptions);
        }
    }

    protected void safeSetDrawable(@Nullable final Drawable drawable) {
        if(Looper.myLooper() == Looper.getMainLooper())
            setImageDrawable(drawable);
        else
            post(new Runnable() {
                @Override
                public void run() {
                    setImageDrawable(drawable);
                }
            });
    }

    public void load(@NonNull BitmapCache.Key key) {
        if(!key.equals(dKey)) {
            safeSetDrawable(dDefault);
            if (dKey != null)
                BitmapCache.unsubscribe(dKey, this);
            BitmapCache.subscribe(dKey = key, this);
            BitmapCache.asyncDecode(key, dOptions);
        }
    }

    public void setImageFile(@NonNull File file) {
        load(BitmapCache.getKey(getContext(), file));
    }

    public void setImageURL(@NonNull URL url) {
        load(BitmapCache.getKey(getContext(), url));
    }

    @Override
    public void setImageResource(int resId) {
        load(BitmapCache.getKey(getContext(), resId));
    }

    /**
     * @deprecated use setImageFile/URL/Resource
     */
    @Override
    @Deprecated
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }
}
