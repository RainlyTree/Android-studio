package com.example.servicetest.customLayoutManager

import android.graphics.PointF
import androidx.annotation.FloatRange

/**
 * @param fraction 在路径上的位置 百分比
 * @param index Item所对应的索引
 * @param angle Item的旋转角度
 */
class PosTan(): PointF() {

    var fraction: Float = 0f
    var index: Int = 0
    var angle: Float = 0f
    fun set(x: Float, y: Float, angle: Float) {
        set(x, y)
        this.angle = angle
    }

    constructor(posTan: PosTan, ind: Int, fra: Float) : this() {
        set(posTan.x, posTan.y, posTan.angle)
        fraction = fra
        index = ind
    }
}