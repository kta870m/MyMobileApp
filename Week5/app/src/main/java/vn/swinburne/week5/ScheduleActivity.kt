package vn.swinburne.week5

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity

class ScheduleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Load saved schedule if available
        val sharedPreferences = getSharedPreferences("SmartLightPrefs", Context.MODE_PRIVATE)
        val savedHour = sharedPreferences.getInt("scheduled_hour", -1)
        val savedMinute = sharedPreferences.getInt("scheduled_minute", -1)

        if (savedHour != -1 && savedMinute != -1) {
            timePicker.hour = savedHour
            timePicker.minute = savedMinute
        }

        saveButton.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            // Save schedule using SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("scheduled_hour", hour)
            editor.putInt("scheduled_minute", minute)
            editor.apply()

            // Show confirmation message
            Toast.makeText(this, "Schedule saved: $hour:$minute", Toast.LENGTH_SHORT).show()

            // Return to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}