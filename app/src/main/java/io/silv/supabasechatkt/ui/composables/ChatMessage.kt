package io.silv.supabasechatkt.ui.composables

import io.silv.supabasechatkt.data.Message
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.silv.supabasechatkt.getColorFromInt

@Composable
fun ChatMessage(message: Message) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .animateContentSize()
            .clickable { expanded = !expanded }
            .padding(start = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message.sender,
                color = getColorFromInt(message.color)
            )
            Text(text = message.content)
        }
        if (expanded)
            Text(text = message.time ?: "")
    }
}