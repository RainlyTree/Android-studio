package com.example.servicetest.customLayoutManager

import android.graphics.Path
import android.graphics.PathMeasure
import androidx.annotation.FloatRange
import kotlin.math.atan2

class Keyframes(path: Path) {
    private var mX: FloatArray = floatArrayOf()  //路径上的所有x轴坐标
    private var mY: FloatArray = floatArrayOf()  //路径上的所有y轴坐标
    private var mAngle: FloatArray = floatArrayOf()  //路径上每一个坐标所对应的角度

    var mTemp = PosTan()
    val PRECISON = .5f

    var mNumPoints = 0

    var pathLength = 0f

    init {
        initPath(path)
    }

    private fun initPath(path: Path) {
        val pathMeasure = PathMeasure(path, false)
        pathLength = pathMeasure.length
        val numPoints = ((pathLength / PRECISON) + 1).toInt()
        mNumPoints = numPoints
        //临时存放坐标点
        val position = floatArrayOf(0f, 0f)
        //临时存放正切值
        val tangent = floatArrayOf(0f, 0f)
        //当前距离
        var distance = 0f
        mX = FloatArray(numPoints)
        mY = FloatArray(numPoints)
        mAngle = FloatArray(numPoints)
        for (i in 0 until numPoints) {
            //更新当前距离
            distance = (i * pathLength) / (numPoints - 1)
            //根据当前距离获取对应的坐标点和正切点
            pathMeasure.getPosTan(distance, position, tangent)
            mX[i] = position[0]
            mY[i] = position[1]
            //利用反正切函数得到角度
            mAngle[i] = fixAngle((atan2(tangent[1].toDouble(), tangent[0].toDouble()) * 180f / Math.PI).toFloat())
        }
    }

    fun getPathLength(): Int {
//        因为PRECISION = 0.5
//        return (int) (mNumPoints * PRECISION);
        return mNumPoints / 2
    }

    /**
     * 调整角度， 使其在0 ~ 360
     */
    private fun fixAngle(rotation: Float): Float {
        val angle = 360f
        var finalRotation = rotation
        if (rotation < 0) {
            finalRotation = rotation + angle
        }
        if (rotation > angle) {
            finalRotation = rotation % angle
        }
        return finalRotation
    }

    fun getValue(@FloatRange(from = 0.0, to = 1.0) fraction: Float): PosTan? {
        //超出范围直接返回空
        if (fraction >= 1 || fraction < 0) {
            return null
        } else {
            var index = (fraction * mNumPoints).toInt()
            mTemp.set(mX[index], mY[index], mAngle[index])
            return mTemp
        }
    }
}