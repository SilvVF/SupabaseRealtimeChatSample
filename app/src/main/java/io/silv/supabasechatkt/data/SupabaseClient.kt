package io.silv.supabasechatkt.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.silv.supabasechatkt.BuildConfig

object Supabase {

    // use the key and url provided by supabase for you project
    // click settings on the left - project settings -> API
    private const val KEY = BuildConfig.SUPABASE_KEY
    private const val URL = BuildConfig.SUPABASE_URL

    val client = createSupabaseClient(
        supabaseUrl = URL,
        supabaseKey = KEY
    ) {
        install(Realtime) {
            // enter any config options here
        }
        install(Postgrest) {
            // enter any config options here
        }
    }
}