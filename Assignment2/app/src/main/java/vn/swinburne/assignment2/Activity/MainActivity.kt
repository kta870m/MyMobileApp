package vn.swinburne.assignment2.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import vn.swinburne.assignment2.Entity.Instrument
import vn.swinburne.assignment2.R

import vn.swinburne.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val instruments = listOf(
        Instrument("Guitar", 4.5f, listOf("Electric", "Acoustic"), 100, R.drawable.guitar),
        Instrument("Piano", 5.0f, listOf("Grand", "Upright"), 300, R.drawable.piano),
        Instrument("Violin", 4.0f, listOf("Classic", "Electric"), 150, R.drawable.violin)
    )
    private var currentIndex = 0
    private var userCredit = 500  // Example user credit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Set custom font for ActionBar title
        val customTitle = TextView(this)
        customTitle.text = "Instrument Rental"
        customTitle.textSize = 20f
        customTitle.setTextColor(resources.getColor(android.R.color.white))

        // Set Lobster font
        val lobsterFont = ResourcesCompat.getFont(this, R.font.lobster)
        customTitle.typeface = lobsterFont

        supportActionBar?.apply {
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customTitle
        }

        // Set background image
        binding.root.setBackgroundResource(R.drawable.studio)

        updateUI()

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % instruments.size
            updateUI()
        }

        // Change the color of the stars in the RatingBar
        val stars = binding.ratingBar.progressDrawable as LayerDrawable
        // Change the filled stars to yellow
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)

        binding.borrowButton.setOnClickListener {
            val instrument = instruments[currentIndex]
            if (userCredit >= instrument.pricePerMonth) {
                val intent = Intent(this, RentActivity::class.java)
                intent.putExtra("instrument_data", instrument)
                intent.putExtra("user_credit", userCredit) // Pass credit to next activity
                startActivityForResult(intent, REQUEST_CODE_RENT)
            } else {
                Snackbar.make(binding.root, "Not enough credits to rent this instrument!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RENT && resultCode == RESULT_OK) {
            userCredit = data?.getIntExtra("updated_credit", userCredit) ?: userCredit
            updateUI()
        }
    }

    private fun updateUI() {
        val instrument = instruments[currentIndex]
        binding.instrumentName.text = instrument.name
        binding.instrumentImage.setImageResource(instrument.imageResId)
        binding.ratingBar.rating = instrument.rating
        binding.itemPrice.text = "Price: ${instrument.pricePerMonth} credits"
        binding.itemAttributes.text = "Attributes: ${instrument.attributes.joinToString(", ")}"
        binding.creditText.text = "Credits: $userCredit"
    }

    companion object {
        private const val REQUEST_CODE_RENT = 1001
    }
}
