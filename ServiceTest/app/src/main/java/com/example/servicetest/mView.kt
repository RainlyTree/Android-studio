package com.example.servicetest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 *  Created by chenlin on 2021/11/1.
 */
class mView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    var radiusWidth = 3f

    var rect: Rect? = null
    var rectS: Rect? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE);
        val paint = Paint(Paint.ANTI_ALIAS_FLAG);
        paint.isAntiAlias = true;   // 去锯齿
        paint.color = Color.BLUE;
        paint.style = Paint.Style.STROKE;   //描边
        paint.strokeWidth = 3F;
        paint.strokeCap = Paint.Cap.ROUND

        canvas.drawCircle(width.toFloat() / 2, width.toFloat() / 2, (width.toFloat() - 3) / 2, paint)


        // 绘制圆形
        canvas.drawCircle(40F, 40f, 30f, paint);
        // 绘制正方形
        canvas.drawRect(10f, 80f, 70f, 140f, paint);
        // 绘制矩形
        canvas.drawRect(10f, 150f, 70f, 190f, paint);
        // 绘制圆角矩形
        val rel = RectF(10f, 200f, 70f, 230f);
        canvas.drawRoundRect(rel, 15f, 15f, paint);
        // 绘制椭圆
        val rell = RectF(10f, 240f, 70f, 270f);
        canvas.drawOval(rell, paint);
        // 定义一个Path对象，封闭成一个三角形
        val path1 = Path();
        path1.moveTo(10f, 340f);
        path1.lineTo(70f, 340f);
        path1.lineTo(40f, 290f);
        path1.close();
        // 根据Path进行绘制，绘制三角形
        canvas.drawPath(path1, paint);
        // 定义一个Path对象，封闭成一个五角形
        val path2 = Path();
        path2.moveTo(26f, 360f);
        path2.lineTo(54f, 360f);
        path2.lineTo(70f, 392f);
        path2.lineTo(40f, 420f);
        path2.lineTo(10f, 392f);
        path2.close();
        // 根据Path进行绘制，绘制五角形
        canvas.drawPath(path2, paint);

        // -----设置填充风格后绘制------
        paint.style = Paint.Style.FILL;   //全填充
        paint.color = Color.RED;

        canvas.drawCircle(width.toFloat() / 2, width.toFloat() / 2, (width.toFloat() - 3) / 2 , paint)

        // 绘制圆形
        canvas.drawCircle(120f, 40f, 30f, paint);
        // 绘制正方形
        canvas.drawRect(90f, 80f, 150f, 140f, paint);
        // 绘制矩形
        canvas.drawRect(90f, 150f, 150f, 190f, paint);
        // 绘制圆角矩形
        val re2 = RectF(90f, 200f, 150f, 230f);
        canvas.drawRoundRect(re2, 15f, 15f, paint);
        // 绘制椭圆
        val re21 = RectF(90f, 240f, 150f, 270f);
        canvas.drawOval(re21, paint);
        // 定义一个Path对象，封闭成一个三角形
        val path3 = Path();
        path3.moveTo(90f, 340f);
        path3.lineTo(150f, 340f);
        path3.lineTo(120f, 290f);
        path3.close();
        // 绘制三角形
        canvas.drawPath(path3, paint);
        // 定义一个Path对象，封闭成一个五角形
        val path4 = Path();
        path4.moveTo(106f, 360f);
        path4.lineTo(134f, 360f);
        path4.lineTo(150f, 392f);
        path4.lineTo(120f, 420f);
        path4.lineTo(90f, 392f);
        path4.close();
        // 绘制五角形
        canvas.drawPath(path4, paint);

        // -----设置渐变器后绘制--------
        // 为Paint设置渐变器
        val mShader = LinearGradient(0f, 0f, 40f, 60f, IntArray(4) {
            Color.RED; Color.GREEN; Color.BLUE; Color.YELLOW }, null,
            Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        // 设置阴影
        paint.setShadowLayer(45f, 10f, 10f, Color.GRAY);
        // 绘制圆形
        canvas.drawCircle(200f, 40f, 30f, paint);
        // 绘制正方形
        canvas.drawRect(170f, 80f, 230f, 140f, paint);
        // 绘制矩形
        canvas.drawRect(170f, 150f, 230f, 190f, paint);
        // 绘制圆角矩形
        val re3 = RectF(170f, 200f, 230f, 230f);
        canvas.drawRoundRect(re3, 15f, 15f, paint);
        // 绘制椭圆
        val re31 = RectF(170f, 240f, 230f, 270f);
        canvas.drawOval(re31, paint);
        // 定义一个Path对象，封闭成一个三角形
        val path5 = Path();
        path5.moveTo(170f, 340f);
        path5.lineTo(230f, 340f);
        path5.lineTo(200f, 290f);
        path5.close();
        // 绘制三角形
        canvas.drawPath(path5, paint);

        // 定义一个Path对象，封闭成一个五角形
        val path6 = Path();
        path6.moveTo(186f, 360f);
        path6.lineTo(214f, 360f);
        path6.lineTo(230f, 392f);
        path6.lineTo(200f, 420f);
        path6.lineTo(170f, 392f);
        path6.close();
        // 绘制五角形
        canvas.drawPath(path6, paint);


        // ------设置字符大小后绘制-------
        paint.textSize = 22F;
        paint.shader = null
        paint.setShadowLayer(10f, -20f, 30f, Color.GRAY)
        // 绘制7个字符串
        canvas.drawText("圆形", 240f, 50f, paint);
        canvas.drawText("正方形", 240f, 120f, paint);
        canvas.drawText("矩形", 240f, 175f, paint);
        canvas.drawText("圆角矩形", 230f, 220f, paint);
        canvas.drawText("椭圆", 240f, 260f, paint);
        canvas.drawText("三角形", 240f, 325f, paint);
        canvas.drawText("五角形", 240f, 390f, paint)

        canvas.drawText("好好学习天天向上", 300f, 100f, paint)
        val path = Path()
        path.addArc(RectF(100f, 100f, 600f, 600f), 0f, 260f)    //画弧形
        canvas.drawTextOnPath("geigei 姐姐不会生气吗 生气了也没事 谁叫geigei心里只有我呢  嘻嘻嘻嘻嘻嘻嘻嘻嘻", path, 10f, 10f, paint)
    }
}