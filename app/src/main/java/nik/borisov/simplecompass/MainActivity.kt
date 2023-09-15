package nik.borisov.simplecompass

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import nik.borisov.simplecompass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val compass by lazy {
        SampleCompass(
            this
        ) { azimuth ->
            runOnUiThread {
                rotateLimb(azimuth)
            }
        }
    }

    private var currentAzimuth = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        compass.startCompass()
    }

    override fun onPause() {
        super.onPause()
        compass.stopCompass()
    }

    private fun rotateLimb(azimuth: Float) {
        val animation = RotateAnimation(
            -currentAzimuth,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).also {
            it.duration = 500
            it.repeatCount = 0
            it.fillAfter = true
        }
        currentAzimuth = azimuth

        binding.compassLimbImageView.startAnimation(animation)
    }
}