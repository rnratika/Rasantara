package com.example.rasantara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.rasantara.ui.main.HomeFragment
import com.example.rasantara.ui.explore.ExploreFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView =
            findViewById(R.id.bottom_navigation)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
                        .commit()
                    true
                }

                R.id.nav_explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, ExploreFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}