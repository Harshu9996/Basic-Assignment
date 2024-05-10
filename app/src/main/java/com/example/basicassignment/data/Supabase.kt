package com.example.basicassignment.data

import android.util.Log
import com.example.basicassignment.R
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object Supabase {
    val TAG = "Supabase"

        val supabase = createSupabaseClient(
            supabaseUrl = "https://enzmkamnxmjepndvlryw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVuem1rYW1ueG1qZXBuZHZscnl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ5ODQ0NzgsImV4cCI6MjAzMDU2MDQ3OH0.4lhE_eF_QsEzwbkv2VxgTs-g4q0lVjG4NvmHWoP9r7k"
        ) {
            Log.d(TAG, "initializing Supabase")
            install(Postgrest)
        }

}