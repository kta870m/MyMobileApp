package vn.swinburne.lightmeup

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var isToggled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "W03-LightMeUpAnswer"

        val iconToggle = findViewById<ImageView>(R.id.iconToggle)

        iconToggle.setOnClickListener {
            isToggled = !isToggled

            if (isToggled) {
                iconToggle.setImageResource(R.drawable.icon_late) // Icon khi toggle
            } else {
                iconToggle.setImageResource(R.drawable.icon_in) // Icon ban đầu
            }
        }

        iconToggle.setOnLongClickListener {
            isToggled = !isToggled

            if (isToggled) {
                iconToggle.setImageResource(R.drawable.icon_late)
            } else {
                iconToggle.setImageResource(R.drawable.icon_in)
            }
            true
        }
    }
}