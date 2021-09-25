package com.example.dictionaryapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dictionaryapplication.R
import com.example.dictionaryapplication.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        callBack()
    }
    private fun setup() {
    }
    private fun callBack() {

    }


}