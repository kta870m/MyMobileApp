package swin.edu.au.assigment3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import swin.edu.au.assigment3.model.Note
import swin.edu.au.assigment3.repository.NoteRepository

class NoteViewModel(app: Application, private val noteRepository: NoteRepository) : AndroidViewModel(app) {
    fun addNote(note: Note) =
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }

    fun deleteNote(note: Note) =
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }

    fun updateNote(note: Note) =
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }

    fun getAllNotes() = noteRepository.getAllNotes()
    fun searchNote(query: String?) =
        noteRepository.searchNote(query)
}