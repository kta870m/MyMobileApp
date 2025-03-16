package vn.swinburne.assignment2.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import vn.swinburne.assignment2.instrument.Instrument
import vn.swinburne.assignment2.R
import vn.swinburne.assignment2.common.AppUtils
import vn.swinburne.assignment2.databinding.ActivityRentBinding

class RentActivity : AppCompatActivity() {
    // View Binding for activity_rent.xml
    private lateinit var binding: ActivityRentBinding

    // Selected instrument passed from MainActivity
    private lateinit var instrument: Instrument

    // EditText for user description input
    private lateinit var txtDescription : EditText

    // User's available credits
    private var userCredit: Int = 0

    // Total cost including instrument and selected accessories
    private var totalCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve instrument data and user credit from the Intent
        instrument = intent.getParcelableExtra("instrument_data")!!
        userCredit = intent.getIntExtra("user_credit", 0)

        txtDescription = findViewById(R.id.editDescription)

        // List of pre-defined Chips in XML
        var listChip = listOf(
            findViewById<Chip>(R.id.chipExtraStrings),
            findViewById<Chip>(R.id.chipAmplifier),
            findViewById<Chip>(R.id.chipCarryingCase),
        );

        // Apply Lobster font to chips
        setChipFonts()

        // Set listeners for chips
        setupChipClickListeners(listChip)

        // Change background based on selected instrument
        when(instrument.name) {
            "Guitar" -> binding.root.setBackgroundResource(R.drawable.guitar_rental)
            "Piano" -> binding.root.setBackgroundResource(R.drawable.piano_rental)
            "Violin" -> binding.root.setBackgroundResource(R.drawable.violin_rental)
        }

        totalCost = instrument.pricePerMonth; // Initialize total cost with instrument price

        // Customize ActionBar title
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

        // Display instrument details on the screen
        binding.itemName.text = instrument.name
        binding.itemImage.setImageResource(instrument.imageResId)
        binding.itemPrice.text = "Price: ${instrument.pricePerMonth.toString()} credits";
        binding.itemAttributes.text = "Attributes: ${instrument.attributes.joinToString(", ")}"

        // Save button logic
        binding.saveButton.setOnClickListener {
            var totalCost = instrument.pricePerMonth

            // Require description before booking
            if (txtDescription.text.isNullOrBlank()) {
                AppUtils.showCustomSnackbar(binding.root, "Please enter a description before proceeding!")
                AppUtils.playSound(this, "failed")
                return@setOnClickListener
            }

            // Check selected chips and add cost
            if (binding.chipExtraStrings.isChecked) totalCost += instrument.accessories.values.toList()[0]
            if (binding.chipCarryingCase.isChecked) totalCost += instrument.accessories.values.toList()[2]
            if (binding.chipAmplifier.isChecked) totalCost += instrument.accessories.values.toList()[1]

            // If enough credits -> book success
            if (userCredit >= totalCost) {
                userCredit -= totalCost
                val resultIntent = Intent()
                resultIntent.putExtra("updated_credit", userCredit)
                resultIntent.putExtra("rented_instrument_name", instrument.name)
                resultIntent.putExtra("selected_accessories", getSelectedAccessories()) // Send selected accessories
                setResult(Activity.RESULT_OK, resultIntent)

                AppUtils.showCustomSnackbar(binding.root, "Successfully booked ${instrument.name}!")
                AppUtils.playSound(this,"success")
                finish()
            } else {
                AppUtils.showCustomSnackbar(binding.root, "Not enough credits!")
                AppUtils.playSound(this,"failed")
            }
        }

        // Cancel button logic
        binding.cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }


    }

    // Collect selected accessories and return as a comma-separated string
    private fun getSelectedAccessories(): String {
        val selectedAccessories = mutableListOf<String>()
        if (binding.chipExtraStrings.isChecked) selectedAccessories.add(binding.chipExtraStrings.text.toString())
        if (binding.chipCarryingCase.isChecked) selectedAccessories.add(binding.chipCarryingCase.text.toString())
        if (binding.chipAmplifier.isChecked) selectedAccessories.add(binding.chipAmplifier.text.toString())
        return selectedAccessories.joinToString(", ")
    }


    // Dynamically bind data to chips & handle chip click events
    private fun setupChipClickListeners(listChip : List<Chip>) {
        binding.chipGroupAccessories.removeAllViews()
        for (i in 0 until listChip.count()) {
            val name = instrument.accessories.keys.toTypedArray()[i];
            val price = instrument.accessories.values.toTypedArray()[i];
            listChip[i].text = "$name ($price credits)";


            listChip[i].setOnCheckedChangeListener { _, isChecked ->
                var message: String;
              if (isChecked) {
                    totalCost += price;
                    message = "Added $: +$price credits. Total: $totalCost credits"
                    listChip[i].chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.primaryColor))
                    AppUtils.playSound(this, "checked")
                } else {
                    totalCost -= price
                    message = "Removed $name: -$price credits. Total: $totalCost credits"
                    listChip[i].chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.secondaryColor))
                    AppUtils.playSound(this,"unchecked")
                }
                AppUtils.showCustomSnackbar(binding.root, message)
            }

            binding.chipGroupAccessories.addView(listChip[i])
        }

    }

    // Apply Lobster font to predefined chips
    private fun setChipFonts() {
        val lobsterFont: Typeface? = ResourcesCompat.getFont(this, R.font.lobster)
        binding.chipExtraStrings.typeface = lobsterFont
        binding.chipCarryingCase.typeface = lobsterFont
        binding.chipAmplifier.typeface = lobsterFont
    }

}

