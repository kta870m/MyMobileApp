package vn.swinburne.demolab2


import android.annotation.SuppressLint
import android.content.Intent
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
    lateinit var txtErr : TextView
    lateinit var txtUsername : EditText
    lateinit var txtPassword : EditText
    lateinit var btnLogin : Button
    var USERNAME : String = "administrator"
    var PASSWORD : String = "1234567"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        };

        txtErr = findViewById(R.id.txt_err)
        txtUsername = findViewById(R.id.txt_username)
        txtPassword = findViewById(R.id.txt_password)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                if(USERNAME == txtUsername.text.toString() && PASSWORD == txtPassword.text.toString()) {
                    txtErr.setText("Successful")
                    txtErr.setTextColor(resources.getColor(R.color.green))
                    val intent = Intent(this, TestActivity::class.java)
                    startActivity(intent)
                }else{
                    txtErr.setText("Failed")
                    txtErr.setTextColor(resources.getColor(R.color.red))
                }
            }
        }
    }

    
}