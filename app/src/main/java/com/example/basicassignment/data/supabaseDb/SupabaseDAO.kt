package com.example.basicassignment.data.supabaseDb

import com.example.basicassignment.domain.VideoItem

interface SupabaseDAO {
    suspend fun getAllVideos() : List<VideoItem>
}