package com.example.basicassignment.data.supabaseDb

import com.example.basicassignment.data.Supabase
import com.example.basicassignment.domain.VideoItem
import io.github.jan.supabase.postgrest.from

class SupabaseDB : SupabaseDAO {
    override suspend fun getAllVideos(): List<VideoItem> {
        return Supabase.supabase.from("Videos")
            .select().decodeList<VideoItem>()
    }
}