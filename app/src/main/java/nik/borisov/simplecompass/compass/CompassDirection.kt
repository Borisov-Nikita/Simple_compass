package nik.borisov.simplecompass.compass

import androidx.annotation.StringRes
import nik.borisov.simplecompass.R

enum class CompassDirection(
    @StringRes val direction: Int,
) {
    NORTH(R.string.north),
    SOUTH(R.string.south),
    EAST(R.string.east),
    WEST(R.string.west),
    NORTHEAST(R.string.northeast),
    NORTHWEST(R.string.northwest),
    SOUTHEAST(R.string.southeast),
    SOUTHWEST(R.string.southwest),
}