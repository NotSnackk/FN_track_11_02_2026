package com.example.fortnite_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.fortnite_tracker.databinding.SearchActivityBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: SearchActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemePreferences.applyTheme(this)
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SearchActivity", "onCreate")

        binding.themeToggle.isChecked = ThemePreferences.isDarkTheme(this)
        binding.themeToggle.setOnCheckedChangeListener { _, isChecked ->
            ThemePreferences.setDarkTheme(this, isChecked)
        }

        binding.searchButton.setOnClickListener {
            submitQuery()
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                submitQuery()
                true
            } else {
                false
            }
        }
    }

    private fun submitQuery() {
        val query = binding.searchInput.text?.toString()?.trim().orEmpty()
        if (query.isBlank()) {
            binding.searchInput.error = "Wpisz nick gracza"
            return
        }

        val intent = Intent(this@SearchActivity, MainActivity::class.java)
        intent.putExtra("SEARCH_QUERY", query)
        startActivity(intent)
        finish()
    }
}
