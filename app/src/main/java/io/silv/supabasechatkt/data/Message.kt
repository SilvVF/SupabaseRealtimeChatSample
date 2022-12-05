package io.silv.supabasechatkt.data

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val sender: String,
    val content: String,
    val color: Int,
    val time: String? = null,
)