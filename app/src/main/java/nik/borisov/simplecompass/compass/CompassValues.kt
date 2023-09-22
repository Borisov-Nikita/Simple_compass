package nik.borisov.simplecompass.compass

import androidx.annotation.StringRes

data class CompassValues(

    val azimuth: Float,
    @StringRes
    val direction: Int,
    val degree: String
)
