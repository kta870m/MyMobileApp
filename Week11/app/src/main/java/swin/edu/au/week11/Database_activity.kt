package swin.edu.au.week11

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Database_activity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var studentList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)


        db = DBHelper(this)
        db.writableDatabase


        val etStudentName = findViewById<EditText>(R.id.et_student_name)
        val btnAddStudent = findViewById<Button>(R.id.btn_add_student)
        listView = findViewById(R.id.lv_students)

        studentList = db.readData()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        listView.adapter = adapter

        btnAddStudent.setOnClickListener {
            val name = etStudentName.text.toString().trim()
            if (name.isNotEmpty()) {
                db.insertData(name)
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show()
                etStudentName.text.clear()
                refreshList()
            }else{
                Toast.makeText(this, "Please enter a student name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun refreshList() {
        studentList.clear()
        studentList.addAll(db.readData())
        adapter.notifyDataSetChanged()
    }
}