package com.example.mycanvas

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drawView = DrawView(this)
        drawView.setBackgroundColor(Color.BLACK)
        setContentView(drawView)
    }
}