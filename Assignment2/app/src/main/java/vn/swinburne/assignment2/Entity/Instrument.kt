package vn.swinburne.assignment2.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instrument(
    val name: String,
    val rating: Float,
    val attributes: List<String>,
    val pricePerMonth: Int,
    val imageResId: Int
) : Parcelable
