package com.vaca.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val string:StringSurfaceView=findViewById(R.id.fuck)
        string.resume()
    }
}