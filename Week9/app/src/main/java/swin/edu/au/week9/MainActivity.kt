package swin.edu.au.week9

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    lateinit var txtDisplay: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)

        // ViewModel instance
        val imageViewModel: MyViewModel by viewModels()

        // Observing LiveData
        imageViewModel.image.observe(this, Observer { image ->
            imageView.setImageDrawable(getDrawable(image))
            Log.d("LiveDataLog", "Image has been updated")
        })
    }

    fun callThread(){
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val job1: Deferred<String> = async {
                    Thread1()
                }

                val job2 : Deferred<String> = async {
                    Thread2()
                }
                val result1 = job1.await()
                val result2 = job2.await()
                val result = result1 + result2
                Log.d("T1", "Result" + result)
            }
            Log.d("T1", "Execute time" + executionTime.toString())
        }
    }

    suspend fun Thread1(): String{
        Log.d("T1","Start Thread 1")
        delay(1000)
        Log.d("T1","Finish Thread 1")
        return "Thread 1";
    }

    suspend fun Thread2(): String{
            Log.d("G2","Start Thread 2")
            delay(1700)
            Log.d("G2","Finish Thread 2")
        return "Thread 2";
    }
}