package com.example.fortnite_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fortnite_tracker.databinding.StartActivityBinding


class StartActivity : AppCompatActivity() {
    private lateinit var binding: StartActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = StartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("StartActivity", "onCreate")
        val button = binding.buttonNext
        button.setOnClickListener {
            // Przekierowanie do SecondActivity
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

}
