package vn.swinburne.assignment2.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import vn.swinburne.assignment2.R

object AppUtils {
    @SuppressLint("RestrictedApi", "ResourceAsColor")
    fun showCustomSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        val customView = LayoutInflater.from(view.context).inflate(R.layout.custom_snackbar, null)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        customView.setBackgroundColor(customView.context.getColor(R.color.secondaryColor)) // Custom background color
        snackbar.setTextColor(Color.WHITE) // Custom text color
        val textView = customView.findViewById<TextView>(R.id.snackbar_text)
        textView.text = message
        textView.textSize = 20f
        // Add Custom View to Snackbar
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }

    fun playSound(context: Context,mode: String) {
        val mediaPlayer = when (mode){
            "failed" ->  MediaPlayer.create(context, R.raw.not_enough_credits)
            "success" -> MediaPlayer.create(context, R.raw.success_book)
            "checked" -> MediaPlayer.create(context, R.raw.choice_sound)
            "unchecked" -> MediaPlayer.create(context, R.raw.uncheck_sound)
            else -> null
        }
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener { it.release() } // Release to free resources
    }
}