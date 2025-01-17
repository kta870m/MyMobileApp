package vn.swinburne.climbinggames

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var score = 0
    private var currentHold = 0
    private var hasFallen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvScore: TextView = findViewById(R.id.tvScore)
        val btnClimb: Button = findViewById(R.id.btnClimb)
        val btnFall: Button = findViewById(R.id.btnFall)
        val btnReset: Button = findViewById(R.id.btnReset)

        btnClimb.setOnClickListener {
            if (!hasFallen && currentHold < 9) {
                currentHold++
                score += when (currentHold) {
                    in 1..3 -> 1 // Blue zone
                    in 4..6 -> 2 // Green zone
                    in 7..9 -> 3 // Red zone
                    else -> 0
                }
                updateScore(tvScore)
                Log.d("Climb", "Current hold: $currentHold, Score: $score")
            }
        }

        btnFall.setOnClickListener {
            if (currentHold in 1..8 && !hasFallen) {
                hasFallen = true
                score = (score - 3).coerceAtLeast(0)
                updateScore(tvScore)
                Log.d("Fall", "Player has fallen. Current hold: $currentHold, Score: $score")
            }
        }

        btnReset.setOnClickListener {
            score = 0
            currentHold = 0
            hasFallen = false
            updateScore(tvScore)
            Log.d("Reset", "Game reset")
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateScore(tvScore: TextView) {
        val localizedScoreText = getString(R.string.score).replace("0", score.toString())
        tvScore.text = localizedScoreText
        when (currentHold) {
            in 1..3 -> tvScore.setTextColor(Color.BLUE) // Blue zone
            in 4..6 -> tvScore.setTextColor(Color.GREEN) // Green zone
            in 7..9 -> tvScore.setTextColor(Color.RED) // Red zone
            else -> tvScore.setTextColor(Color.BLACK) // Default color
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putInt("currentHold", currentHold)
        outState.putBoolean("hasFallen", hasFallen)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score")
        currentHold = savedInstanceState.getInt("currentHold")
        hasFallen = savedInstanceState.getBoolean("hasFallen")
        updateScore(findViewById(R.id.tvScore))
    }
}
