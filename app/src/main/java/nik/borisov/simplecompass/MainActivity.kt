package nik.borisov.simplecompass

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import nik.borisov.simplecompass.databinding.ActivityMainBinding
import nik.borisov.simplecompass.viewmodel.CompassViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CompassViewModel::class.java]
    }

    private fun observeViewModel() {
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

    private fun showUnsupportedMessage() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.oops))
            .setMessage(getString(R.string.unsupported))
            .setPositiveButton(getString(R.string.close_app)) { _, _ ->
                finish()
            }
            .create()
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        val isSensorsSupported = viewModel.start()

        if (!isSensorsSupported) {
            showUnsupportedMessage()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }
}