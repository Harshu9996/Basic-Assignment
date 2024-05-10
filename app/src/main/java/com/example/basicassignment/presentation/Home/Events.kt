package com.example.basicassignment.presentation.Home

sealed class Events {
    data class search(val title: String) : Events()
    // Accommodate more events
}