package io.silv.supabasechatkt.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Uses the supabase-kt Realtime and Postgrest plugins to create a realtime chat service.
 * - suspend fun [subscribeToMessages]
 * - suspend fun [sendMessage]
 */
class ChatService(
    private val client: SupabaseClient = Supabase.client
) {
    // Json config for kotlinx serialization that will be used for content negotiation.
    // By default supabase-kt will use Json.Default.
    private val jsonConfig = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * Connects to realtime client an listens for changes to the messages table.
     * On every Insert [onReceived] will be called with teh received value.
     */
    suspend fun subscribeToMessages(
        onReceived: suspend (message: Message) -> Unit
    ) = withContext(Dispatchers.IO) {
        // First connect to the realtime client.
        // This function will automatically setup a websocket session using ktor and
        // the Supabase instance. It will automatically build the Realtime URL for you.
        client.realtime.connect()
        val channel = client.realtime.createChannel("messageChannel")
        // Using PostgresAction specifies you want to listen to all changes.
        // This can be narrowed down using the sealed class.
        // The default schema for a project is public and can be found in your project on the supabase website.
        val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            // listens to all changes from messages table
            table = "messages"
        }
        channel.join() // join the channel only after calling channel.postgresChangeFlow or broadcastFlow
        // Collect emissions from the flow built above
        changes.collect { action ->
            println(action)
            when (action) {
                is PostgresAction.Insert -> {
                    onReceived(
                        // supabase-kt provides the convenience method decodeRecord for converting JSON responses
                        // to the object representation of the postgres table.
                        action.decodeRecord<Message>(jsonConfig)
                    )
                }
                else -> Unit // ignore other actions
            }
        }
    }

    /**
     * Inserts a message into the messages table. This function will throw exceptions it receives
     * from the ktor client.
     */
    suspend fun sendMessage(
        sender: String,
        content: String,
        color: Int
    ) = withContext(Dispatchers.IO) {
        client.postgrest["messages"] // Using Postgrest plugin specify table to insert into.
            .insert(
                Message(
                    sender = sender.ifBlank { "anon-user" },
                    content = content.ifBlank { "I have no message" },
                    color = color.coerceIn(0..6)
                )
            )
    }
}