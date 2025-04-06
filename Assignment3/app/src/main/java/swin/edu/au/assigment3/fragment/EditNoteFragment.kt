package swin.edu.au.assigment3.fragment

// Importing necessary Android classes and libraries
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import swin.edu.au.assigment3.receiver.ReminderReceiver
import swin.edu.au.assigment3.viewmodel.NoteViewModel
import vn.swinburne.assignment2.common.AppUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Fragment class for editing an existing note
class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    // Initializes the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        // Initialize the fields with current note's data
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)
        binding.editNoteDateTimeInput.setText(currentNote.noteDateTime)

        // Set up DateTime picker when the datetime input is clicked
        binding.editNoteDateTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Pre-populate DatePicker with the current note's datetime if it exists
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

            // Display DatePickerDialog and TimePickerDialog to select new datetime
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Sau khi chọn ngày --> mở TimePicker
                TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    binding.editNoteDateTimeInput.setText(sdf.format(calendar.time))
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Set up FloatingActionButton for saving the edited note
        binding.editNoteFab.setOnClickListener {
            // Extract and trim input values
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()
            val noteDateTimeStr = binding.editNoteDateTimeInput.text.toString().trim()

            // Validate the note title
            if (noteTitle.isNotEmpty()) {
                cancelExistingAlarm(currentNote)

                // Update the note with new details
                val note = Note(currentNote.id, noteTitle, noteDesc, noteDateTimeStr)
                notesViewModel.updateNote(note)

                // Schedule a new alarm if datetime is set
                if (noteDateTimeStr.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val targetDate = sdf.parse(noteDateTimeStr)

                    targetDate?.let {
                        val now = Calendar.getInstance().time

                        // Ensure selected datetime is in the future
                        if (it.before(now)) {
                            Snackbar.make(binding.root, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                            AppUtils.playSound(requireContext(),"failed")
                            return@setOnClickListener
                        }

                        // Setup new alarm for this note
                        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
                            putExtra("title", noteTitle)
                            putExtra("desc", noteDesc)
                        }

                        val pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            note.hashCode(),
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        // Attempt to set the alarm
                        try {
                            alarmManager.setAlarmClock(
                                AlarmManager.AlarmClockInfo(it.time, pendingIntent),
                                pendingIntent
                            )
                        } catch (e: SecurityException) {
                            e.printStackTrace()
                            AppUtils.playSound(requireContext(),"failed")
                            Snackbar.make(binding.root, "Can't schedule exact alarm on this device!", Snackbar.LENGTH_LONG).show()
                        }
                    }

                    try {
                        val selectedDate = sdf.parse(noteDateTimeStr)
                        val now = Calendar.getInstance().time

                        if (selectedDate.before(now)) {
                            Snackbar.make(binding.root, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                            AppUtils.playSound(requireContext(),"failed")
                            return@setOnClickListener
                        }
                    } catch (e: Exception) {
                        Snackbar.make(binding.root, "Invalid datetime format!", Snackbar.LENGTH_LONG).show()
                        AppUtils.playSound(requireContext(),"failed")
                        return@setOnClickListener
                    }
                }
                view.findNavController().popBackStack(R.id.homeFragment, false)

            } else {
                Snackbar.make(binding.root, "Please enter note title", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    // Handles deletion of a note
    private fun deleteNote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete"){_,_ ->
                cancelExistingAlarm(currentNote)
                notesViewModel.deleteNote(currentNote)
                AppUtils.playSound(requireContext(),"success")
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    // Creates the options menu
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    // Handles option menu item selections
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            }else -> false
        }
    }

    // Cancel any existing alarms for the note
    private fun cancelExistingAlarm(note: Note) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            note.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }


    // Clean up the binding when the fragment is destroyed to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }

}