package vn.swinburne.assignment2.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import vn.swinburne.assignment2.instrument.Instrument
import vn.swinburne.assignment2.R
import vn.swinburne.assignment2.common.AppUtils
import vn.swinburne.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val instruments = listOf(
        Instrument(
            "Guitar", 4.5f, listOf("Electric", "Acoustic"), 100, R.drawable.guitar,
            mapOf(
                "Extra Strings" to 20,
                "Guitar Strap" to 15,
                "Capo" to 10
            )
        ),
        Instrument(
            "Piano", 5.0f, listOf("Grand", "Upright"), 300, R.drawable.piano,
            mapOf(
                "Keyboard Stand" to 40,
                "Piano Bench" to 50,
                "Dust Cover" to 20
            )
        ),
        Instrument(
            "Violin", 4.0f, listOf("Classic", "Electric"), 150, R.drawable.violin,
            mapOf(
                "Extra Bow" to 25,
                "Violin Shoulder Rest" to 30,
                "Rosin" to 10
            )
        )
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
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)

        binding.borrowButton.setOnClickListener {
            val instrument = instruments[currentIndex]
            if (userCredit >= instrument.pricePerMonth) {
                val intent = Intent(this, RentActivity::class.java)
                intent.putExtra("instrument_data", instrument)
                intent.putExtra("user_credit", userCredit)
                startActivityForResult(intent, REQUEST_CODE_RENT)
            } else {
                AppUtils.showCustomSnackbar(binding.root, "Not enough credits to rent this instrument!")
                AppUtils.playSound(this, "failed")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RENT && resultCode == RESULT_OK) {
            userCredit = data?.getIntExtra("updated_credit", userCredit) ?: userCredit
            val rentedInstrumentName = data?.getStringExtra("rented_instrument_name") ?: "Instrument"
            val selectedAccessories = data?.getStringExtra("selected_accessories") ?: "No accessories"
            updateUI()

            if(selectedAccessories.isNotEmpty()){
                AppUtils.showCustomSnackbar(binding.root, "Successfully booked $rentedInstrumentName with accessories: $selectedAccessories!")
            }else{
                AppUtils.showCustomSnackbar(binding.root, "Successfully booked $rentedInstrumentName")
            }
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


        if(userCredit == 0){
            binding.creditText.setTextColor(Color.RED);
        }else{
            binding.creditText.setTextColor(Color.WHITE);
        }
    }

    companion object {
        private const val REQUEST_CODE_RENT = 1001
    }

}
