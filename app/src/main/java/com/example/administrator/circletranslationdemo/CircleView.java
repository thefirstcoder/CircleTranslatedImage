package com.example.administrator.circletranslationdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleView extends ImageView {
    private Paint mPaint = new Paint();

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable mDrawable = getDrawable();//获取xml文件设置的图片
        if (null == mDrawable) super.onDraw(canvas);//如果为空，交给父类处理
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();//将图片转化成bitmap

        int len = getWidth() < getHeight() ? getWidth() : getHeight();//获取xml的长宽属性值，选择较小的

        Bitmap tempBitmap = Bitmap.createBitmap(len, len, Bitmap.Config.ARGB_8888);//新建一个bitmap对象，作为缓存
        Canvas mCanvas = new Canvas(tempBitmap);//新建画布缓存bitmap对象

        mPaint.setAntiAlias(true);//抗锯齿
        mCanvas.drawCircle(len / 2, len / 2, len / 2, mPaint);//画一个圆

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置画笔的覆盖类型

        Matrix matrix = new Matrix(); // 初始化Matrix对象
        matrix.setScale((float) len / mBitmap.getWidth(), (float) len / mBitmap.getHeight()); //设置缩放比例
        mCanvas.drawBitmap(mBitmap, matrix, mPaint);//画出缩放后的图片

        mPaint.reset();//重置画笔
        canvas.drawBitmap(tempBitmap, 0, 0, new Paint());//绘制缓存图片
    }

}