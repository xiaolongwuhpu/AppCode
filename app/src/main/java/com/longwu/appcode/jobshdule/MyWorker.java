package com.longwu.appcode.jobshdule;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.util.Log;

public class MyWorker extends Worker {

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("MyWorker", "Work started");
        // 执行后台任务
        try {
            Thread.sleep(3000); // 模拟后台任务
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }
        Log.d("MyWorker", "Work finished");
        return Result.success(); // 返回成功结果
    }
}
