package com.longwu.appcode.view
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.LogUtils

class ArcView : View {
    private var paint: Paint? = null
    private var paint2: Paint? = null
    private val startColor = Color.parseColor("#00FFFFFF")
    private val endColor = Color.WHITE

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = 2f
        paint!!.style = Paint.Style.STROKE

        paint2 = Paint()
        paint2!!.isAntiAlias = true
        paint2!!.strokeWidth = 3f
        paint2!!.style = Paint.Style.FILL
    }
    // 根据测量模式确定自定义 View 的宽度和高度
    var width: Float=300f
    var height: Float=150f
    var currentTime = 12;
    var sunOn = 5;
    var sunOff = 19;
    var currentSunX:Double= 0.0;
    var currentSunY:Double= 0.0;

    var centerY = 0.0f
    var centerX = 0.0f
    var radius =0.0f

    fun calSunXY(){
//        val centerY = height / 2
//        val centerX = width / 2
//        val radius = Math.min(width, height) / 2
        val startAngle = Math.PI * (10.0 / 6.0) // 起始角度，9点位置

        val endAngle = Math.PI * (2.0 / 6.0) // 结束角度，3点位置


        val angle = (endAngle - startAngle) / 12.0 // 每个数字之间的角度差


//        for (i in 0..12) {
        val currentAngle = startAngle + angle * 0
        currentSunX = (centerX - radius * Math.cos(currentAngle)).toDouble()
        val sinY = radius * Math.sin(currentAngle)
        currentSunY= (centerY - sinY).toDouble()
            // 打印数字的坐标
        LogUtils.e("ArcViewTest","数字  的坐标：X = $x, Y = $y  sinY=$sinY")
//        }



//        if (currentTime == 12) {
//            val d = 30 + 60*(currentTime-sunOn)/(12-sunOn).toDouble()
////            LogUtils.e("ArcViewTest","度数 d=$d ")
////            currentSunY = 0.0
////            currentSunX = centerX.toDouble()
//            val radian = Math.toRadsinians(d)
//            currentSunY = Math.sin(radian)*radius
//            currentSunX = Math.cos(radian)*radius
//            LogUtils.e("ArcViewTest","onDraw d=$d  currentSunY=$currentSunY  centerY=$centerY  Math.sin(radian)*radius=${Math.sin(radian)}")
//            LogUtils.e("ArcViewTest","onDraw d=$d  currentSunX=$currentSunX  centerX=$centerX  Math.cos(radian)*radius=${Math.cos(radian)}")
//
//        }else  if (currentTime < 12 && currentTime > sunOn) {
//            val d = 30 + 60*(currentTime-sunOn)/(12-sunOn).toDouble()
//            LogUtils.e("ArcViewTest","度数 d=$d ")
//            currentSunY = Math.sin(d)*radius
//            currentSunX = Math.cos(d)*radius
//            LogUtils.e("ArcViewTest","onDraw currentSunY=$currentSunY  centerY=$centerY  Math.sin(d)*radius=${Math.sin(d)*radius}")
//            LogUtils.e("ArcViewTest","onDraw currentSunX=$currentSunX  centerX=$centerX  Math.cos(d)*radius=${Math.cos(d)*radius}")
//
//        } else if (currentTime > 12 && currentTime < sunOff) {
//            val d = 90 - (sunOff-currentTime).toDouble()
//            currentSunY = centerY-Math.sin(d)*radius
//            currentSunX = centerX-Math.cos(d)*radius
//        }


    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        // 调用父类的 onMeasure() 方法，确保测量规格被正确处理
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        // 获取测量模式和大小
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
//
//
//        if (widthMode == MeasureSpec.EXACTLY) {
//            // 确切的测量模式，宽度已经指定
//            width = widthSize.toFloat()
//        } else {
//            // 非确切的测量模式，根据内容计算宽度
//            width = 300.toFloat() // 自定义方法，根据需要实现
//        }
//        if (heightMode == MeasureSpec.EXACTLY) {
//            // 确切的测量模式，高度已经指定
//            height = heightSize.toFloat()
//        } else {
//            // 非确切的测量模式，根据内容计算高度
//            height = 100.toFloat() // 自定义方法，根据需要实现
//        }
////        calSunXY();
////        LogUtils.e("ArcViewTest","onMeasure currentSunX=$currentSunX currentSunY=$currentSunY")
//        // 设置测量宽度和高度
//        setMeasuredDimension(width.toInt(), height.toInt())
//    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
         width = canvas.width.toFloat()
         height = canvas.height.toFloat()
         centerY = height/2
         centerX = width / 2




         radius = Math.min(width, height) / 2
        LogUtils.e("ArcViewTest","onDraw width=$width height=$height")
        LogUtils.e("ArcViewTest","onDraw centerX=$centerX centerY=$centerY radius=$radius")
        calSunXY();
//        LogUtils.e("ArcViewTest","onDraw currentSunX=$currentSunX currentSunY=$currentSunY")

        val shader: Shader = LinearGradient(
            (centerX - radius).toFloat(),
            (height / 2).toFloat(),
            (centerX + radius).toFloat(),
            (height / 2).toFloat(),
            intArrayOf(startColor, endColor, startColor),
            floatArrayOf(0f, .5f, 1f),
            Shader.TileMode.CLAMP
        )
        paint!!.shader = shader
        canvas.drawArc(
            (centerX - radius).toFloat(), 0f,
            (centerX + radius).toFloat(), height.toFloat(), 210f,120f , false, paint!!
        )

        val shader2: Shader = LinearGradient(
            centerX.toFloat(), 0f,
//            currentSunX.toFloat(), currentSunY.toFloat(),
            centerX.toFloat(), centerY/2.toFloat(),
            Color.parseColor("#bbFF6600"),
            Color.parseColor("#10FF6600"),
            Shader.TileMode.CLAMP
        )

        paint2!!.shader = shader2
        canvas.clipRect((centerX - radius).toInt(), 0, (centerX + radius).toInt(), (centerY/2).toInt()) // 限制绘制区域为从顶部到height/2

        canvas.drawArc(
            (centerX - radius).toFloat(), 0f,
            (centerX + radius).toFloat(), height.toFloat(),210f, 30f, true, paint2!!
        )

        paint2?.color = Color.WHITE
        paint2?.strokeWidth =20f
        paint2!!.style = Paint.Style.FILL

        canvas.drawPoint(centerX,centerY,paint2!!);
    }
}
