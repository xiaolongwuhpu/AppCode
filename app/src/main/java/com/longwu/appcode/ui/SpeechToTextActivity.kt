package com.longwu.appcode.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.longwu.appcode.R
import com.longwu.appcode.appwidget.AppWidgetManager
import java.util.*


class SpeechToTextActivity : AppCompatActivity() {

    lateinit var editText: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_to_text)
        editText = findViewById<EditText>(R.id.btn_widget)
        findViewById<Button>(R.id.btn_start).setOnClickListener(View.OnClickListener {
            speechInput()
        })
    }


    private fun speechInput() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) //ACTION_RECOGNIZE_SPEECH接收输入语音，ACTION_WEB_SEARCH触发网络搜索或语音操作
            //指定自有形式的输入
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            ) //EXTRA_LANGUAGE_MODEL表示用于输入音频的语言模型
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音") //EXTRA_PROMPT语音输入对话框中的提示字符串
            //intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);  //EXTRA_MAX_RESULTS限制潜在识别结果的数目
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);  //EXTRA_LANGUAGE指定默认值以外的输入语言
            startActivityForResult(intent, VOICE_RECOGNITION)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "找不到语音设备", 1).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK) {
            val results: ArrayList<String>?
            results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val confidence: FloatArray?
            val confidenceExtra = RecognizerIntent.EXTRA_CONFIDENCE_SCORES
            confidence = data?.getFloatArrayExtra(confidenceExtra)
            Log.e("longwu","results = "+results)
            Log.e("longwu","confidence = "+confidence?.size)
            confidence?.forEach {
                Log.e("longwu","forEach confidence = "+it)
            }
            editText?.setText(results?.get(0)?.toString() ?: "")
            //在这里使用识别的语音字符串results做点事情
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
