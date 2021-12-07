package com.reeta.androidworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var name:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name=findViewById(R.id.tvName)

        //for checking all condition that we declared here
        val constraints=Constraints.Builder().setRequiresCharging(true).
            setRequiredNetworkType(NetworkType.CONNECTED).build()

        // for sending data from activity to worker class
        val data=Data.Builder().putInt("Limit",14).build()

        //for requesting one time work to worker class
        val oneTimeWorkRequest:OneTimeWorkRequest= OneTimeWorkRequestBuilder<DemoWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10,TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        val oneTimeWorkRequest2:OneTimeWorkRequest= OneTimeWorkRequestBuilder<DemoWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10,TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        //for mutiple work request use this same as onetimerequest just use every where periodicWorkRequest object
        val periodicWorkRequest:PeriodicWorkRequest= PeriodicWorkRequestBuilder<DemoWorker>(1,TimeUnit.MINUTES)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

        // this work manager will work for more then one onetime request
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
            .then(oneTimeWorkRequest2)
            .enqueue()

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this
        ,{
            it?.let {
                val output:Data=it.outputData
                val message=output.getString("done")
                    Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()


                name.text=it.state.name
            }
        })

    }
}