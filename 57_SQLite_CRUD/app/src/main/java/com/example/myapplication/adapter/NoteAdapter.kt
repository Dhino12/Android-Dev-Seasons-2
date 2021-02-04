package com.example.myapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.NoteAddUpdateActivity
import com.example.myapplication.CustomOnItemClickListener
import com.example.myapplication.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*


//Seperti ulasan materi sebelumnya, kelas adapter ini berfungsi untuk menampilkan data per baris
// di komponen viewgroup seperti RecyclerView dengan data yang berasal dari objek linkedlist bernama listNotes.
// Anda melakukan proses inflate layout yang dibuat sebelumnya untuk menjadi tampilan per baris di RecyclerView.
// Termasuk juga di dalamnya implementasi dari CustomOnItemClickListener yang membuat
// objek CardViewNote bisa diklik untuk mengarahkan ke halaman NoteAddUpdateActivity.
// Tujuannya, untuk melakukan perubahan data oleh pengguna.

class NoteAdapter(private val activity: Activity):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    //====================================================================================
    // generate getter untuk arraylist-nya dan juga constructor untuk context-nya.
    // Context di sini dibutuhkan karena kita akan memanggil fungsi startActivityForResultketika item diklik.
    var listNotes = ArrayList<Note>()
        set(listNote) {
            if (listNotes.size > 0){
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNote)
            notifyDataSetChanged()
        }
    //====================================================================================

    //====================================================================================
    // 3 metode untuk menambahkan, memperbaharui dan menghapus Item di RecyclerView.
    //====================================================================================
    fun addItem(note: Note){
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }
    fun updateItem(position: Int, note: Note){
        this.listNotes[position] = note
        notifyItemChanged(position,note)
    }
    fun removeItem(position: Int){
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,this.listNotes.size)
    }
    //====================================================================================

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note){
            with(itemView){
                tv_item_title.text = note.title
                tv_item_date.text = note.date
                tv_item_description.text = note.description
                cv_item_note.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View?, position: Int) {
                                val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
                                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                                activity.startActivityForResult(
                                    intent,
                                    NoteAddUpdateActivity.REQUEST_UPDATE
                                )
                            }
                        })
                )
            }
        }
    }
}