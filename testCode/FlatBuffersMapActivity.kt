package com.longwu.appcode.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.flatbuffers.FlatBufferBuilder
import com.longwu.appcode.R
import com.longwu.appcode.bean.KeyVaules
import com.longwu.appcode.bean.TestMap
import com.longwu.appcode.util.TextUtil
import kotlinx.android.synthetic.main.activity_flat_buffers.*
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*


class FlatBuffersMapActivity : AppCompatActivity() {
    val map = Hashtable<String, String>()
    val map2 = Hashtable<String, String>()
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_buffers)
        btn_start_convert.setOnClickListener {
            TestMap()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun TestMap() {
        val map: MutableMap<String, String> = HashMap()
        for (i in 0..500) {
            map["testkey$i"] = "testvalue$i"
        }
        val builder = FlatBufferBuilder()
        val appCategoryOffsets: MutableList<Int> = ArrayList()
        for ((key: String, value: String) in map) {
            val packageNameOffset = builder.createString(key)
            val categoryIdOffset = builder.createString(value)
            KeyVaules.startKeyVaules(builder)
            KeyVaules.addPackageName(builder, packageNameOffset)
            KeyVaules.addCategoryId(builder, categoryIdOffset)
            val appCategoryOffset = KeyVaules.endKeyVaules(builder)
            appCategoryOffsets.add(appCategoryOffset)
        }

        val mapOffset = TestMap.createAppCategoriesVector(builder,
            appCategoryOffsets.stream().mapToInt { obj: Int -> obj }.toArray());

        TestMap.startTestMap(builder)
        TestMap.addAppCategories(builder, mapOffset)
        val appCategoryOffset: Int = KeyVaules.endKeyVaules(builder)
        builder.finish(appCategoryOffset)
        val flatbuffersData = builder.sizedByteArray()
        try {
            var path = filesDir.absolutePath + "/app_category.fbs"
            val fos = FileOutputStream(path)
            fos.write(flatbuffersData)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val appCategories = TestMap.getRootAsTestMap(builder.dataBuffer())
        val appCategoriesLength = appCategories.appCategoriesLength()
        for (i in 0 until appCategoriesLength) {
            val appCategory = appCategories.appCategories(i)
            Log.d("longwu",  appCategory.packageName())
            Log.d("longwu",  appCategory.categoryId())
        }
    }
}
