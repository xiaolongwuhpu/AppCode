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
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*


class FlatBuffersMapActivity : AppCompatActivity() {
    val mapJson = Hashtable<String, String>()
    val mapFbs = Hashtable<String, String>()
    val maptest = Hashtable<String, String>()
    var path: String? = null
    var pathNewJson: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_buffers)
        path = filesDir.absolutePath + "/app_category.fbs"
        pathNewJson = filesDir.absolutePath + "/pathNewJson.json"

        btn_creat_map_data.setOnClickListener {
            startCreatData()
        }

        btn_start_convert.setOnClickListener {
            mapJson.clear()
            startTest()
        }

        btn_start_test_json.setOnClickListener {
            mapJson.clear()
            loadJson()
        }

        btn_test_fbs.setOnClickListener {
            mapFbs.clear()
            loadFbs()
        }
    }

    private fun startCreatData() {
        for (i in 0..34000) {
            maptest["testkey$i"] = "testvalue$i"
        }
        val jsonObject = JSONObject(maptest as Map<*, *>?)
        val jsonString = jsonObject.toString()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(pathNewJson)
            fos.write(jsonString.toByteArray())
            fos.close()
            Log.e("longwu", "new json write success")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("longwu", "new json write error $e")
        } finally {
            fos?.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startTest() {
        loadJson()
        val builder = FlatBufferBuilder()
        val keyValuesOffsets = IntArray(mapJson.size)
        var index = 0
        for ((key: String, value: String) in mapJson) {
            val packageNameOffset = builder.createString(key)
            val categoryIdOffset = builder.createString(value)
            val keyValuesOffset =
                KeyVaules.createKeyVaules(builder, packageNameOffset, categoryIdOffset)
            keyValuesOffsets[index] = keyValuesOffset
            index++
        }
        // 构建TestMap对象
        var testMapOffset = TestMap.createAppCategoriesVector(builder, keyValuesOffsets);
        TestMap.startTestMap(builder);
        TestMap.addAppCategories(builder, testMapOffset);
        var rootOffset = TestMap.endTestMap(builder);
        // 完成构建
        builder.finish(rootOffset)

        val flatbuffersData = builder.sizedByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(path)
            fos.write(flatbuffersData)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
    }

    private fun loadFbs(): Map<String, String>? {
        var t1 = System.currentTimeMillis()
//        val flatBuffers: ByteBuffer = TextUtil.loadFbsFileFromFile(this, path)
        val flatBuffers: ByteBuffer = TextUtil.loadFbsFileFromAssets(this, "raw/app_category.fbs")
        val appCategories = TestMap.getRootAsTestMap(flatBuffers)
        val appCategoriesLength = appCategories.appCategoriesLength()
        Log.d("longwu", "appCategoriesLength $appCategoriesLength")
        for (i in 0 until appCategoriesLength) {
            val appCategory = appCategories.appCategories(i)
            mapFbs[appCategory.packageName()] = appCategory.categoryId()
        }
        var t2 = System.currentTimeMillis()
        Log.e("longwu", "fbs  " + (t2 - t1) + "ms | size=${mapFbs.size}")
        return mapFbs
    }

    private fun loadJson(): Map<String, String>? {
        var t1 = System.currentTimeMillis()
        val jsonObject =
            TextUtil.getJsonObjectFromFile(this, "pathNewJson.json") ?: return HashMap()
        try {
            val ss = jsonObject.toString()
            Log.e("longwu", "ss = $ss")
            val it: Iterator<*> = jsonObject.keys()
            var key: String?
            var value: String?
            while (it.hasNext()) {
                key = it.next() as String?
                value = jsonObject.getString(key)
                mapJson[key] = value
            }
            var t2 = System.currentTimeMillis()
            Log.e("longwu", "json  " + (t2 - t1) + "ms | size=${mapJson.size}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("longwu", "json error $e")
            return null
        }
        return mapJson
    }
}
