package com.github.jiboo.dwiinaar.bitmapmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.util.AttributeSet;
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
        //dDefault = a.getDrawable(R.styleable.AsyncImageView_loadingDrawable);
        a.recycle();
    }

    public AsyncImageView(Context ctx, int defaultResID) {
        super(ctx);
        setImageDrawable(dDefault = ctx.getResources().getDrawable(defaultResID));
    }

    @Override
    public void onBitmapLoaded(BitmapCache.Key key, Bitmap value) {
        safeSetDrawable(new BitmapDrawable(getResources(), value));
    }

    @Override
    public void onBitmapEvicted(BitmapCache.Key key, boolean evicted, Bitmap oldValue, Bitmap newValue) {
        if(evicted)
            safeSetDrawable(dDefault);
        else
            safeSetDrawable(new BitmapDrawable(getResources(), newValue));
    }

    @Override
    public void onBitmapDecodingError(BitmapCache.Key key, Throwable error) {
        safeSetDrawable(dDefault);
    }

    protected void pause() {
        dOptions.requestCancelDecode();
        safeSetDrawable(dDefault);
    }

    protected void resume() {
        if(dKey != null)
            BitmapCache.asyncDecode(dKey, dOptions);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resume();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        pause();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        resume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pause();
    }

    protected void safeSetDrawable(final Drawable drawable) {
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

    public void load(BitmapCache.Key key) {
        if(!key.equals(dKey)) {
            safeSetDrawable(dDefault);
            if (dKey != null)
                BitmapCache.unsubscribe(dKey, this);
            BitmapCache.subscribe(dKey = key, this);
            BitmapCache.asyncDecode(key, dOptions);
        }
    }

    public void setImageFile(File file) {
        load(BitmapCache.getKey(getContext(), file));
    }

    public void setImageURL(URL url) {
        load(BitmapCache.getKey(getContext(), url));
    }

    @Override
    public void setImageResource(int resId) {
        load(BitmapCache.getKey(getContext(), resId));
    }

    @Override
    @Deprecated
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }
}
