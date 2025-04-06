package swin.edu.au.assigment3.fragment


// Necessary Android and library imports
import android.app.AlarmManager
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
import com.google.android.material.snackbar.Snackbar
import swin.edu.au.assigment3.MainActivity
import swin.edu.au.assigment3.R
import swin.edu.au.assigment3.databinding.FragmentAddNoteBinding
import swin.edu.au.assigment3.model.Note
import swin.edu.au.assigment3.receiver.ReminderReceiver
import swin.edu.au.assigment3.viewmodel.NoteViewModel
import vn.swinburne.assignment2.common.AppUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Fragment for adding a note with alarm functionality
class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    // Binding object for accessing views
    private var addNoteBinding: FragmentAddNoteBinding? = null
    // Non-nullable version of the binding object
    private val binding get() = addNoteBinding!!

    // ViewModel for notes
    private lateinit var notesViewModel: NoteViewModel
    // Reference to the fragment's view
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        // Set an OnClickListener to the date/time input field to show DatePickerDialog
        binding.addNoteDateTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"

                // After selecting a date, show TimePickerDialog
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    val dateTime = "$selectedDate $formattedTime"
                    binding.addNoteDateTimeInput.setText(dateTime)
                }, hour, minute, true).show()

            }, year, month, day).show()
        }
    }

    private fun saveNote(view: View){
        // Retrieve and trim input values
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val noteDateTimeStr = binding.addNoteDateTimeInput.text.toString().trim()

        // Check if the note title is not empty
        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc, noteDateTimeStr)

            // Additional check for datetime string
            if (noteDateTimeStr.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val targetDate = sdf.parse(noteDateTimeStr)

                targetDate?.let {
                    val now = Calendar.getInstance().time

                    // Check if the selected date and time is in the futur
                    if (it.before(now)) {
                        AppUtils.playSound(requireContext(),"failed")
                        Snackbar.make(addNoteView, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                        return
                    }

                    // Schedule an alarm for the note reminder
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

                    try {
                        alarmManager.setAlarmClock(
                            AlarmManager.AlarmClockInfo(it.time, pendingIntent),
                            pendingIntent
                        )
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        AppUtils.playSound(requireContext(),"failed")
                        Snackbar.make(addNoteView, "Can't schedule exact alarm on this device!", Snackbar.LENGTH_LONG).show()
                    }
                }

                try {
                    val selectedDate = sdf.parse(noteDateTimeStr)
                    val now = Calendar.getInstance().time

                    if (selectedDate.before(now)) {
                        AppUtils.playSound(requireContext(),"failed")
                        Snackbar.make(addNoteView, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                        return
                    }
                } catch (e: Exception) {
                    AppUtils.playSound(requireContext(),"failed")
                    Snackbar.make(addNoteView, "Invalid datetime format!", Snackbar.LENGTH_LONG).show()
                    return
                }
            }
            // Add the note to the ViewModel
            notesViewModel.addNote(note)
            AppUtils.playSound(requireContext(),"success")
            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            AppUtils.playSound(requireContext(),"failed")
            Snackbar.make(addNoteView, "Please enter note title", Snackbar.LENGTH_SHORT).show()
        }
    }

    // Inflate the menu for the add note fragment
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    // Call saveNote when the save menu item is selected
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    // Clean up the binding to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }


}