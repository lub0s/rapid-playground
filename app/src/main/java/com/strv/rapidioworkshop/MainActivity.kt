package com.strv.rapidioworkshop

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import com.strv.rapidioworkshop.databinding.ActivityMainBinding
import com.strv.rapidioworkshop.vmbk.vmb

interface MainView {

}

class MainActivity : AppCompatActivity(), MainView {
    val vmb = vmb<MainViewModel, ActivityMainBinding>(R.layout.activity_main)
}

class MainViewModel : ViewModel() {

}
