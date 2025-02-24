package swin.edu.au.week7

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class ListActivity : AppCompatActivity() {
    lateinit var isStudent : RecyclerView;
    var strList: Array<String> = arrayOf("Haky04", "Kta87m", "Freeing");
    var imgList: Array<Int> = arrayOf(R.drawable.pro_img, R.drawable.pro_img_2, R.drawable.pro_img_3);
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isStudent=findViewById(R.id.lsStudent);
        isStudent.layoutManager=LinearLayoutManager(this);
        var data = ArrayList<DataClass>();
        for(i in strList.indices){
            data.add(DataClass(strList[i].toString(), imgList[i]))
        }
        var adapter = RecycleViewAdapter(data);
        isStudent.setAdapter(adapter);


//        isStudent.setOnItemClickListener({ parent, view, position, id ->
//            var element = adapter.getItem(position) as String
//            Snackbar.make(view, element, Snackbar.LENGTH_LONG).show()
//        });
    }
}