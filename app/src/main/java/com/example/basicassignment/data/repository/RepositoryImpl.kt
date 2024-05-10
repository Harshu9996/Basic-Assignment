package com.example.basicassignment.data.repository

import com.example.basicassignment.data.supabaseDb.SupabaseDAO
import com.example.basicassignment.domain.VideoItem
import com.example.basicassignment.domain.repository

class repositoryImpl(
    private val DAO: SupabaseDAO
) : repository {
    override suspend fun getAllVideos(): List<VideoItem> {
        return  DAO.getAllVideos()
    }
}