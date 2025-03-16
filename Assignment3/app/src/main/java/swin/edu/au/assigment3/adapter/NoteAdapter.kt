package swin.edu.au.assigment3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import swin.edu.au.assigment3.databinding.NoteLayoutBinding
import swin.edu.au.assigment3.fragment.HomeFragmentDirections
import swin.edu.au.assigment3.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id==newItem.id &&
                    oldItem.noteDesc==newItem.noteDesc &&
                    oldItem.noteTitle==newItem.noteTitle &&
                    oldItem.noteDateTime==newItem.noteDateTime
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDesc
        holder.itemBinding.noteDateTime.text = currentNote.noteDateTime

        // Nếu noteDateTime không rỗng thì hiển thị, ngược lại ẩn đi
        if (currentNote.noteDateTime.isNotEmpty()) {
            holder.itemBinding.noteDateTime.visibility = View.VISIBLE
            holder.itemBinding.noteDateTime.text = currentNote.noteDateTime
        } else {
            holder.itemBinding.noteDateTime.visibility = View.GONE
        }

        holder.itemView.setOnClickListener{
           val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }

}