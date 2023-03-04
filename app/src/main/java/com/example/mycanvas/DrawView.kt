package com.example.mycanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class DrawView(context: Context?) : View(context) {
    private var paint = Paint()
    override fun onDraw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.strokeWidth = 3f
        canvas.drawRect(30F, 30F, 80F, 80F, paint)
        paint.strokeWidth = 0f
        paint.color = Color.CYAN
        canvas.drawRect(33F, 60F, 77F, 77F, paint)
        paint.color = Color.YELLOW
        canvas.drawRect(33F, 33F, 77F, 60F, paint)
    }
}