package swin.edu.au.assigment3.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import swin.edu.au.assigment3.MainActivity
import swin.edu.au.assigment3.R
import swin.edu.au.assigment3.databinding.FragmentEditNoteBinding
import swin.edu.au.assigment3.model.Note
import swin.edu.au.assigment3.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)
        binding.editNoteDateTimeInput.setText(currentNote.noteDateTime)

        binding.editNoteDateTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Nếu đã có ngày cũ --> set sẵn
            val parts = currentNote.noteDateTime.split(" ")
            if (parts.size == 2) {
                val dateParts = parts[0].split("/")
                val timeParts = parts[1].split(":")
                if (dateParts.size == 3 && timeParts.size == 2) {
                    calendar.set(
                        dateParts[2].toInt(), // year
                        dateParts[1].toInt() - 1, // month
                        dateParts[0].toInt(), // day
                        timeParts[0].toInt(), // hour
                        timeParts[1].toInt() // minute
                    )
                }
            }

            // Mở DatePicker
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Sau khi chọn ngày --> mở TimePicker
                TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    binding.editNoteDateTimeInput.setText(sdf.format(calendar.time))
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        binding.editNoteFab.setOnClickListener {
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()
            val noteDateTimeStr = binding.editNoteDateTimeInput.text.toString().trim()

            if (noteTitle.isNotEmpty()) {
                if (noteDateTimeStr.isNotEmpty()) {
                    // Nếu có nhập DateTime thì validate
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    try {
                        val selectedDate = sdf.parse(noteDateTimeStr)
                        val now = Calendar.getInstance().time

                        if (selectedDate.before(now)) {
                            Snackbar.make(binding.root, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                            return@setOnClickListener
                        }
                    } catch (e: Exception) {
                        Snackbar.make(binding.root, "Invalid datetime format!", Snackbar.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                }

                // Trường hợp datetime trống hoặc hợp lệ
                val note = Note(currentNote.id, noteTitle, noteDesc, noteDateTimeStr)
                notesViewModel.updateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment, false)

            } else {
                Snackbar.make(binding.root, "Please enter note title", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete"){_,_ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            }else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }

}