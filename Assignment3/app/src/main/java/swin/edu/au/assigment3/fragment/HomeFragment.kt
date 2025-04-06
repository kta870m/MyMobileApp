package swin.edu.au.assigment3.fragment

// Import statements for necessary Android and Jetpack components
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import swin.edu.au.assigment3.MainActivity
import swin.edu.au.assigment3.R
import swin.edu.au.assigment3.adapter.NoteAdapter
import swin.edu.au.assigment3.databinding.FragmentHomeBinding
import swin.edu.au.assigment3.model.Note
import swin.edu.au.assigment3.viewmodel.NoteViewModel

// Fragment for displaying the home screen with a list of notes
class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel : NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called after the view is fully created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        setupHomeRecycleView()

        // Navigate to the AddNoteFragment when the FloatingActionButton is clicked
        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    // Update the UI based on whether there are notes or not
    private fun updateUI(note: List<Note>?){
     if(note != null){
         if(note.isNotEmpty()){
             binding.emptyNotesImage.visibility = View.GONE
             binding.homeRecyclerView.visibility = View.VISIBLE
         }else{
             binding.emptyNotesImage.visibility = View.VISIBLE
             binding.homeRecyclerView.visibility = View.GONE
         }
     }
    }

    // Set up the RecyclerView with StaggeredGridLayoutManager and the adapter
    private fun setupHomeRecycleView(){
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            // Observe changes in the note list from the ViewModel and update the RecyclerView
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){ note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    // Perform a search for notes based on the user's query
    private fun searchNote(query: String?){
        val searchQuery = "%$query%"
        notesViewModel.searchNote(searchQuery).observe(this){
            noteAdapter.differ.submitList(it)
        }
    }

    // Handle submission of the search query
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    // Handle changes to the search query
    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!= null){
            searchNote(newText)
        }

        return true
    }

    // Clean up the binding when the fragment is destroyed to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    // Inflate the menu and set up the SearchView
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)
        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    // Handle menu item selections
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}