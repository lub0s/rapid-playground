package com.strv.rapidioworkshop.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.strv.rapidioworkshop.BR
import com.strv.rapidioworkshop.R
import com.strv.rapidioworkshop.data.Channel
import com.strv.rapidioworkshop.databinding.ActivityMainBinding
import com.strv.rapidioworkshop.utils.LifecycleReceiver
import com.strv.rapidioworkshop.utils.SingleLiveData
import com.strv.rapidioworkshop.utils.vmb
import io.rapid.Rapid
import io.rapid.RapidDocument
import io.rapid.lifecycle.RapidLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding

interface MainView {
    val channelItemBinding: ItemBinding<RapidDocument<Channel>>
}

interface ChannelClick {
    fun channelClick(channelId: String)
}

class MainActivity : AppCompatActivity(), MainView, ChannelClick {

    val vmb = vmb<MainViewModel, ActivityMainBinding>(R.layout.activity_main)
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override val channelItemBinding =
        ItemBinding.of<RapidDocument<Channel>>(BR.channel, R.layout.item_channel)
            .bindExtra(BR.listener, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDrawer()

        vmb.viewModel.displayMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun channelClick(channelId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChannelFragment(channelId))
            .commit()

        vmb.binding.drawerLayout.closeDrawers()
        supportActionBar?.title = "#$channelId"
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

class MainViewModel : ViewModel(), LifecycleReceiver {

    val displayMessage = SingleLiveData<String>()

    val collection get() = Rapid.getInstance().collection("channels", Channel::class.java)

    val channels = ObservableField<List<RapidDocument<Channel>>>(emptyList())
    val newChannelName = ObservableField<String>("")

    override fun onLifecycleReady(lifecycleOwner: LifecycleOwner) {
        RapidLiveData.from(collection).observe(lifecycleOwner, Observer {
            channels.set(it)
        })
    }

    fun newChannelClick() {
        collection.document(newChannelName.get())
            .mutate(Channel())
            .onSuccess {
                displayMessage.value = "Yeah!"
                newChannelName.set("")
            }
            .onError { displayMessage.value = it.message ?: "Error adding" }
    }

}
