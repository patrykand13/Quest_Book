package pl.momlok.questbook.database

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_view_list_quest.view.*
import pl.momlok.questbook.objects.Quest
import pl.momlok.questbook.activity.QuestActivity
import pl.momlok.questbook.R

class CardViewAdapterListQuests (val context: Context, val db: SQLiteDatabase, var quests: ArrayList<Quest>): RecyclerView.Adapter<MyViewHolderListQuest>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderListQuest {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cardView_quest = layoutInflater.inflate(R.layout.card_view_list_quest, parent, false)
        return MyViewHolderListQuest(cardView_quest)
    }

    override fun onBindViewHolder(holder: MyViewHolderListQuest, position: Int) {
        //------------ELEMENT POJEDYNCZEJ NOTATKI--------
        val listTitle = holder.view.titleCardViewListQuest
        val listStatus = holder.view.statusCardViewListQuest
        val cardView_quest = holder.view.cardViewListQuest
        val context: Context = holder.view.context

        //---------------------------------
        listTitle.setText(quests[holder.adapterPosition].listTitle)
        listStatus.setText(quests[holder.adapterPosition].listStatus)

        cardView_quest.setOnClickListener {
            var intent_edit = Intent(context, QuestActivity::class.java)

            val listTitle_edit = quests[holder.adapterPosition].listTitle
            val listId_edit = quests[holder.adapterPosition].id.toString()

            intent_edit.putExtra("questEdit", listTitle_edit)
            intent_edit.putExtra("idEdit", listId_edit)
            context.startActivity(intent_edit)

        }

        cardView_quest.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                //----------USUWANIE Z BAZY----
                db.delete(ListQuestInfo.LIST_QUEST_NAME, BaseColumns._ID + "=?",
                        arrayOf(quests[holder.adapterPosition].id.toString()))
                //----------USUWANIE Z WIDOKU---
                quests.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                //-----------------------------
                return true
            }
        })


    }

    override fun getItemCount(): Int {
        val cursor = db.query(ListQuestInfo.LIST_QUEST_NAME, null,
            null, null,
            null,null,null)
        val liczbaWierszy = cursor.count
        cursor.close()
        return liczbaWierszy
    }

}

class MyViewHolderListQuest (val view: View):RecyclerView.ViewHolder(view)