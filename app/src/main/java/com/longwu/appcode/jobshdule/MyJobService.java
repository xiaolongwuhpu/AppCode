package com.longwu.appcode.jobshdule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("MyJobService", "Job started");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("MyJobService", "Job onStopJob");
        return false;
    }
}
