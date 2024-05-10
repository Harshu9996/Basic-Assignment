package com.example.basicassignment.domain

import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val id:Int,
    val title:String,
    val description : String,
    val channel: String,
    val likes: Int,
    val dislikes:Int,
    val videoid: String
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
            "${title.first()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
