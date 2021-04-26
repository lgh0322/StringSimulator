package com.vaca.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import java.lang.Thread.sleep


class StringSurfaceView : SurfaceView, Runnable {

    var surfaceHolder: SurfaceHolder = this.holder
    var canvas: Canvas? = null
    private val wavePaint = Paint()
    private val bgPaint = Paint()

    val drawSize = 500

    val dt=48f
    val dx=50f
    val a=1f
    val b=0.00001f


    val C=a*(dt/dx)*(dt/dx)
    val D=dt*dt
    val F=-b/(dt*dt)

    val force=FloatArray(drawSize){
        0f
    }

    val stringNow=FloatArray(drawSize){
        0f
    }

    val stringFuture=FloatArray(drawSize){
        0f
    }


    val stringPast=FloatArray(drawSize){
        0f
    }


    init {
        for(k in 0 until drawSize){
            stringNow[k]=Math.sin(k.toFloat()/drawSize*20*Math.PI).toFloat()*200
            stringPast[k]=Math.sin(k.toFloat()/drawSize*20*Math.PI).toFloat()*200
        }


    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {

        wavePaint.apply {
            color = getColor(R.color.black)
            style = Paint.Style.STROKE
            strokeWidth = 5.0f
            isAntiAlias=true
        }

        bgPaint.apply {
            color = getColor(R.color.black)
            style = Paint.Style.FILL
            textSize=60f
            isAntiAlias=true
        }


    }

    lateinit var w: Rect
    val li = FloatArray(2000)

    fun Float.abs():Float=if(this<0){
        -this
    }else{
        this
    }

    private fun onDrawX(canvas: Canvas) {
        canvas.drawARGB(255, 255, 255, 255)

        val wavePath = Path()
        wavePath.moveTo(0f,height/2-stringNow[0])

        for((index,k) in stringNow.withIndex()){
            wavePath.lineTo(width.toFloat()/(drawSize)*index,height/2-k)
        }
        canvas.drawPath(wavePath,wavePaint)

        for(k in 1 until drawSize-1){
            val v=stringNow[k]-stringPast[k]
            force[k]=F*v*v.abs()
        }



        for(k in 1 until drawSize-1){
            stringFuture[k]=C*(stringNow[k+1]+stringNow[k-1]-2*stringNow[k])+D*force[k]+2*stringNow[k]-stringPast[k]
        }

        for(k in 0 until drawSize){
            stringPast[k]=stringNow[k]
            stringNow[k]=stringFuture[k]
        }



        canvas.drawText("String simulator by vaca", 100f, 100f, bgPaint)
    }


    private fun onDrawX() {


        for(k in 1 until drawSize-1){
            val v=stringNow[k]-stringPast[k]
            force[k]=F*v*v.abs()
        }



        for(k in 1 until drawSize-1){
            stringFuture[k]=C*(stringNow[k+1]+stringNow[k-1]-2*stringNow[k])+D*force[k]+2*stringNow[k]-stringPast[k]
        }

        for(k in 0 until drawSize){
            stringPast[k]=stringNow[k]
            stringNow[k]=stringFuture[k]
        }


    }



    private fun getColor(resource_id: Int): Int {
        return ContextCompat.getColor(context, resource_id)
    }

    var isRun = false
    private var thread: Thread? = null

    var t1 = System.currentTimeMillis()
    var t2 = 0
    var t3 = 0L
    var t4 = 0

    fun resume() {
        isRun = true
        thread = Thread(this)
        thread?.start()
    }

    override fun run() {
        while (isRun) {
            if (surfaceHolder.surface.isValid) {
                val m = surfaceHolder.lockCanvas()
                onDrawX(m)
                surfaceHolder.unlockCanvasAndPost(m)
            }else{
                onDrawX()
                sleep(1)
            }
        }
    }

    var x1 = 0f
    var y1 = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y

            }

            MotionEvent.ACTION_UP -> {

            }

            MotionEvent.ACTION_MOVE -> {

            }
        }
        return super.onTouchEvent(event)
    }


    fun pause() {
        isRun = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
        }
    }
}


