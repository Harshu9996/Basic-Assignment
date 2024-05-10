package com.example.basicassignment.presentation.Home

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.example.basicassignment.R
import com.example.basicassignment.domain.VideoItem
import com.example.basicassignment.presentation.Home.Events
import com.example.basicassignment.presentation.Home.HomeViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val videos = homeViewModel.Videos.collectAsState()
    val searchText = homeViewModel.searchText.collectAsState()



    Home(videos = videos.value, searchText = searchText.value) { event->
        homeViewModel.onEvent(event)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(modifier: Modifier = Modifier,videos:List<VideoItem>,searchText:String, onEvent:(event:Events)->Unit) {

    val TAG = "Home"

    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val scope = rememberCoroutineScope()
    var currentVideoId by remember{
        mutableStateOf("")
    }
    var isSheetVisible by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(bottomSheetState) {
        snapshotFlow { bottomSheetState.isVisible }.collect { isVisible ->

                Log.d(TAG, "Home: isSheetVisible = $isVisible")
               isSheetVisible = isVisible
        }
    }




    BottomSheetScaffold(
        sheetContent = {
        BottomSheetUI(isSheetVisible = isSheetVisible, currentVideoId = currentVideoId, videos = videos, lifecycleOwner = LocalLifecycleOwner.current ){videoId->
            currentVideoId = videoId
        }
    },
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            TopAppBar(title = { Text(text = "BASIC Assignment", color = MaterialTheme.colorScheme.onPrimary)}, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary))
            
        }

        ) {paddingValues->
        LazyColumn(modifier.padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally){
            
            item{
                TextField(value = searchText,modifier = Modifier.padding(bottom = 8.dp, top = 16.dp), onValueChange = {it->
                        onEvent(Events.search(it))
                }, placeholder = {
                    Text(text = "Search")
                })
            }
            items(videos, key = {videoItem->
                videoItem.id

            }){videoItem->
                VideoTile(title = videoItem.title, description = videoItem.description, likes = videoItem.likes, dislikes = videoItem.dislikes, channel = videoItem.channel, videoId = videoItem.videoid){
                    scope.launch {
                        currentVideoId = videoItem.videoid

                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }

            }
        }
    }
}

@Composable
fun VideoTile(title:String,description:String,likes:Int,dislikes: Int,channel:String,videoId: String, onCLick:()->Unit) {
    val TAG = "Home"
    var isDescriptionOpen = remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier
        .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
        .clickable {
            onCLick()
        }){
        Column(modifier = Modifier.animateContentSize()){
            val imageurl = "https://img.youtube.com/vi/$videoId/0.jpg"
            Log.d(TAG, "VideoTile: imageUrl = "+imageurl)
            AsyncImage(
                model = imageurl,
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier
                    .height(250.dp),
                contentScale = ContentScale.FillBounds
            )


            Text(
                text = title,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
                ,style = MaterialTheme.typography.titleMedium
            )

            Row(modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = channel,
                    modifier = Modifier
                        .wrapContentSize(),
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(onClick = {
                    isDescriptionOpen.value = !isDescriptionOpen.value
                }) {
                    if(!isDescriptionOpen.value){
                        Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24), contentDescription = "Open Description Box")

                    }else{
                        Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24), contentDescription = "Close Description Box")

                    }

                }

            }


            if(isDescriptionOpen.value){
                Text(text = description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp))
            }


            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
                var likeState by remember{
                    mutableStateOf(false)
                }

                var dislikeState by remember{
                    mutableStateOf(false)
                }
                IconButton(onClick = { likeState = !likeState }, modifier = Modifier.padding(end = 1.dp)) {
                    if (!likeState){
                        Icon(painter = painterResource(id = R.drawable.baseline_thumb_up_off_alt_24), contentDescription = "Likes")

                    }else{
                        Icon(painter = painterResource(id = R.drawable.baseline_thumb_up_alt_24), contentDescription = "Likes")

                    }

                }

                Text(text = likes.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 2.dp))

                IconButton(onClick = { dislikeState = !dislikeState }, modifier = Modifier.padding(end = 1.dp)) {
                    if (!dislikeState){
                        Icon(painter = painterResource(id = R.drawable.baseline_thumb_down_off_alt_24), contentDescription = "Likes")

                    }else{
                        Icon(painter = painterResource(id = R.drawable.baseline_thumb_down_alt_24), contentDescription = "Likes")

                    }

                }
                Text(text = dislikes.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 2.dp))

            }





        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetUI(isSheetVisible: Boolean, currentVideoId:String,modifier: Modifier = Modifier,videos:List<VideoItem>, lifecycleOwner: LifecycleOwner,onCLick: (String) -> Unit) {

    val TAG = "Home"
    if(currentVideoId.isNotEmpty()){

      Column {
          val currVideo = videos.find { it.videoid == currentVideoId }
          YouTubePlayer(isSheetVisible = isSheetVisible,title = currVideo!!.title, description = currVideo.description,
              channel = currVideo.channel,
              likes = currVideo.likes, dislikes = currVideo.dislikes,youtubeVideoId = currentVideoId, lifecycleOwner = lifecycleOwner)


          LazyColumn{

              items(videos){videoItem->
                  VideoTile(title = videoItem.title, description = videoItem.description, likes = videoItem.likes, dislikes = videoItem.dislikes ,channel = videoItem.channel , videoId = videoItem.videoid ) {
                     onCLick(videoItem.videoid)
                  }
              }
          }
      }
    }

}

@Composable
fun YouTubePlayer(
    youtubeVideoId:String,
    title:String,
    description: String,
    channel: String,
    likes: Int,
    dislikes: Int,
    isSheetVisible:Boolean,
    lifecycleOwner: LifecycleOwner
) {
    val TAG = "Home"
    val isDescriptionOpen = remember {
        mutableStateOf(false)
    }
    Log.d(TAG, "YouTubePlayer: isSheetVisible = "+isSheetVisible)
    Column(modifier = Modifier.animateContentSize()) {

        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),factory = {context->

            YouTubePlayerView(context=context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId,0f)
                    }

                })
            }
        },
            update = {it->
                it.getYouTubePlayerWhenReady(object: YouTubePlayerCallback{
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        Log.d(TAG, "onYouTubePlayer: update function invoked")
                        youTubePlayer.loadVideo(youtubeVideoId,0f)
                        if(!isSheetVisible){
                            youTubePlayer.pause()
                        }
                    }

                })

            }
        )

        Text(
            text = title,
            modifier = Modifier
                .wrapContentSize()
                .padding(4.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Row(modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = channel,
                modifier = Modifier
                    .wrapContentSize(),
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = {
                isDescriptionOpen.value = !isDescriptionOpen.value
            }) {
                if(!isDescriptionOpen.value){
                    Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24), contentDescription = "Open Description Box")

                }else{
                    Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24), contentDescription = "Close Description Box")

                }

            }

        }

        if(isDescriptionOpen.value){
            Text(text = description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp))
        }


        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            var likeState by remember{
                mutableStateOf(false)
            }

            var dislikeState by remember{
                mutableStateOf(false)
            }
           IconButton(onClick = { likeState = !likeState }, modifier = Modifier.padding(end = 1.dp)) {
               if (!likeState){
                    Icon(painter = painterResource(id = R.drawable.baseline_thumb_up_off_alt_24), contentDescription = "Likes")

               }else{
                   Icon(painter = painterResource(id = R.drawable.baseline_thumb_up_alt_24), contentDescription = "Likes")

               }

           }

           Text(text = likes.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 2.dp))

            IconButton(onClick = { dislikeState = !dislikeState }, modifier = Modifier.padding(end = 1.dp)) {
                if (!dislikeState){
                    Icon(painter = painterResource(id = R.drawable.baseline_thumb_down_off_alt_24), contentDescription = "Likes")

                }else{
                    Icon(painter = painterResource(id = R.drawable.baseline_thumb_down_alt_24), contentDescription = "Likes")

                }

            }
            Text(text = dislikes.toString(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 2.dp))

        }




    }
}