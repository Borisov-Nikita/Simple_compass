package nik.borisov.simplecompass.viewmodel

import androidx.annotation.StringRes

data class CompassAndLevelValues(

    val biasX: Float,
    val biasY: Float,
    val azimuth: Float,
    @StringRes val compassDirection: Int,
    val compassDegree: String
)
