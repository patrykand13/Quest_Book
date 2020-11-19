package pl.momlok.questbook.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_list_quest.*
import pl.momlok.questbook.R
import pl.momlok.questbook.database.CardViewAdapterListQuests
import pl.momlok.questbook.database.DateBaseHelper
import pl.momlok.questbook.database.ListQuestInfo
import pl.momlok.questbook.database.TasksInfo
import pl.momlok.questbook.objects.Quest
import java.lang.Exception

class ListQuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quest)
    }


    override fun onResume() {
        super.onResume()
        val dbHelper = DateBaseHelper(applicationContext)
        val db = dbHelper.writableDatabase
        val cursor = db.query(ListQuestInfo.LIST_QUEST_NAME, null, null, null, null, null, null)

        val quests = ArrayList<Quest>()

        if(cursor.count > 0){
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val quest = Quest()
                quest.id = cursor.getInt(0)
                quest.listTitle = cursor.getString(1)
                quest.listStatus = cursor.getString(2)
                quests.add(quest)
                cursor.moveToNext()
            }
            cursor.close()
            recyclerViewListQuest.layoutManager = GridLayoutManager(applicationContext,2)
            recyclerViewListQuest.adapter = CardViewAdapterListQuests(applicationContext, db, quests)
        }

        addBTquest.setOnClickListener {
            var questEdit = questName.text
            var quest = questEdit.toString()
            if(!quest.equals("")){
                try {
                    db?.execSQL("CREATE TABLE ${questEdit}(" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "${TasksInfo.TASKS_TITLE} TEXT NOT NULL," +
                            "${TasksInfo.TASKS_STATUS} TEXT NOT NULL)")
                    val value = ContentValues()
                    value.put(ListQuestInfo.LIST_QUEST_TITLE,quest)
                    value.put(ListQuestInfo.LIST_QUEST_STATUS,"0/0")
                    db.insert(ListQuestInfo.LIST_QUEST_NAME, null, value)

                    var edit_intent = Intent(applicationContext, QuestActivity::class.java)
                    edit_intent.putExtra("questEdit",questName.text)
                    startActivity(edit_intent)
                }catch (e: Exception){
                    Toast.makeText(applicationContext,"Lista o podanej nazwie już istnieje",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext,"Podaj nazwe listy zadań",Toast.LENGTH_SHORT).show()
            }
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