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

/*
          Work Manager = WorkManager is an API that makes it easy to schedule reliable,
                          asynchronous tasks that are expected to run even if the app exits
                          or the device restarts.that is guaranteed to execute sometime
                          after its Constraints are met.

                          There are two types of work supported by WorkManager:
                          OneTimeWorkRequest and PeriodicWorkRequest.

               A WorkRequest has an associated id that can be used for lookups and
               observation.

         Features :-

       1)  Work Constraints :-Declaratively define the optimal conditions for your work to
                              run using Work Constraints. (For example, run only when the
                              device is on Wi-Fi, when the device is idle, or when it has
                              sufficient storage space, etc.)

      2) Robust Scheduling :- WorkManager allows you to schedule work to run one- time or
                              repeatedly using flexible scheduling windows.

        And Scheduled work is stored in an internally managed SQLite database.

     3) Flexible Retry Policy :- Sometimes work fails. WorkManager offers flexible retry policies.

     4) Work Chaining :- For complex related work, chain individual work tasks together

     ex:- WorkManager.getInstance(...)
    .beginWith(listOf(workA,workB))
    .then(workC)
    .enqueue()
*/