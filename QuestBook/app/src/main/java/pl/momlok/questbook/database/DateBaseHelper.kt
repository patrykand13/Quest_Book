package pl.momlok.questbook.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object NotesInfo: BaseColumns {
    const val NOTE_NAME = "Notes"
    const val NOTE_COLUMN_TITLE = "title"
    const val NOTE_COLUMN_MESSAGE = "message"
}

object ListQuestInfo: BaseColumns{
    const val LIST_QUEST_NAME = "ListQuest"
    const val LIST_QUEST_TITLE = "listTitle"
    const val LIST_QUEST_STATUS = "listStatus"
}

object TasksInfo: BaseColumns{
    const val TASKS_NAME = "taskName"
    const val TASKS_TITLE = "taskTitle"
    const val TASKS_STATUS = "taskStatus"
}




class DateBaseHelper (context: Context): SQLiteOpenHelper(context, NotesInfo.NOTE_NAME, null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Notes(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${NotesInfo.NOTE_COLUMN_TITLE} TEXT NOT NULL," +
                "${NotesInfo.NOTE_COLUMN_MESSAGE} TEXT NOT NULL)")
        db?.execSQL("CREATE TABLE ListQuest(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${ListQuestInfo.LIST_QUEST_TITLE} TEXT NOT NULL," +
                "${ListQuestInfo.LIST_QUEST_STATUS} TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${NotesInfo.NOTE_NAME}")
        onCreate(db)
    }

}