package com.example.mycanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class DrawView(context: Context?) : View(context) {
    private var paint = Paint()
    override fun onDraw(canvas: Canvas) {

        canvas.apply {

            // draw outer rectangle
            paint.color = context.getColor(R.color.black_transparent)
            paint.strokeWidth = 3f
            rotate(10F)
            drawRect(400F, 10F, 900F, 400F, paint)

        }
    }
}