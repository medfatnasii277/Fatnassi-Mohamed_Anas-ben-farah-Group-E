package com.example.productprojectandroid.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.productprojectandroid.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listeners
        binding.btnViewProducts.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        // Handle language switching
        binding.btnSwitchLanguage.setOnClickListener {
            val currentLocale = resources.configuration.locale
            val newLocale = if (currentLocale.language == "en") {
                Locale("fr") // Set language to French
            } else {
                Locale("en") // Set language to English
            }
            setLocale(newLocale)
            recreate() // Recreate the activity to apply changes
        }
    }

    private fun setLocale(locale: Locale) {
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        // Save locale in shared preferences to persist the language setting
        val prefs = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        prefs.edit().putString("language", locale.language).apply()
    }
}
