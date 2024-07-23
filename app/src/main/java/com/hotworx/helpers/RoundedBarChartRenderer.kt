package com.hotworx.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.hotworx.R

class RoundedBarChartRenderer(myContext: Context,chart: BarDataProvider?, animator: ChartAnimator?, viewPortHandler: ViewPortHandler?) : BarChartRenderer(chart, animator, viewPortHandler) {
    private var mRightRadius = 5f
    private var mLeftRadius = 5f
    private val mGradientColors = intArrayOf(
        ContextCompat.getColor(myContext, R.color.colorAccent),
        ContextCompat.getColor(myContext, R.color.colorStart)

    )

    fun setRightRadius(mRightRadius: Float) {
        this.mRightRadius = mRightRadius
    }

    fun setLeftRadius(mLeftRadius: Float) {
        this.mLeftRadius = mLeftRadius
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY


        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)


        val centerX = mViewPortHandler.contentLeft() + (mViewPortHandler.contentWidth() / 2f)

        val centerY = mViewPortHandler.contentTop() + (mViewPortHandler.contentHeight() / 2f)

        val topGradientColors = intArrayOf(mGradientColors[1], mGradientColors[1])
        val topGradientPositions = floatArrayOf(0.0f, 0.5f)
        val topGradient = LinearGradient(
            0f,
            0f,
            centerX,
            centerY,
            topGradientColors,
            topGradientPositions,
            Shader.TileMode.REPEAT
        )

        val bottomGradientColors = intArrayOf(mGradientColors[1], mGradientColors[0])
        val bottomGradientPositions = floatArrayOf(0.5f, 1.0f)
        val bottomGradient = LinearGradient(
            0f,
            centerY,
            centerX,
            mChart.height.toFloat(),
            bottomGradientColors,
            bottomGradientPositions,
            Shader.TileMode.REPEAT
        )


        val composeShader = ComposeShader(topGradient, bottomGradient,PorterDuff.Mode.ADD)

        mRenderPaint.shader = composeShader


        var j = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

            if (mChart.isDrawBarShadowEnabled) {
                // Draw shadow if needed
                if (mRightRadius > 0) {
                    c.drawRoundRect(
                        RectF(buffer.buffer[j], mViewPortHandler.contentTop(),
                            buffer.buffer[j + 2],
                            mViewPortHandler.contentBottom()), mRightRadius, mRightRadius, mShadowPaint)
                } else {
                    c.drawRect(buffer.buffer[j], mViewPortHandler.contentTop(),
                        buffer.buffer[j + 2],
                        mViewPortHandler.contentBottom(), mShadowPaint)
                }
            }

            if (mRightRadius > 0) {
                c.drawRoundRect(
                    RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3]), mRightRadius, mRightRadius, mRenderPaint)
            } else {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint)
            }
            j += 4
        }
    }
}
