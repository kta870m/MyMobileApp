package vn.swinburne.caculations

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
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
        supportActionBar?.title = "W3-calculations"

        val txtNum1 = findViewById<EditText>(R.id.txtNum1)
        val txtNum2 = findViewById<EditText>(R.id.txtNum2)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val btnEqual = findViewById<Button>(R.id.btnEqual)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnEqual.setOnClickListener {
            val num1 = txtNum1.text.toString().toDoubleOrNull()
            val num2 = txtNum2.text.toString().toDoubleOrNull()

            if (num1 != null && num2 != null) {
                val result = when (radioGroup.checkedRadioButtonId) {
                    R.id.rdPlus -> num1 + num2
                    R.id.rdMinus -> num1 - num2
                    R.id.rdMulti -> num1 * num2
                    R.id.rdDivide -> {
                        if (num2 != 0.0) num1 / num2
                        else getString(R.string.error_divide_by_zero)
                    }

                    else -> getString(R.string.error_invalid_input)
                }
                tvResult.text = "${getString(R.string.result_text)} $result"
            } else {
                tvResult.text = getString(R.string.error_invalid_input)
            }
        }
    }
}