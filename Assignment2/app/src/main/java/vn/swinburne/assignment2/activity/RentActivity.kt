package vn.swinburne.assignment2.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import vn.swinburne.assignment2.instrument.Instrument
import vn.swinburne.assignment2.R
import vn.swinburne.assignment2.databinding.ActivityRentBinding

class RentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRentBinding
    private lateinit var instrument: Instrument
    private var userCredit: Int = 0
    private var totalCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instrument = intent.getParcelableExtra("instrument_data")!!
        userCredit = intent.getIntExtra("user_credit", 0)

        var listChip = listOf(
            findViewById<Chip>(R.id.chipExtraStrings),
            findViewById<Chip>(R.id.chipAmplifier),
            findViewById<Chip>(R.id.chipCarryingCase),
        );

        setChipFonts()
        setupChipClickListeners(listChip)

        when(instrument.name) {
            "Guitar" -> binding.root.setBackgroundResource(R.drawable.guitar_rental)
            "Piano" -> binding.root.setBackgroundResource(R.drawable.piano_rental)
            "Violin" -> binding.root.setBackgroundResource(R.drawable.violin_rental)
        }

        totalCost = instrument.pricePerMonth;

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

                showCustomSnackbar(binding.root, "Successfully booked ${instrument.name}!")
                finish()
            } else {
                showCustomSnackbar(binding.root, "Not enough credits!")
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
        if (binding.chipExtraStrings.isChecked) selectedAccessories.add(binding.chipExtraStrings.text.toString())
        if (binding.chipCarryingCase.isChecked) selectedAccessories.add(binding.chipCarryingCase.text.toString())
        if (binding.chipAmplifier.isChecked) selectedAccessories.add(binding.chipAmplifier.text.toString())
        return selectedAccessories.joinToString(", ")
    }


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

                } else {
                    totalCost
                    message = "Removed $name: -$price credits. Total: $totalCost credits"
                    listChip[i].chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.secondaryColor))
                }
                showCustomSnackbar(binding.root, message)
            }

            binding.chipGroupAccessories.addView(listChip[i])
        }

    }

    private fun setChipFonts() {
        val lobsterFont: Typeface? = ResourcesCompat.getFont(this, R.font.lobster)
        binding.chipExtraStrings.typeface = lobsterFont
        binding.chipCarryingCase.typeface = lobsterFont
        binding.chipAmplifier.typeface = lobsterFont
    }

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
}

