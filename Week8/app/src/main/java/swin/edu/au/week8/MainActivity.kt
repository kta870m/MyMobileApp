package swin.edu.au.week8

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val lines = mutableListOf<String>()

        val inputStream = resources.openRawResource(R.raw.sample_text)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.forEachLine { lines.add(it) }
        reader.close()

        val adapter = TextAdapter(lines)
        recyclerView.adapter = adapter
    }
}