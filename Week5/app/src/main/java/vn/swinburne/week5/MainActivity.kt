package vn.swinburne.week5

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorPicker = findViewById<ColorPickerView>(R.id.colorPicker)
        val brightnessBar = findViewById<SeekBar>(R.id.brightnessBar)
        val onOffSwitch = findViewById<Switch>(R.id.onOffSwitch)
        val scheduleButton = findViewById<Button>(R.id.scheduleButton)

        colorPicker.setColorListener(ColorListener { color, _ ->
            window.decorView.setBackgroundColor(color)
        })

        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val alpha = progress / 100f
                window.decorView.setBackgroundColor(Color.argb((alpha * 255).toInt(), 255, 255, 0))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        onOffSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                window.decorView.setBackgroundColor(Color.YELLOW)
            } else {
                window.decorView.setBackgroundColor(Color.BLACK)
            }
        }

        scheduleButton.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
    }
}