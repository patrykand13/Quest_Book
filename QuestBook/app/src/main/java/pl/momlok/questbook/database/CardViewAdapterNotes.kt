package pl.momlok.questbook.database

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_view_note.view.*
import pl.momlok.questbook.objects.Note
import pl.momlok.questbook.activity.NotesActivity
import pl.momlok.questbook.R

class CardViewAdapterNotes (val context: Context, val db: SQLiteDatabase, var notes: ArrayList<Note>): RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cardView_note = layoutInflater.inflate(R.layout.card_view_note, parent, false)
        return MyViewHolder(cardView_note)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //------------ELEMENT POJEDYNCZEJ NOTATKI--------
        val cardView_note = holder.view.cardViewNote
        val title = holder.view.titleCardViewNote
        val message = holder.view.messageCardViewNote
        val context: Context = holder.view.context

        //---------------------------------

        title.setText(notes[holder.adapterPosition].title)
        message.setText(notes[holder.adapterPosition].message)

        cardView_note.setOnClickListener {
            var intent_edit = Intent(context, NotesActivity::class.java)

            val title_edit = notes[holder.adapterPosition].title
            val message_edit = notes[holder.adapterPosition].message
            val id_edit = notes[holder.adapterPosition].id.toString()

            intent_edit.putExtra("title", title_edit)
            intent_edit.putExtra("message", message_edit)
            intent_edit.putExtra("ID", id_edit)
            context.startActivity(intent_edit)
        }

        cardView_note.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                //-------KOPIOWANIE DO SCHOWKA---------
                /*val copy_info = Toast.makeText(context,"Skopiowano",Toast.LENGTH_SHORT)
                val cm = context.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
                val clipdata = ClipData.newPlainText("CopyText",
                "Title: " + title.text + "\n" + "Message: " + message.text)
                cm.setPrimaryClip(clipdata)
                copy_info.show()
                */
                //-----------------------------
                //----------USUWANIE Z BAZY----
                db.delete(NotesInfo.NOTE_NAME, BaseColumns._ID + "=?",
                        arrayOf(notes[holder.adapterPosition].id.toString()))
                //----------USUWANIE Z WIDOKU---
                notes.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                //-----------------------------
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        val cursor = db.query(NotesInfo.NOTE_NAME, null,
            null, null,
            null,null,null)
        val liczbaWierszy = cursor.count
        cursor.close()
        return liczbaWierszy
    }

}

class MyViewHolder (val view: View):RecyclerView.ViewHolder(view)