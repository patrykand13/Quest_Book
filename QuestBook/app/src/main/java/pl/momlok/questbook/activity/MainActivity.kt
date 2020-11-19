package pl.momlok.questbook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.momlok.questbook.objects.Note
import pl.momlok.questbook.R
import pl.momlok.questbook.database.CardViewAdapterNotes
import pl.momlok.questbook.database.DateBaseHelper
import pl.momlok.questbook.database.NotesInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val dbHelper = DateBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase
        val cursor = db.query(NotesInfo.NOTE_NAME, null, null, null, null, null, null)

        val notes = ArrayList<Note>()

        if(cursor.count > 0){
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val note = Note()
                note.id = cursor.getInt(0)
                note.title = cursor.getString(1)
                note.message = cursor.getString(2)
                notes.add(note)
                cursor.moveToNext()
            }
            cursor.close()
            recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
            recyclerView.adapter = CardViewAdapterNotes(applicationContext, db, notes)
        }
    }

    fun goQuest(view: View){
        var intent = Intent(applicationContext, ListQuestActivity::class.java)
        startActivity(intent)
    }
    fun goNote(view: View){
        var intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
    fun newNote(view: View){
        var intent = Intent(applicationContext, NotesActivity::class.java)
        startActivity(intent)
    }



}