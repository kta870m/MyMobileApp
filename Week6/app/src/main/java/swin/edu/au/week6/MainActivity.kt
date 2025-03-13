package swin.edu.au.week6

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import swin.edu.au.week6.Equation
import kotlin.random.Random
import swin.edu.au.week6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var correctAnswers = 0
    private val requestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generateNewEquation()

        binding.buttonNext.setOnClickListener { generateNewEquation() }
    }

    private fun generateNewEquation() {
        val factor1 = Random.nextInt(1, 13)
        val factor2 = Random.nextInt(1, 13)
        val equation = Equation(factor1, factor2)

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("equation", equation)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            val correct = data?.getBooleanExtra("correct", false) ?: false
            if (correct) {
                correctAnswers++
            }
            binding.textCorrectAnswers.text = "Correct answers: $correctAnswers"
        }
    }
}

