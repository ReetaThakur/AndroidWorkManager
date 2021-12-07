package com.reeta.androidworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class DemoWorker(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {

val TAG:String="deeksha"
    override fun doWork(): Result {
        Log.v(TAG,"Thread name ${Thread.currentThread().name}")

        displayNotification("Work Manager","Task Started")

        //for geting data from activity it is like intent
        val incomingData:Data =inputData
        val limit=incomingData.getInt("Limit",0)

        for (i in 1..limit){
           Thread.sleep(1000)
           Log.v(TAG, "The value is $i")
       }

        displayNotification("Work Manager","Task Completed")

        // for giving data back to activity like if task done then give result to activity that task is done
        val output:Data=Data.Builder().putString("done","Success !!").build()

        //Result.retry() if task is fail then try once again
        return Result.success(output)
    }

    // for showing notification
    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "masai",
                "masai",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "masai")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }
}