package io.silv.supabasechatkt.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatInput(
    username: String,
    message: String,
    onMessageChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    sendMessage: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.9f)),
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChanged,
                placeholder = {
                    Text(text = "enter a username",color = Color.LightGray)
                },
                label = {
                    Text(text = "username")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { onUsernameChanged("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            )
        }
        item {
            OutlinedTextField(
                value = message,
                onValueChange = onMessageChanged,
                placeholder = {
                    Text(text = "enter a message",color = Color.LightGray)
                },
                label = {
                    Text(text = "message")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { onMessageChanged("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            )
        }
        item {
            Button(
                onClick = sendMessage,
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "send message")
            }
        }
    }
}