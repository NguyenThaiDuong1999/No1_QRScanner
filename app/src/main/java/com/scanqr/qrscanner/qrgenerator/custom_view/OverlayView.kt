package com.scanqr.qrscanner.qrgenerator.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#80000000")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val path = Path()
    private val scanRect = Rect()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val frameWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._160sdp)
        val frameHeight = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._160sdp)

        val left = (width - frameWidth) / 2
        val top = (height - frameHeight) / 2
        val right = left + frameWidth
        val bottom = top + frameHeight
        scanRect.set(left, top, right, bottom)

        path.reset()
        path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
        path.addRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), Path.Direction.CCW)

        canvas.drawPath(path, overlayPaint)
    }

    fun getScanRect(): Rect {
        return scanRect
    }
}