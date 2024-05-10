package com.example.basicassignment.presentation.Home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicassignment.domain.repository
import com.example.basicassignment.domain.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SuspiciousIndentation")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: repository,
    private val application: Application
)  : AndroidViewModel(application) {
    val TAG = "HomeViewModel"


    private lateinit var videosHolder : List<VideoItem>

    private val _Videos = MutableStateFlow<List<VideoItem>>(listOf())
    val Videos: StateFlow<List<VideoItem>> = _Videos.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()



    init {
        viewModelScope.launch {
            videosHolder = repository.getAllVideos()
          _Videos.value = videosHolder
            Log.d(TAG, "videos = : "+_Videos.value)

        }
    }


    fun onEvent(event:Events){
        when(event){
            is Events.search->{
                _searchText.value = event.title
                viewModelScope.launch {
                    Log.d(TAG, "onEvent: title from event = "+event.title)
                    if(event.title.isNullOrBlank() || event.title.isNullOrEmpty()){
                       _Videos.value = videosHolder
                    }else{

                        _Videos.value = _Videos.value.filter {
                            it.doesMatchSearchQuery(event.title)
                        }
                    }

                }
            }
            else ->{
                Log.d(TAG, "onEvent: No matching event")
            }
        }

    }

}