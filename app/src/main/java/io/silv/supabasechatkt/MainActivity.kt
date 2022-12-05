package io.silv.supabasechatkt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.silv.supabasechatkt.ui.composables.ChatInput
import io.silv.supabasechatkt.ui.composables.ChatMessage
import io.silv.supabasechatkt.ui.theme.SupabaseChatKtTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val viewModel by viewModels<ChatViewModel>()
            val state = viewModel.state

            LaunchedEffect(key1 = true) {
                viewModel.sideEffect.collect { effect ->
                    when (effect) {
                        is ChatSideEffect.Error -> Toast.makeText(
                            this@MainActivity,
                            effect.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            SupabaseChatKtTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .imePadding(), contentAlignment = Alignment.BottomCenter
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Text(text = "all chat")
                            }
                            items(state.messages) {
                                ChatMessage(message = it)
                            }
                        }
                        ChatInput(
                            username = state.username,
                            message = state.userMessage,
                            onMessageChanged = {
                                viewModel.userMessageHandler(it)
                            },
                            onUsernameChanged = {
                                viewModel.usernameTextHandler(it)
                            },
                            sendMessage = {
                                viewModel.sendMessage(
                                    sender = state.username,
                                    content = state.userMessage
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getColorFromInt(it: Int) = remember(it) {
    derivedStateOf {
        when(it) {
            0 -> Color.Gray
            1 -> Color.Blue
            2 -> Color.Green
            3 -> Color.Magenta
            4 -> Color.Cyan
            5 -> Color.Yellow
            else -> Color.Red
        }
    }.value
}
