# SupabaseRealtimeChatSample

This is a simple chat app made using the supabase-kt client. https://github.com/supabase-community

![image](https://user-images.githubusercontent.com/98186105/205529386-4342f5ac-b137-49ee-b6ad-5553f3b3c7f4.png)

## Supabase Postgres table
The Postgres database contains one table which is the messages table. All chat messages in the app are going to be broadcast to an all chat of users that observe the changes to this table.

Each row in the table represents a single message sent by a user the Model Message will be used in the app as a Kotlin data class. The Postgres table messages in the image represents what the table looks like in supabse table editor. Supabase has default implementations for triggers on inserts. In this project the timestamp and UUID genereate default ones are used.

# Android App

This app uses the supabase-kt client. https://github.com/supabase-community

To start first add the dependencies to the app level build.gradle file - in a single module project this will say build.gradle(:app).
Each individual one contains the core plugin / supabase client but each feature on the community tab has a dependency.

 - These will also need to be added to the supabse client on creation in the creatSupabaseClient(url, key) { install(Postgrest) } 

implementation("io.github.jan-tennert.supabase:gotrue-kt:$supabase") // Auth
implementation("io.github.jan-tennert.supabase:realtime-kt:$supabase") // Realtime
implementation("io.github.jan-tennert.supabase:postgrest-kt:$supabase") // postgrest
implementation("io.github.jan-tennert.supabase:storage-kt:VERSION") // Storage
implementation("io.github.jan-tennert.supabase:functions-kt:VERSION") // Functions
![carbon (2)](https://user-images.githubusercontent.com/98186105/205532004-718efd8d-0076-4a59-83fa-6ff0136f7e23.png)


After that make sure to add the ktor client and kotlinxserialization https://github.com/Kotlin/kotlinx.serialization .
add ktor client engine (if you don't already have one, see https://ktor.io/docs/http-client-engines.html for all engines)
e.g. the CIO engine

id("org.jetbrains.kotlin.plugin.serialization")
  
implementation("io.ktor:ktor-client-cio:2.1.3")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
 
![carbon (1)](https://user-images.githubusercontent.com/98186105/205531738-b82fb5f0-e5e3-4949-8e4a-ad1779837e90.png)


## Inside the app

The app is built usingg ComposeUI and Kotlin.

### data package

Inside the data package is the ChatService class, Message data class, and the SupabaseClient.kt file

The Message data class represents a single row in the postgres database and will be used to deserialize responses when receiving incoming chat messages.
This is done using kotlinxSerialization Json.

The ChatService class contains the methods for sending and subcribing to chat messages. This is all done using the Supabase realtime feature that
allows realtime updates on any changes to the database. When an insert is made this will receive an update from the SupabaseClient which can be reacted to in the app. In this case it is displaying the update as a chat message.

### ui package

![Screenshot_20221204-213652 (1)](https://user-images.githubusercontent.com/98186105/205538597-57e3aaf4-9c52-44f1-95d3-56b41e9b78be.png)
![Screenshot_20221204-213605](https://user-images.githubusercontent.com/98186105/205538726-52539609-dcd5-4d33-9f94-c8f42ab5a0c5.png)

This package contains all of the compose UI compnoents for displaying the chat messages.




### root package - supabasechatkt

This contains the ChatViewModel which is used to persist the chat messages for the UI on things like configuration changes and also gives access to ViewModelScope to make lifecylce aware calls to the data layer functions. It also contains the MainActivity it is using a single activity and only one screen.
