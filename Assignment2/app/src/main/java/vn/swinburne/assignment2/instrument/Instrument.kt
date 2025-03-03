package vn.swinburne.assignment2.instrument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instrument(
    val name: String,
    val rating: Float,
    val attributes: List<String>,
    val pricePerMonth: Int,
    val imageResId: Int,
    val accessories: Map<String, Int>
) : Parcelable
