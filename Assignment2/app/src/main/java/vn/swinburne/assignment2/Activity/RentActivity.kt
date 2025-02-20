package vn.swinburne.assignment2.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.swinburne.assignment2.Entity.Instrument
import vn.swinburne.assignment2.R
import vn.swinburne.assignment2.databinding.ActivityRentBinding

class RentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRentBinding
    private lateinit var instrument: Instrument
    private var userCredit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instrument = intent.getParcelableExtra("instrument_data")!!
        userCredit = intent.getIntExtra("user_credit", 0)

        when(instrument.name) {
            "Guitar" -> binding.root.setBackgroundResource(R.drawable.guitar_rental)
            "Piano" -> binding.root.setBackgroundResource(R.drawable.piano_rental)
            "Violin" -> binding.root.setBackgroundResource(R.drawable.violin_rental)
        }

        supportActionBar?.title = "Confirm Booking"

        instrument = intent.getParcelableExtra("instrument_data")!!
        userCredit = intent.getIntExtra("user_credit", 0)

        binding.itemName.text = instrument.name
        binding.itemImage.setImageResource(instrument.imageResId)
        binding.itemPrice.text = "Price: ${instrument.pricePerMonth.toString()} credits";
        binding.itemAttributes.text = "Attributes: ${instrument.attributes.joinToString(", ")}"



        binding.saveButton.setOnClickListener {
            if (userCredit >= instrument.pricePerMonth) {
                userCredit -= instrument.pricePerMonth
                val resultIntent = Intent()
                resultIntent.putExtra("updated_credit", userCredit)
                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, "Successfully booked ${instrument.name}!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Not enough credits!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}

