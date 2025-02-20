package vn.swinburne.climbinggames

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Initializing variables to keep track of the score, current hold, and fall status
    private var score = 0
    private var currentHold = 0
    private var hasFallen = false

    // This function sets up the activity layout and the initial state
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Setting the layout from XML

        // Finding UI components by their IDs
        val tvScore: TextView = findViewById(R.id.tvScore)
        val btnClimb: Button = findViewById(R.id.btnClimb)
        val btnFall: Button = findViewById(R.id.btnFall)
        val btnReset: Button = findViewById(R.id.btnReset)
        val txtFallMessage : TextView = findViewById(R.id.txtFallMessage)

        // Set up the climb button click listener
        btnClimb.setOnClickListener {
            if (!hasFallen && currentHold < 9) {
                // Only increase hold and score if the player hasn't fallen and hasn't reached the top
                currentHold++
                score += when (currentHold) {
                    in 1..3 -> 1 // Blue zone gives 1 point
                    in 4..6 -> 2 // Green zone gives 2 points
                    in 7..9 -> 3 // Red zone gives 3 points
                    else -> 0
                }
                updateScore(tvScore)
                Log.d("Climb", "Current hold: $currentHold, Score: $score")
            }
        }

        // Set up the fall button click listener
        btnFall.setOnClickListener {
            // Player can only fall if they are on the wall and have not already fallen
            if (currentHold in 1..8 && !hasFallen) {
                hasFallen = true
                score = (score - 3).coerceAtLeast(0) // Reduce score but not below 0
                updateScore(tvScore)
                val fallMessage = getString(R.string.fall_message, currentHold, score)
                Log.d("Fall", fallMessage)
                txtFallMessage.text = fallMessage;
                txtFallMessage.setTextColor(Color.RED)
            }
        }

        btnReset.setOnClickListener {
            score = 0
            currentHold = 0
            hasFallen = false
            updateScore(tvScore)
            Log.d("Reset", "Game reset")
            txtFallMessage.text = "";
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

    // Save the current game state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        outState.putInt("currentHold", currentHold)
        outState.putBoolean("hasFallen", hasFallen)
    }

    // Restore the game state when the activity is recreated
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score")
        currentHold = savedInstanceState.getInt("currentHold")
        hasFallen = savedInstanceState.getBoolean("hasFallen")
        updateScore(findViewById(R.id.tvScore))
    }
}
