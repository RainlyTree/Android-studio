package com.example.servicetest.toolClass

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.shapes.Shape
import android.view.View


fun solid(color: Any): ShapeDrawable.Solid {
    return ShapeDrawable.Solid(color)
}


sealed class ShapeDrawable {

    var next: ShapeDrawable? = null

    //背景填充色
    class Solid(val color: Any) : ShapeDrawable() {
        override fun box(drawable: GradientDrawable?): GradientDrawable {
            drawable!!.setColor(color as Int)
            return drawable
        }
    }

    //圆角
    class Corner : ShapeDrawable {
        var radiusArray: FloatArray? = null
        var radius: Float = 0f

        constructor(radius: Int) : super() {
            this.radius = radius.toFloat()
        }

        constructor(
            topLeftRadius: Int,
            topRightRadius: Int,
            bottomRightRadius: Int,
            bottomLeftRadius: Int
        )

        override fun box(drawable: GradientDrawable?): GradientDrawable {
            if (radiusArray == null) {
                drawable!!.cornerRadius = radius
            } else {
                drawable!!.cornerRadii = radiusArray
            }
            return drawable
        }
    }

    //背景边框 描边
    data class Stroke(
        val strokeWidth: Int,
        val dashColor: Any,
        val dashWidth: Int = 0,
        val dashGap: Int = 0,
        val shapeType: Int = GradientDrawable.RECTANGLE
    ) : ShapeDrawable() {
        override fun box(drawable: GradientDrawable?): GradientDrawable {
            drawable!!.apply {
                setStroke(
                    strokeWidth,
                    dashColor as Int,
                    dashWidth.toFloat(),
                    dashGap.toFloat()
                )
                shape = shapeType
            }
            return drawable
        }
    }

    //背景渐变填充
    data class GradientState(
        val orientation: Orientation,
        val startColor: Any,
        val endColor: Any
    ) : ShapeDrawable() {
        override fun box(drawable: GradientDrawable?): GradientDrawable {
            return GradientDrawable(
                orientation,
                intArrayOf(startColor as Int, endColor as Int)
            )
        }
    }

    operator fun plus(shape: ShapeDrawable): ShapeDrawable {
        shape.next = this
        return shape
    }

    abstract fun box(drawable: GradientDrawable?): GradientDrawable
}

var View.shape: ShapeDrawable?
    get() = null
    set(value) {
        var s: ShapeDrawable? = value
        val list = mutableListOf<ShapeDrawable>()
        var drawable: GradientDrawable? = null
        while (s != null) {
            if (s is ShapeDrawable.GradientState) {
                drawable = s.box(null)
            } else {
                list.add(s)
            }
            s = s.next
        }

        if (drawable == null) {
            drawable = GradientDrawable()
        }

        list.forEach {
            it.box(drawable)
        }

        background = drawable
    }