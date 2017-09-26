package com.strv.rapidioworkshop.data



data class User(
    val displayName: String
)


data class Channel(
    val lastMessageId: String = ""
)


data class Message(
    val text: String,
    val channelId: String,
    val senderDM: String,
    val timestamp: Long
)
