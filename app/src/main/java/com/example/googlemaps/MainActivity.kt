package com.example.googlemaps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.googlemaps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.openMapsbtn.setOnClickListener {

            startActivity(Intent(this,MapsActivity::class.java))


        }
    }
}