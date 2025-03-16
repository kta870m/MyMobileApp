package swin.edu.au.assigment3.fragment

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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
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

        // DateTime Picker ở đây:
        binding.addNoteDateTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Date Picker
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"

                // Time Picker
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
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val noteDateTimeStr = binding.addNoteDateTimeInput.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc, noteDateTimeStr)
            notesViewModel.addNote(note)

            if (noteDateTimeStr.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val targetDate = sdf.parse(noteDateTimeStr)

                targetDate?.let {
                    val now = Calendar.getInstance().time

                    if (it.before(now)) {
                        Snackbar.make(addNoteView, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                        return
                    }

                    // Đặt Alarm sau khi xác nhận là đúng giờ
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
                        Snackbar.make(addNoteView, "Can't schedule exact alarm on this device!", Snackbar.LENGTH_LONG).show()
                    }
                }

                try {
                    val selectedDate = sdf.parse(noteDateTimeStr)
                    val now = Calendar.getInstance().time

                    if (selectedDate.before(now)) {
                        Snackbar.make(addNoteView, "Datetime must be present or future", Snackbar.LENGTH_LONG).show()
                        return
                    }
                } catch (e: Exception) {
                    Snackbar.make(addNoteView, "Invalid datetime format!", Snackbar.LENGTH_LONG).show()
                    return
                }
            }
            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Snackbar.make(addNoteView, "Please enter note title", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }


}