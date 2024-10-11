package com.example.helloworld

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // XMLレイアウトを使ってビューをセット
        setContentView(R.layout.activity_main)

        // Pythonの初期化 (AndroidPlatformの指定)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        //pythonのインスタンスを取得
        val py = Python.getInstance()

        // ComposeViewの設定
        val composeView = findViewById<ComposeView>(R.id.compose_view)

        // ボタンとカスタムビューの設定
        val myButton = findViewById<Button>(R.id.paint)
        val customView = findViewById<CustomView>(R.id.custom_view)
        val backButton = findViewById<Button>(R.id.Back_btn)
        val unlucky = findViewById<Button>(R.id.btn)
        val start = findViewById<Button>(R.id.complete)
        val arduino_test = findViewById<Button>(R.id.arduino_test)
        val python = Python.getInstance()

        //pythonスクリプトを呼び出し
        val pythonModule:PyObject = python.getModule("canny_and_pypx")

        //画像ファイルのパス指定(input,output)
        val inputImageId = R.drawable.regiscan
        val outputImagePath = "${getExternalFilesDir(null)?.absolutePath}/pixel_art_output.png"

        //Python関数を呼び出し、処理後の画像パスを取得
        //val result: PyObject = pythonModule.callAttr(
         //   "process_image",
           // "android.resource://$packageName/$inputImageId",  // 入力画像パス
            //outputImagePath,
            //128,  // ピクセルサイズ
            //16    // 色数
        //)
        backButton.setOnClickListener {
            // SecondActivityに遷移
            finish()
        } // ここで閉じる

        myButton.setOnClickListener {
            customView.visibility = View.VISIBLE // カスタムビューを表示
            customView.invalidate() // 再描画を要求
        }

        unlucky.setOnClickListener{
            Toast.makeText(this, "はずれ", Toast.LENGTH_LONG).show()
        }

        start.setOnClickListener{
            val intent = Intent(this,Result::class.java)
            startActivity(intent)
        }

        arduino_test.setOnClickListener {
            val intent = Intent(this, Arduino::class.java)
            startActivity(intent)
        }
    }

}
// CustomViewの定義 (従来のビュー)
class CustomView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private var paint: Paint? = null

    init {
        init()
    }

    private fun init() {
        paint = Paint().apply {
            color = Color.RED // 四角形の塗りつぶし色
            style = Paint.Style.FILL // 塗りつぶしを有効化
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // カスタムビューの中央に赤い正方形を描画
        val size = 300f // 正方形のサイズ
        val left = (width / 2 - size / 2)
        val top = (height / 2 - size / 2)
        canvas.drawRect(left, top, left + size, top + size, paint!!) // 四角形を描画
    }
}