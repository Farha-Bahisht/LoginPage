package com.example.loginpage

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class DashboardActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var connectionStatus: TextView
    private lateinit var intensityText: TextView
    private lateinit var scanButton: Button
    private lateinit var vibrationSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize TTS
        tts = TextToSpeech(this, this)

        // UI elements
        connectionStatus = findViewById(R.id.connectionStatus)
        intensityText = findViewById(R.id.currentIntensity)
        scanButton = findViewById(R.id.scanButton)
        vibrationSeekBar = findViewById(R.id.vibrationSeekBar)

        // Set up SeekBar listener
        vibrationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val message = "Vibration intensity set to $progress"
                intensityText.text = "Current Intensity: $progress"
                speak(message)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Scan button click
        scanButton.setOnClickListener {
            speak("Scanning for device...")
            Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show()
            connectionStatus.text = "Scanning..."
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                speak("Welcome to your dashboard. Device not connected.")
            } else {
                Toast.makeText(this, "TTS language not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun speak(text: String) {
        if (::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
