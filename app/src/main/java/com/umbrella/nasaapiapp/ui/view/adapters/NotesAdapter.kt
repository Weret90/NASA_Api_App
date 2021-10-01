package com.umbrella.nasaapiapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.model.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesVH>() {

    private val notes = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesVH(view)
    }

    override fun onBindViewHolder(holder: NotesVH, position: Int) {
        holder.bind(notes[position])
    }

    fun addNote(note: Note) {
        notes.add(note)
        notifyItemInserted(notes.lastIndex)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NotesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) {
            itemView.findViewById<TextView>(R.id.noteTitle).text = note.title
            itemView.findViewById<TextView>(R.id.noteDescription).text = note.description
            itemView.setOnClickListener {
                notes.remove(note)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}
