package vn.swinburne.demolab2


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var btnPlus: Button
    lateinit var btnMinus: Button
    lateinit var btnDivide: Button
    lateinit var btnMutiply: Button
    lateinit var txtNum1: EditText
    lateinit var txtNum2: EditText
    lateinit var txtResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        };

        btnPlus = findViewById(R.id.btn_plus);
        btnMinus = findViewById(R.id.btn_minus);
        btnDivide = findViewById(R.id.btn_devide);
        btnMutiply = findViewById(R.id.btn_multiply);
        txtNum1 = findViewById(R.id.txt_num_1);
        txtNum2 = findViewById(R.id.txt_num_2);
        txtResult = findViewById(R.id.txt_result);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnMutiply.setOnClickListener(this);



    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_plus -> {
                txtResult.setText("Result: " + (txtNum2.getText().toString().toDouble() + txtNum1.getText().toString().toDouble()))
            }
            R.id.btn_minus -> {
                txtResult.setText("Result: " + (txtNum2.getText().toString().toDouble() - txtNum1.getText().toString().toDouble()))
            }
            R.id.btn_devide -> {
                txtResult.setText("Result: " + (txtNum2.getText().toString().toDouble() / txtNum1.getText().toString().toDouble()))
            }
            R.id.btn_multiply -> {
                txtResult.setText("Result: " + (txtNum2.getText().toString().toDouble() * txtNum1.getText().toString().toDouble()))
            }
        }
    }

    
}