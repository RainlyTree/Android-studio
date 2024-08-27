package com.example.servicetest

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.servicetest.toolUtils.px

/**
 *  Created by chenlin on 2021/11/1.
 */
class DashView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    val DASH_WIDTH = 2f
    val DASH_LENGTH = 10f
    val OPEN_ANGLE = 120f

    val OFFSET = 150f.px

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val path = Path()
    val dash = Path()
    lateinit var pathEffect: PathEffect

    init {
        paint.strokeWidth = 3f.px
        paint.style = Paint.Style.STROKE
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addArc(
            width / 2 - OFFSET,
            height / 2 - OFFSET,
            width / 2 + OFFSET,
            height / 2 + OFFSET,
            90 + OPEN_ANGLE / 2,
            360 - OPEN_ANGLE
        )
        val pathMeasure = PathMeasure(path, false)
        pathEffect = PathDashPathEffect(dash, (pathMeasure.length - DASH_WIDTH) / 20f, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        //花弧形
        canvas.drawPath(path, paint)

        //刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
    }
}