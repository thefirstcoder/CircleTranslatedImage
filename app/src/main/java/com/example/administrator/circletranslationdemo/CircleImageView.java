package com.example.administrator.circletranslationdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/5.
 */

public class CircleImageView extends ImageView {
    //缩放类型
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    // 默认边界宽度
    private static final int DEFAULT_BORDER_WIDTH = 0;
    // 默认边界颜色

    private final RectF mBorderRect = new RectF();

    private final Paint mBitmapPaint = new Paint();
    //这个描边，则与本身的原图bitmap没有任何关联，
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader; // 位图渲染
    private float mBorderRadius;// 带边框的的图片半径

    private ColorFilter mColorFilter;
    //初始false
    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    private int mStartX;
    private int mStartY;

    //构造函数
    public CircleImageView(Context context) {
        super(context);
        init();
    }

    //构造函数
    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     */
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //通过obtainStyledAttributes 获得一组值赋给 TypedArray（数组） , 这一组值来自于res/values/attrs.xml中的name="CircleImageView"的declare-styleable中。
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        a.recycle();
        init();
    }

    /**
     * 作用就是保证第一次执行setup函数里下面代码要在构造函数执行完毕时调用
     */
    private void init() {
        //在这里ScaleType被强制设定为CENTER_CROP，就是将图片水平垂直居中，进行缩放。
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        //绘制内圆形 图片 画笔为mBitmapPaint
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBitmapPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            //通常来说 我们的代码就是执行到这里就返回了。返回的就是我们最原始的bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }
        if (mBitmap == null) {
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
        invalidate();
    }
}