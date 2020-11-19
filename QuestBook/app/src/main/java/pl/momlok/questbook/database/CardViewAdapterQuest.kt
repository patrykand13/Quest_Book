package pl.momlok.questbook.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_view_quest.view.*
import pl.momlok.questbook.R
import pl.momlok.questbook.objects.Task

class CardViewAdapterQuest (val context: Context, val db: SQLiteDatabase, var tasks: ArrayList<Task>, var tab: String): RecyclerView.Adapter<MyViewHolderQuest>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderQuest {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cardView_task = layoutInflater.inflate(R.layout.card_view_quest, parent, false)
        return MyViewHolderQuest(cardView_task)
    }

    override fun onBindViewHolder(holder: MyViewHolderQuest, position: Int) {
        //------------ELEMENT POJEDYNCZEJ NOTATKI--------
        val cardView_task = holder.view.cardViewTask
        val taskTitle = holder.view.titleCardViewQuest
        val listStatus = holder.view.buttonCardViewQuest

        //---------------------------------

        taskTitle.text = tasks[holder.adapterPosition].taskTitle
        if (tasks[holder.adapterPosition].taskStatus =="false"){
            listStatus.setText("Do zrobienia")
        }else if(tasks[holder.adapterPosition].taskStatus.equals("true")) {
            listStatus.setText("Zrobione")
        }
        listStatus.setOnClickListener {
            val value = ContentValues()
            value.put(TasksInfo.TASKS_TITLE,tasks[holder.adapterPosition].taskTitle)
            if (tasks[holder.adapterPosition].taskStatus == "false"){
                value.put(TasksInfo.TASKS_STATUS,"true")
            }else if(tasks[holder.adapterPosition].taskStatus.equals("true")) {
                value.put(TasksInfo.TASKS_STATUS,"false")
            }
            db.update(
                    tab, value, BaseColumns._ID + "=?",
                    arrayOf(tasks[holder.adapterPosition].id.toString()))
            Toast.makeText(context,"Zmieniono",Toast.LENGTH_SHORT).show()
        }

        cardView_task.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                //----------USUWANIE Z BAZY----
                db.delete(tab, BaseColumns._ID + "=?",
                        arrayOf(tasks[holder.adapterPosition].id.toString()))
                //----------USUWANIE Z WIDOKU---
                tasks.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                //-----------------------------
                return true
            }
        })


    }

    override fun getItemCount(): Int {
        val cursor = db.query(tab, null,
                null, null,
                null,null,null)
        val liczbaWierszy = cursor.count
        cursor.close()
        return liczbaWierszy
    }

}

class MyViewHolderQuest (val view: View): RecyclerView.ViewHolder(view)