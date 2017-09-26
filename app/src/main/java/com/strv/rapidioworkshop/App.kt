package com.strv.rapidioworkshop

import android.app.Application
import com.strv.rapidioworkshop.utils.PrefDelegate


class App : Application() {
    init {
        PrefDelegate.initialize(this)
    }
}