package swin.edu.au.assigment3.database

// Necessary Android and Room library imports
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import swin.edu.au.assigment3.model.Note

// Annotation to define the entities used in the database and the version of the database
@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    // Define an abstract method to get DAO for accessing database operations
    abstract fun getNoteDao() : NoteDao

    companion object {
        @Volatile
        private var instance : NoteDatabase? = null
        private val LOCK = Any()

        // Singleton pattern to get the instance of the database
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also{
                instance = it
            }
        }

        // Helper function to create a new instance of the database
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}