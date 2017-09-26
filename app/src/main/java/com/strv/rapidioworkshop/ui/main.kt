package com.strv.rapidioworkshop.ui

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.strv.rapidioworkshop.R
import com.strv.rapidioworkshop.databinding.ActivityMainBinding
import com.strv.rapidioworkshop.utils.vmb

interface MainView {

}

class MainActivity : AppCompatActivity(), MainView {
    val vmb = vmb<MainViewModel, ActivityMainBinding>(R.layout.activity_main)
    private lateinit var drawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDrawer()
    }

    private fun setupDrawer() {
        setSupportActionBar(vmb.binding.toolbar)
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_menu)
        DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.text))
        supportActionBar?.setHomeAsUpIndicator(icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle = ActionBarDrawerToggle(this, vmb.binding.drawerLayout, R.string.open, R.string.close)
        vmb.binding.drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}

class MainViewModel : ViewModel() {

}
