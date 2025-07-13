package com.example.implicitintent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    private lateinit var number1Input: EditText
    private lateinit var number2Input: EditText
    private lateinit var calculateButton: Button
    private lateinit var shareResultButton: Button
    private lateinit var dialSupportButton: Button
    private lateinit var viewGoogleButton: Button
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        number1Input = findViewById(R.id.number1_input)
        number2Input = findViewById(R.id.number2_input)
        calculateButton = findViewById(R.id.calculate_button)
        shareResultButton = findViewById(R.id.share_result_button)
        dialSupportButton = findViewById(R.id.dial_support_button)
        viewGoogleButton = findViewById(R.id.view_google_button)
        resultText = findViewById(R.id.result_text)

        // Restore saved inputs (e.g., after rotation)
        savedInstanceState?.let {
            number1Input.setText(it.getString("number1"))
            number2Input.setText(it.getString("number2"))
            resultText.text = it.getString("result")
        }

        // Calculate button: Perform addition
        calculateButton.setOnClickListener {
            val num1 = number1Input.text.toString().toDoubleOrNull() ?: 0.0
            val num2 = number2Input.text.toString().toDoubleOrNull() ?: 0.0
            val result = num1 + num2
            resultText.text = "Result: $result"
        }

        // Share Result button: Implicit Intent to share text
        shareResultButton.setOnClickListener {
            val result = resultText.text.toString()
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, result)
                putExtra(Intent.EXTRA_SUBJECT, "Calculation Result")
            }
            // Use chooser to let user select app
            startActivity(Intent.createChooser(shareIntent, "Share Result"))
        }

        // Dial Support button: Implicit Intent to dial a number
        dialSupportButton.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:9840987714".toUri()
            }
            startActivity(dialIntent)
        }

        viewGoogleButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://www.google.com".toUri()
            startActivity(intent)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save inputs and result for configuration changes
        outState.putString("number1", number1Input.text.toString())
        outState.putString("number2", number2Input.text.toString())
        outState.putString("result", resultText.text.toString())
    }

}
