package com.longwu.appcode.jobshdule

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.Constraints.Builder
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.longwu.appcode.R
import java.util.concurrent.TimeUnit

class BackgroundActivity : AppCompatActivity() {

    val TAG = "BackgroundActivity"
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        findViewById<Button>(R.id.JobScheduler).setOnClickListener {
            onJobStartClick()
        }

        findViewById<Button>(R.id.WorkManager).setOnClickListener {
            workManagerStart()
        }
    }

    private fun workManagerStart() {
        val constraints: Constraints = Builder()
            .setRequiresCharging(true)
            .build()

        /// OneTimeWorkRequest
        val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)


        // PeriodicWorkRequest
        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }


    private val JOB_INFO_ID = 10001
    private val JOB_PERIODIC = 15 * 60 * 1000L
    private fun onJobStartClick() {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_INFO_ID, componentName)
            .setPeriodic(JOB_PERIODIC)
            .build()
        Log.d(TAG, "onJobStartClick");
        jobScheduler.schedule(jobInfo)
    }
}