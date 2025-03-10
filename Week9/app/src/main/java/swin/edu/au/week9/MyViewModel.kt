package swin.edu.au.week9

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    // LiveData for image resource ID
    var image = MutableLiveData<Int>()

    init {
        // Setting initial image resource
        image.value = R.drawable.ic_assignment_turned_in_24px
    }

    // Function to change the image
    fun nextImage() {
        image.value = R.drawable.assignment_ind// new image resource ID
    }
}