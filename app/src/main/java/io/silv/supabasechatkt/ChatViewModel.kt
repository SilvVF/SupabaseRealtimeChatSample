package io.silv.supabasechatkt

import io.silv.supabasechatkt.data.Message
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.silv.supabasechatkt.data.ChatService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChatViewModel(
    private val chatService: ChatService = ChatService()
): ViewModel() {

    private val color: Int = Random(kotlinx.datetime.Clock.System.now().nanosecondsOfSecond).nextInt(from = 0, until = 7)

    var state by mutableStateOf(ChatState())
        private set

    private val _sideEffect = Channel<ChatSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            runCatching {
                // Subscribe to the chat flow that will call the function passed to it
                // with new messages received from postgres inserts.
                chatService.subscribeToMessages { message ->
                    state = state.copy(
                        messages = listOf(message) + state.messages
                    )
                }
            }.onFailure {
                _sideEffect.send(ChatSideEffect.Error(it.message ?: "error receiving message"))
            }
        }
    }

    fun sendMessage(sender: String, content: String) {
        viewModelScope.launch {
            runCatching {// Insert a message into the postgres db.
                chatService.sendMessage(sender, content, color)
            }.onFailure { // supabase-kt will re-throw exceptions thrown by ktor. Such as when you have no internet
                _sideEffect.send(ChatSideEffect.Error(it.message ?: "error sending message"))
            }
        }
    }
    fun userMessageHandler(content: String) {
        viewModelScope.launch {
            state = state.copy(
                userMessage = content
            )
        }
    }

    fun usernameTextHandler(username: String) {
        viewModelScope.launch {
            state = state.copy(
                username = username
            )
        }
    }
}

sealed class ChatSideEffect {
    data class Error(val message: String): ChatSideEffect()
}

@Immutable
@Stable
data class ChatState(
    val username: String = "",
    val userMessage: String = "",
    val messages: List<Message> = emptyList()
)