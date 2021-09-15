package com.umbrella.nasaapiapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umbrella.nasaapiapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NASAApiApp)
        setContentView(R.layout.activity_main)
    }
}