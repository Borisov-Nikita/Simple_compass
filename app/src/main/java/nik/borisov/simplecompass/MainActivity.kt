package nik.borisov.simplecompass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import nik.borisov.simplecompass.databinding.ActivityMainBinding
import nik.borisov.simplecompass.viewmodel.CompassViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CompassViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.orientation.observe(this) { compassAndLevelValues ->
            binding.compassWithLevelView.onOrientationChanged(
                compassAndLevelValues.biasX,
                compassAndLevelValues.biasY,
                compassAndLevelValues.azimuth,
                getString(compassAndLevelValues.compassDirection),
                compassAndLevelValues.compassDegree
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val isSensorsSupported = viewModel.start()

        //TODO add dialog
        if (!isSensorsSupported) {
            Snackbar.make(
                binding.root,
                "Your device doesn't supported this app.",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Ok") {
                finish()
            }.show()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }
}