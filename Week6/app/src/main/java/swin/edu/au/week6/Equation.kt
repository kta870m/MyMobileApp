package swin.edu.au.week6

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equation(val factor1: Int, val factor2: Int) : Parcelable