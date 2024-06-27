package com.longwu.appcode.location

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longwu.appcode.R
import com.longwu.appcode.base.BaseActivity
import com.longwu.appcode.util.ThreadUtils
import java.io.IOException
import java.util.Locale


class LocationActivity : BaseActivity() {
    private var TAG = "LocationActivity"
    override fun getContentViewLayoutID(): Int? {
        return R.layout.activity_location
    }
    var adapter:LocalRecyclerAdapter? = null
    val dataList = mutableListOf("开始测试")
    override fun initViewsAndEvents() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LocalRecyclerAdapter(dataList)
        recyclerView.adapter = adapter

        localHandler.postDelayed({
            getLocationAndCityName()
        }, 5000)
    }

    var localHandler = android.os.Handler();

    @SuppressLint("MissingPermission")
    private fun getLocationAndCityName() {
        Log.e("longwu", "getLocationAndCityName------- ${ThreadUtils.isMainThread()}")
        //kotlin 统计代码执行时间
        val startTime = System.currentTimeMillis()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationProvider = LocationManager.NETWORK_PROVIDER
        val lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
        //生成一个随机值，范围在-10.00000000000000 ~ 9.9999999999999之间
        val random = Math.random() * 0.5 - 0.00250000
        Log.e("longwu", "${lastKnownLocation?.longitude}  ${lastKnownLocation?.latitude}")
        val longitude = lastKnownLocation!!.longitude+random
        val latitude = lastKnownLocation!!.latitude+random
        val geocoder = Geocoder(this, Locale.ENGLISH)
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            Log.e("longwu", "00  ${e?.message}")
            e.printStackTrace()
        }

        val endTime = System.currentTimeMillis()
        Log.e("longwu", "getLocationAndCityName------- ${endTime - startTime}")
        Log.e("longwu", "${addresses?.size}}")

        if (addresses?.isNotEmpty()==true) {
            val address = addresses!![0]
            val countryName = address.countryName
            val adminArea = address.adminArea
            val featureName = address.featureName
            val countryCode = address.countryCode
            val locale = address.locale
            val postalCode = address.postalCode
            val subLocality = address.subLocality
            Log.e("longwu", "countryName=$countryName adminArea=$adminArea featureName=$featureName countryCode=$countryCode locale=$locale  postalCode=$postalCode  subLocality=$subLocality")
        }
        adapter?.addItem("$latitude $longitude  耗时：${endTime - startTime}ms")
        adapter?.notifyDataSetChanged()
        localHandler.postDelayed({
            getLocationAndCityName()
        }, 10000)
    }

    override fun onDestroy() {
        super.onDestroy()
        localHandler?.removeCallbacksAndMessages(null)
    }

}