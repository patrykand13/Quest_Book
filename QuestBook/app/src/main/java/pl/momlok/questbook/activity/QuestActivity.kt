package pl.momlok.questbook.activity

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_quest.*
import pl.momlok.questbook.R
import pl.momlok.questbook.objects.Task
import pl.momlok.questbook.database.CardViewAdapterQuest
import pl.momlok.questbook.database.DateBaseHelper
import pl.momlok.questbook.database.ListQuestInfo
import pl.momlok.questbook.database.TasksInfo

class QuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

    }

    override fun onResume() {
        super.onResume()
        var idEdit =""
        var tab = ""
        val dbHelper = DateBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase
        if (intent.hasExtra("idEdit")) {
            idEdit = intent.getStringExtra("idEdit")
        }

        if (intent.hasExtra("questEdit")) {
            var tabela = intent.getCharSequenceExtra("questEdit")
            tab = tabela.toString()
        }

        try {
            widok(idEdit,db, tab)
        }catch (e: Exception){
            Toast.makeText(applicationContext,"Nie mozna odswiezyc", Toast.LENGTH_SHORT).show()
        }


    }

    fun widok(idEdit: String, db: SQLiteDatabase, tab:String){

        var db = db
            var tab = tab
            questNameTask.text = "Tytuł zadania: $tab"

            var cursor = db.query(
                    tab, null,
                    null, null,
                    null, null, null
            )

            val tasks = ArrayList<Task>()
            var done = 0
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val task = Task()
                    task.id = cursor.getInt(0)
                    task.taskTitle = cursor.getString(1)
                    task.taskStatus = cursor.getString(2)
                    if(cursor.getString(2) == "true") done++
                    tasks.add(task)
                    cursor.moveToNext()
                }
            }
            var cur = cursor.count

            cursor.close()

            var curdon: String = "$done/$cur"

            val value = ContentValues()
            value.put(ListQuestInfo.LIST_QUEST_TITLE,tab)
            value.put(ListQuestInfo.LIST_QUEST_STATUS,curdon)
            var idEdit = idEdit
            db.update(
                    ListQuestInfo.LIST_QUEST_NAME, value, BaseColumns._ID + "=?",
                    arrayOf(intent.getStringExtra("idEdit"))
            )


            try {
                recyclerViewQuest.layoutManager = GridLayoutManager(applicationContext, 1)
                recyclerViewQuest.adapter = CardViewAdapterQuest(applicationContext, db, tasks,tab)
            }catch (e:Exception){ Toast.makeText(applicationContext, "Blad ladowania", Toast.LENGTH_SHORT).show()}

            addQuestBT.setOnClickListener {
                var title = taskName.text.toString()

                val value2 = ContentValues()
                value2.put(TasksInfo.TASKS_TITLE, title)
                value2.put(TasksInfo.TASKS_STATUS, "false")
                db.insert(tab, null, value2)
                Toast.makeText(applicationContext, "Zadanie zapisane $title", Toast.LENGTH_SHORT).show()
                taskName.setText("")
                widok(idEdit, db, tab)
            }

            refreshBT.setOnClickListener {
                widok(idEdit, db, tab)
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
}