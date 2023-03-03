package com.longwu.appcode.permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.longwu.appcode.R
import com.longwu.appcode.base.BaseActivity
import com.longwu.appcode.util.PermissionRomUtil


class PermissionActivity : BaseActivity() {
    private val storage = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private var TAG = "permission_longwu"
    override fun getContentViewLayoutID(): Int? {
        return R.layout.activity_permission
    }

    override fun initViewsAndEvents() {
        findViewById<Button>(R.id.start).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (ContextCompat.checkSelfPermission(mContext!!,
                        storage) != PackageManager.PERMISSION_GRANTED
                ) {
                    showToast("还未授权，去请求权限")
                    ActivityCompat.requestPermissions(mContext as Activity, arrayOf(storage), 1)
                } else {
                    showToast("已授权")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult requestCode = $requestCode  resultCode = $resultCode")
    }

    var dialog: AlertDialog? = null;
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            Log.e(TAG, "onRequestPermissionsResult  grantResults = ${grantResults.contentToString()} | permissions=${permissions.contentToString()}")
            for (i in permissions.indices) {
                if (grantResults[i] == PermissionChecker.PERMISSION_GRANTED) { //选择了“始终允许”
                    Toast.makeText(this, "权限" + permissions[i] + "申请成功", Toast.LENGTH_LONG).show()
                } else {
                    showToast("用户选择了禁止不再询问")
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                            permissions[i])
                    ) { //用户选择了禁止不再询问
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                        builder.setTitle("权限申请")
                            .setMessage("点击允许才可以使用我们的app哦")
                            .setPositiveButton("去允许") { mDialog, id ->

                                 PermissionRomUtil.jumpPermissionPage(this)
//                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                val uri = Uri.fromParts("package",
//                                    packageName,
//                                    null) //注意就是"package",不用改成自己的包名
//                                intent.data = uri
//                                startActivityForResult(intent, 2)
                            }
                        dialog = builder.show()
                        break
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }

    private fun showToast(s: String) {
        Log.e(TAG, s)
        Toast.makeText(mContext, s, 0).show()
    }
}