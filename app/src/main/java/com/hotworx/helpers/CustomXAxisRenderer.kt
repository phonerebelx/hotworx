package com.hotworx.helpers

import android.graphics.Canvas
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler


class CustomXAxisRenderer(viewPortHandler: ViewPortHandler, xAxis: XAxis, trans: Transformer) : XAxisRenderer(viewPortHandler, xAxis, trans) {

    override fun drawLabel(
        c: Canvas,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        val lines = formattedLabel.split(" ")
        Utils.drawXAxisValue(c, lines[0], x, y + 5, mAxisLabelPaint, anchor, angleDegrees)
        for (i in 1 until lines.size) {
            Utils.drawXAxisValue(
                c, lines[i], x, (y + 5 + mAxisLabelPaint.textSize * i),
                mAxisLabelPaint, anchor, angleDegrees
            )
        }
    }
}