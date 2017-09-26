package com.strv.rapidioworkshop.ui

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strv.rapidioworkshop.BR
import com.strv.rapidioworkshop.R
import com.strv.rapidioworkshop.data.Message
import com.strv.rapidioworkshop.databinding.FragmentChannelBinding
import com.strv.rapidioworkshop.utils.vmb
import io.rapid.Rapid
import io.rapid.RapidDocument
import io.rapid.RapidMutateOptions
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList


interface ChannelView {
    val messageItemBinding: ItemBinding<RapidDocument<Message>>
}


class ChannelFragment : Fragment(), ChannelView {

    companion object {
        operator fun invoke(channelId: String) = ChannelFragment().apply {
            arguments = Bundle().apply {
                putString("channel-extra", channelId)
            }
        }
    }

    val vmb = vmb<ChannelViewModel, FragmentChannelBinding>(R.layout.fragment_channel) {
        ChannelViewModel(arguments.getString("channel-extra"))
    }

    override val messageItemBinding: ItemBinding<RapidDocument<Message>>
        get() = ItemBinding.of<RapidDocument<Message>>(BR.message, R.layout.item_message)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return vmb.rootView
    }
}

class ChannelViewModel(val channelId: String): ViewModel() {

    private val collection = Rapid.getInstance().collection("messages", Message::class.java)

    val newMessageText = ObservableField<String>("")

    val messages = DiffObservableList<RapidDocument<Message>>(object : DiffObservableList.Callback<RapidDocument<Message>> {
        override fun areItemsTheSame(oldItem: RapidDocument<Message>?, newItem: RapidDocument<Message>?): Boolean {
            return oldItem == newItem ?: false
        }
        override fun areContentsTheSame(oldItem: RapidDocument<Message>, newItem: RapidDocument<Message>): Boolean {
            return newItem.hasSameContentAs(oldItem)
        }
    })


    init {
        collection.equalTo("channelId", channelId)
            .subscribe {
                messages.update(it)
            }
    }

    fun sendMessage() {
        collection.newDocument().mutate(
            Message(newMessageText.get(), channelId, "sdf", -1L),
            RapidMutateOptions.Builder().fillPropertyWithServerTimestamp("timestamp").build()
        )

        newMessageText.set("")
    }
}