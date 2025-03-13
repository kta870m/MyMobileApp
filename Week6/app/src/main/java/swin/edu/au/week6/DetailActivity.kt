package swin.edu.au.week6

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import swin.edu.au.week6.Equation
import swin.edu.au.week6.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val equation = intent.getParcelableExtra<Equation>("equation")
        equation?.let {
            binding.textQuestion.text = "${it.factor1} × ${it.factor2} = ${it.factor1 * it.factor2}"
        }

        binding.buttonCorrect.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("correct", true)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.buttonWrong.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("correct", false)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
