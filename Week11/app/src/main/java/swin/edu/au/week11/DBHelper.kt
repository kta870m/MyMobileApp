package swin.edu.au.week11

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASESNAME = "UniManagament"
val TABLENAME = "Students"
val COL_NAME = "name"
val COL_ID = "id"

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASESNAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLENAME ($COL_ID INTEGER PRIMARY KEY, $COL_NAME VARCHAR(256))"
        db?.execSQL(createTable)
    }

    fun insertData(studentName: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, studentName)
        val result = db.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    fun readData(): MutableList<String> {
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        val list : MutableList<String> = ArrayList()
        if(result.moveToFirst()){
            do{
                var name = result.getString(result.getColumnIndex(COL_NAME))
                list.add(name)
            }while (result.moveToNext())
        }
        return list
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}