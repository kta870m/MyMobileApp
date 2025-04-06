package vn.swinburne.assignment2.common


import android.content.Context
import android.media.MediaPlayer
import swin.edu.au.assigment3.R


object AppUtils {
    fun playSound(context: Context,mode: String) {
        val mediaPlayer = when (mode){
            "delete" ->  MediaPlayer.create(context, R.raw.delete)
            "success" -> MediaPlayer.create(context, R.raw.success)
            "failed" -> MediaPlayer.create(context, R.raw.failed)
            else -> null
        }
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener { it.release() } // Release to free resources
    }
}