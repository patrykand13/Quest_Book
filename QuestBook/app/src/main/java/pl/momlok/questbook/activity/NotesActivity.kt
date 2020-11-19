package pl.momlok.questbook.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notes.*
import pl.momlok.questbook.R
import pl.momlok.questbook.database.DateBaseHelper
import pl.momlok.questbook.database.NotesInfo

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val dbHelper = DateBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase


        if(intent.hasExtra("title")) titleNote.setText(intent.getStringExtra("title"))
        if(intent.hasExtra("message")) messageNote.setText(intent.getStringExtra("message"))
    }

    fun saveNote(view: View){
        val dbHelper = DateBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase
        val title = titleNote.text.toString()
        val message = messageNote.text.toString()
        val infoSaveToast = Toast.makeText(applicationContext,"Notatka zapisana",Toast.LENGTH_SHORT)

        val value = ContentValues()
        value.put(NotesInfo.NOTE_COLUMN_TITLE,title)
        value.put(NotesInfo.NOTE_COLUMN_MESSAGE,message)

        if(intent.hasExtra("ID")){
            db.update(
                    NotesInfo.NOTE_NAME, value, BaseColumns._ID + "=?",
                    arrayOf(intent.getStringExtra("ID"))
            )
            Toast.makeText(applicationContext, "Zmieniono dane", Toast.LENGTH_SHORT).show()

        }else{
            if(!title.isNullOrEmpty() || !message.isNullOrEmpty()){
                db.insert(NotesInfo.NOTE_NAME, null, value)
                infoSaveToast.show()
            }else{
                Toast.makeText(applicationContext, "Nie ma co zapisac", Toast.LENGTH_SHORT).show()
            }
        }

        db.close()
        onBackPressed()
    }
}