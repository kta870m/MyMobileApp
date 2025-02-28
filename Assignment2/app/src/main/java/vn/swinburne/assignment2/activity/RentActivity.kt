package vn.swinburne.assignment2.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import vn.swinburne.assignment2.instrument.Instrument
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

        setChipFonts()
        setupChipClickListeners()

        when(instrument.name) {
            "Guitar" -> binding.root.setBackgroundResource(R.drawable.guitar_rental)
            "Piano" -> binding.root.setBackgroundResource(R.drawable.piano_rental)
            "Violin" -> binding.root.setBackgroundResource(R.drawable.violin_rental)
        }

        //Set custom font for ActionBar title
        val customTitle = TextView(this)
        customTitle.text = "Confirm Booking"
        customTitle.textSize = 20f
        customTitle.setTextColor(resources.getColor(android.R.color.white))

        // Set Lobster font
        val lobsterFont = ResourcesCompat.getFont(this, R.font.lobster)
        customTitle.typeface = lobsterFont

        supportActionBar?.apply {
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customTitle
        }

        instrument = intent.getParcelableExtra("instrument_data")!!
        userCredit = intent.getIntExtra("user_credit", 0)

        binding.itemName.text = instrument.name
        binding.itemImage.setImageResource(instrument.imageResId)
        binding.itemPrice.text = "Price: ${instrument.pricePerMonth.toString()} credits";
        binding.itemAttributes.text = "Attributes: ${instrument.attributes.joinToString(", ")}"



        binding.saveButton.setOnClickListener {
            var totalCost = instrument.pricePerMonth

            // Check selected chips and add cost
            if (binding.chipExtraStrings.isChecked) totalCost += 20
            if (binding.chipCarryingCase.isChecked) totalCost += 30
            if (binding.chipAmplifier.isChecked) totalCost += 50

            if (userCredit >= totalCost) {
                userCredit -= totalCost
                val resultIntent = Intent()
                resultIntent.putExtra("updated_credit", userCredit)
                resultIntent.putExtra("rented_instrument_name", instrument.name)
                resultIntent.putExtra("selected_accessories", getSelectedAccessories()) // Send selected accessories
                setResult(Activity.RESULT_OK, resultIntent)

                Snackbar.make(binding.root, "Successfully booked ${instrument.name}!", Snackbar.LENGTH_SHORT).show()
                finish()
            } else {
                Snackbar.make(binding.root, "Not enough credits!", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }


    }

    // Function to get selected accessories as a string
    private fun getSelectedAccessories(): String {
        val selectedAccessories = mutableListOf<String>()
        if (binding.chipExtraStrings.isChecked) selectedAccessories.add("Extra Strings")
        if (binding.chipCarryingCase.isChecked) selectedAccessories.add("Carrying Case")
        if (binding.chipAmplifier.isChecked) selectedAccessories.add("Amplifier")
        return selectedAccessories.joinToString(", ")
    }

    private fun setupChipClickListeners() {
        val accessoryPrices = mapOf(
            binding.chipExtraStrings to 20,
            binding.chipCarryingCase to 30,
            binding.chipAmplifier to 50
        )

        var totalCost = instrument.pricePerMonth // Bắt đầu với giá nhạc cụ

        for ((chip, price) in accessoryPrices) {
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    totalCost += price
                    chip.setChipBackgroundColorResource(R.color.primaryColor) // Đổi màu khi chọn
                    Snackbar.make(binding.root, "Added ${chip.text}: +$price credits. Total cost: $totalCost credits", Snackbar.LENGTH_SHORT).show()
                } else {
                    totalCost -= price
                    chip.setChipBackgroundColorResource(R.color.secondaryColor) // Trả lại màu khi bỏ chọn
                    Snackbar.make(binding.root, "Removed ${chip.text}: -$price credits. Total cost: $totalCost credits", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setChipFonts() {
        val lobsterFont: Typeface? = ResourcesCompat.getFont(this, R.font.lobster)

        binding.chipExtraStrings.typeface = lobsterFont
        binding.chipCarryingCase.typeface = lobsterFont
        binding.chipAmplifier.typeface = lobsterFont
    }
}

