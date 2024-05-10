package com.example.basicassignment.domain

import com.example.basicassignment.domain.VideoItem

interface repository {
    suspend fun getAllVideos() : List<VideoItem>
}