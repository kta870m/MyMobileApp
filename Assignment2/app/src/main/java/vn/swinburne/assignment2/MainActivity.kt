package vn.swinburne.assignment2

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize

import vn.swinburne.assignment2.databinding.ActivityMainBinding

@Parcelize
data class Instrument(
    val name: String,
    val rating: Float,
    val attributes: List<String>,
    val pricePerMonth: Int,
    val imageResId: Int
) : Parcelable

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

        supportActionBar?.title = "Instrument Rental"
        // Set background image
        binding.root.setBackgroundResource(R.drawable.studio)

        updateUI()

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % instruments.size
            updateUI()
        }

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
