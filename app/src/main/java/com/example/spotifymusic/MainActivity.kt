package com.example.spotifymusic

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.spotifymusic.dto.response.SpotifyTrackDetailsResponse
import com.example.spotifymusic.dto.response.SpotifyTrendingSongsResponse
import com.example.spotifymusic.ui.theme.SpotifyMusicTheme
import com.example.spotifymusic.viewModels.SpotifyViewModel
import com.locon.core.data.PhoneCodesUtil
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    private var spotifyViewModel: SpotifyViewModel = SpotifyViewModel()
    private var mediaPlayer:MediaPlayer= MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyMusicTheme() {
//                startActivity(Intent(applicationContext,AnimatedHomeScreenActivity::class.java))
                MainContentSpotify(spotifyViewModel,mediaPlayer)
            }
        }
    }
}

@Composable
fun MainContentSpotify(spotifyViewModel: SpotifyViewModel, mediaPlayer: MediaPlayer) {

    var navController= rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Spash.route){
        composable(Screens.Spash.route){
            SplashScreen(navController,spotifyViewModel)
        }
        composable(Screens.Home.route){
            HomeScreen(spotifyViewModel,navController)
        }
        composable(Screens.SongDisplay.route, arguments = listOf(
            navArgument("index"){
                type= NavType.IntType
                defaultValue=0
            },
            navArgument("trackId"){
                type= NavType.StringType
                defaultValue="46hzNOUOAlivuCZZ0wE3zi"
            },
            navArgument("songCategory"){
                type= NavType.StringType
                defaultValue=SongCategory.TRENDING_LIST.name
            }
        )
        ){
            var index=it.arguments?.getInt("index")
            var trackId=it.arguments?.getString("trackId")
            var songCategory=it.arguments?.getString("songCategory")
            Log.d("song category","${songCategory}")
            SecondScreenSong(spotifyViewModel,index,trackId,songCategory,navController,mediaPlayer)
        }
        composable(Screens.PlayList.route){
            PlaylistScreen(spotifyViewModel = spotifyViewModel, navController = navController,mediaPlayer)
        }
    }

}
@Composable
fun SplashScreen(navController: NavHostController, spotifyViewModel: SpotifyViewModel){
    var startAnimation by remember { mutableStateOf(false) }


    val alphaAnimation= animateFloatAsState(
        targetValue = if(startAnimation) 1f else 0f,
    animationSpec = tween(
        durationMillis = 3000,
        easing = EaseInCubic
    ))
    LaunchedEffect(key1 = true, block ={
        startAnimation=true
        delay(4000)
        navController.navigate(Screens.Home.route)
    } )
    Splash(alphaAnimation)

}
@Composable
fun Splash(alphaAnimation: State<Float>) {
    Box(modifier= Modifier
        .background(Color.Blue.copy(0.5f))
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Icon(
            modifier = Modifier
                .size(120.dp)
                .alpha(alphaAnimation.value),
            painter = painterResource(id = R.drawable.baseline_music_note_24),
            contentDescription = "music-icon",
            tint=Color.White
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(spotifyViewModel: SpotifyViewModel,navController: NavController) {

    var searchClicked by remember { mutableStateOf(false) }
    spotifyViewModel.getSpotifySongs()

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(10.dp),
        contentAlignment = Alignment.TopCenter,){
        Column() {

            Row(horizontalArrangement = Arrangement.Center,modifier= Modifier
                .fillMaxWidth()
                .clickable { spotifyViewModel.showCountryDropDown = true }) {
                Box(Modifier.clickable { spotifyViewModel.showCountryDropDown=true }) {
                    TextField(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .border(width = 1.dp, color = Color.DarkGray, shape = RectangleShape),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color.Green,
                            containerColor = Color.Black
                        ),
                        value = spotifyViewModel.selectedCountry.name,
                        onValueChange={},
                        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_location_24),
                                contentDescription = "Icon",
                                tint = Color.White,
                                modifier=Modifier.clickable { spotifyViewModel.showCountryDropDown=true }
                            )
                        }
                    )
                }

                Icon(
                    modifier = Modifier
                        .padding(vertical = 25.dp, horizontal = 30.dp)
                        .clickable {
                            spotifyViewModel.getSpotifySongs()
                            searchClicked = true
                        }
                        .size(30.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "searchIcon",
                    tint = Color.White
                )
            }
            if(spotifyViewModel.showCountryDropDown){
                countryPicker(context = LocalContext.current,spotifyViewModel)
            }


            Log.d("songs", spotifyViewModel.spotifySongsList.toString())

            if(spotifyViewModel.spotifySongsList.isNotEmpty()) {


                searchClicked = false
                Box(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.background(
                            color = Color.Black, shape = RoundedCornerShape(16.dp)
                        )
                    ) {
                        itemsIndexed(spotifyViewModel.spotifySongsList){
                                index, item ->
                            SongDisplay(index, item, spotifyViewModel, navController)

                        }
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), contentAlignment = Alignment.BottomEnd) {
                        IconButton(
                            onClick = { navController.navigate("playlists") },
                            modifier= Modifier
                                .clip(CircleShape)
                                .border(1.dp, color = Color.White, shape = CircleShape)
                                .size(60.dp)
                                .background(Color.Black.copy(0.3f))){
                            Icon(
                                painterResource(id = R.drawable.baseline_playlist_play_24),
                                contentDescription = "playlist",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
            else {

                if (searchClicked) {
                    Column(
                        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = Color.White,
                            strokeWidth = 4.dp
                        )
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp), contentAlignment = Alignment.BottomEnd) {
                IconButton(
                    onClick = { navController.navigate("playlists") },
                    modifier= Modifier
                        .clip(CircleShape)
                        .border(1.dp, color = Color.White, shape = CircleShape)
                        .size(60.dp)
                        .background(Color.Black.copy(0.3f))){
                    Icon(
                        painterResource(id = R.drawable.baseline_playlist_play_24),
                        contentDescription = "playlist",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

        }

    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun bottomNavigationBar(){
//    val navController = rememberNavController()
//
//    val bottomNavigationItems = listOf(
//        SpotifyBottomNavigationScreens.Home,
//        SpotifyBottomNavigationScreens.Play,
//        SpotifyBottomNavigationScreens.Playlist
//    )
//    Scaffold(
//        bottomBar = {
//            SpotifyAppBottomNavigation(navController, bottomNavigationItems)
//        },
//    ){
//        //
//    }
//
//}

//@Composable
//fun SpotifyAppBottomNavigation(navController: NavHostController, bottomNavigationItems: List<SpotifyBottomNavigationScreens>) {
//    BottomNavigation {
//        val currentRoute = currentRoute(navController)
//        bottomNavigationItems.forEach { screen ->
//            BottomNavigationItem(
//                icon = { Icon(screen.imageVector, contentDescription = screen.text) },
//                label = { Text(screen.text) },
//                selected = currentRoute == screen.route,
////                alwaysShowLabels = false, // This hides the title for the unselected items
//                onClick = {
//                    // This if check gives us a "singleTop" behavior where we do not create a
//                    // second instance of the composable if we are already on that destination
//                    if (currentRoute != screen.route) {
//                        navController.navigate(screen.route)
//                    }
//                }
//            )
//        }
//    }
//}
@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString("route")
}


@Composable
fun countryPicker(context: Context, spotifyViewModel: SpotifyViewModel) {
   PhoneCodesUtil.fetchPhoneCodes(context)

    var allCountries = PhoneCodesUtil.getAllCountry()
    Log.d("countries", "${allCountries}")


    DropdownMenu(
        expanded = spotifyViewModel.showCountryDropDown,
        onDismissRequest = { spotifyViewModel.showCountryDropDown = false },
        modifier= Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        allCountries.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = { Text(item.name) },
                onClick = {
                    spotifyViewModel.selectedCountry=item
                    spotifyViewModel.showCountryDropDown=false
                } )

        }
    }
}
@Composable
fun SongDisplay(
    index: Int,
    item: SpotifyTrendingSongsResponse,
    spotifyViewModel: SpotifyViewModel,
    navController: NavController
){
    var trackId= getTrackId(item,spotifyViewModel)

    Card(
        modifier = Modifier
            .padding(6.dp)
            .clickable { navController.navigate("song/${SongCategory.TRENDING_LIST.name}/$index/$trackId") }
        ,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.DarkGray)


    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(Color.DarkGray)
        ) {
            Column() {
                Image(
                    painter = rememberImagePainter(data = item.trackMetadata?.displayImageUri,
                        builder = {
                            scale(Scale.FILL)
                        }),
                    contentDescription = item.trackMetadata?.trackName,
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(130.dp)
                        .padding(horizontal = 10.dp)

                )
            }
            Column(verticalArrangement = Arrangement.Center,modifier=Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {


                textDisplay(item.trackMetadata?.trackName, "Track Name")
                textDisplay(
                    item.trackMetadata?.let { getArtistList(it.artists) },
                    "Artist Name"
                )
                textDisplay(item.trackMetadata?.releaseDate, "Release Date")
            }
        }
    }
}

fun getArtistList(artists: ArrayList<SpotifyTrendingSongsResponse.Artists>):String{
    var artist:String=""
    artists.forEachIndexed { index, item ->
        artist=artist+item.name+" ,"
    }
    return artist.substring(0,artist.length-2)
}
fun getArtistListFromTrackResponse(artists: ArrayList<SpotifyTrackDetailsResponse.Artists>):String{
    var artist:String=""
    artists.forEachIndexed { index, item ->
        artist=artist+item.name+" ,"
    }
    return artist.substring(0,artist.length-2)
}
fun getTrackId(item: SpotifyTrendingSongsResponse, spotifyViewModel: SpotifyViewModel):String? {
    var trackUri= item.trackMetadata?.trackUri

    var trackId= trackUri?.let { trackUri.substring(it.lastIndexOf(":")+1) }


    return trackId
}
@Composable
fun  textDisplay(value:String?, label:String) {
    Text(
        "$label : $value",
        style = TextStyle( fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp),
        color=Color.White,
        modifier = Modifier.padding(vertical = 3.dp, horizontal = 10.dp)
    )
}
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun SecondScreenSong(
//    spotifyViewModel: SpotifyViewModel,
//    index: Int?,
//    trackId: String?,
//    navController: NavHostController,
//    mediaPlayer: MediaPlayer
//) {
//    Log.d("$index", "$trackId:  ${spotifyViewModel.currentSongIndex}")
//    var item:SpotifyTrendingSongsResponse?=null
//    if(spotifyViewModel.spotifySongsList.isNotEmpty() ) {
//        item = index?.let { spotifyViewModel.spotifySongsList[index] }
//    }
//    if (trackId != null && (spotifyViewModel.currentSongUri.isEmpty() || index != spotifyViewModel.currentSongIndex)) {
//        spotifyViewModel.getTrackDetails(trackId, index,"COUNTRY_WISE")
//    }
//    var nextSongButtonEnabled by remember { mutableStateOf((index?.plus(1)) != spotifyViewModel.spotifySongsList.size) }
//
//
//    LaunchedEffect(key1 = spotifyViewModel.currentSongUri, block = {
//        if (spotifyViewModel.currentSongUri.isNotEmpty() && index == spotifyViewModel.currentSongIndex ) {
//            try {
//                mediaPlayer.reset()
//                mediaPlayer!!.setDataSource(spotifyViewModel.currentSongUri)
//                mediaPlayer!!.prepare()
//            }
//            catch (ex:Exception){
//                throw Exception(ex.message)
//            }
//            Log.d("setting data source","$index    $trackId    ${spotifyViewModel.currentSongUri}")
//        }
//    })
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(color = Color.Black),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            Modifier
//                .fillMaxWidth()
//                .padding(vertical = 10.dp), contentAlignment = Alignment.TopStart) {
//            Row(Modifier.fillMaxWidth()){
//                IconButton(modifier = Modifier
//                    .weight(1f)
//                    .padding(end = 30.dp),onClick = {
//                    spotifyViewModel.currentSongIndex=null
//                    mediaPlayer!!.pause()
//                    navController.navigate("home")
//                }) {
//                    Icon(
//                        modifier = Modifier.weight(1f),
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
//                IconButton(modifier = Modifier
//                    .weight(1f)
//                    .padding(start = 50.dp),
//                    onClick =
//                    {      spotifyViewModel.menuButtonExpanded=true }) {
//                    Icon(
//                        modifier = Modifier.weight(1f),
//                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
//                        contentDescription = "Menu",
//                        tint = Color.White,
//                    )
//                }
//            }
//            if(spotifyViewModel.menuButtonExpanded)
//                index?.let { DropDownMenuSong(navController,spotifyViewModel, it,mediaPlayer) }
//        }
//
//        if (item != null) {
//            Box(
//                modifier = Modifier
//                    .weight(0.8f)
//                    .fillMaxWidth()
//                    .padding(vertical = 0.dp)
//            ) {
//                AsyncImage(
//                    model = item.trackMetadata?.displayImageUri,
//                    contentDescription = item.trackMetadata?.trackName,
//                    Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 15.dp)
//                )
//            }
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 70.dp),
//                contentAlignment = Alignment.Center) {
//                Text(
//                    text = "${item.trackMetadata?.trackName}",
//                    color = Color.White,
//                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
//                    modifier = Modifier.basicMarquee(
//                        animationMode = MarqueeAnimationMode.Immediately,
//                        delayMillis = 50,
//                        velocity = 13.dp,
//                        spacing = MarqueeSpacing.fractionOfContainer(0.2f / 3f),
//                    ),
//                )
//            }
//            var artists = item.trackMetadata?.let { getArtistList(it.artists) }
//            if (artists != null) {
//                var listArtist = artists.split(" ,")
//                LazyRow(Modifier.padding(horizontal = 60.dp)) {
//                    itemsIndexed(listArtist) { index, item ->
//                        Text(
//                            text = if(index==listArtist.size-1) "$item" else "$item , ",
//                            color = Color.White,
//                            style = TextStyle(fontSize = 20.sp)
//                        )
//                    }
//                }
//            }
//
//            Row(
//                Modifier
//                    .padding(top = 10.dp)
//                    .padding(horizontal = 10.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "play",
//                    tint = Color.LightGray,
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clickable {
//                            if (trackId != null) {
//                                Log.d(
//                                    "on click",
//                                    "$index    $trackId    ${spotifyViewModel.currentSongUri}  ${mediaPlayer}"
//                                )
//                                try {
//                                    mediaPlayer!!.start()
//                                } catch (ex: Exception) {
//                                    throw Exception(ex.message)
//                                }
//                            }
//                        })
//                Icon(
//                    painter = painterResource(id = R.drawable.baseline_pause_24),
//                    contentDescription = "",
//                    Modifier
//                        .size(40.dp)
//                        .clickable { mediaPlayer!!.pause() }, tint = Color.LightGray
//                )
//                if (index != null) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_skip_next_24),
//                        contentDescription = "",
//                        Modifier
//                            .size(40.dp)
//                            .clickable(
//                                enabled = nextSongButtonEnabled
//                            ) {
//                                if (index != null && nextSongButtonEnabled) {
//                                    spotifyViewModel.currentSongIndex = index + 1
//                                    var nextTrackId = getTrackId(
//                                        spotifyViewModel.spotifySongsList[index + 1],
//                                        spotifyViewModel
//                                    )
//                                    navController.navigate("song/${index + 1}/$nextTrackId")
//
//                                }
//                            }, tint = if(nextSongButtonEnabled) Color.LightGray else Color.LightGray.copy(0.5f)
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SecondScreenSong(
    spotifyViewModel: SpotifyViewModel,
    index: Int?,
    trackId: String?,
    songCategory: String?,
    navController: NavHostController,
    mediaPlayer: MediaPlayer,
) {
    var songListToBeUsed by remember { mutableStateOf(spotifyViewModel.spotifySongsList) }
    var currentSongIndexToBeUsed by remember { mutableStateOf(spotifyViewModel.currentSongIndex) }
    var currentSongUriToBeUsed by remember { mutableStateOf(spotifyViewModel.currentSongUri) }

    if(songCategory==SongCategory.PLAYLIST.name){
        songListToBeUsed=spotifyViewModel.playlistSongs
        currentSongIndexToBeUsed=spotifyViewModel.playlistCurrentSongIndex
        currentSongUriToBeUsed=spotifyViewModel.playlistCurrentSongUri
    }
    else{
        songListToBeUsed=spotifyViewModel.spotifySongsList
        currentSongIndexToBeUsed=spotifyViewModel.currentSongIndex
        currentSongUriToBeUsed=spotifyViewModel.currentSongUri
    }

    if (trackId != null && (currentSongUriToBeUsed.isEmpty() || index != currentSongIndexToBeUsed) && songCategory!=null) {
            spotifyViewModel.getTrackDetails(trackId, index, songCategory)
    }
    var nextSongButtonEnabled by remember { mutableStateOf((index?.plus(1)) != songListToBeUsed.size) }

    Log.d("current track","$currentSongIndexToBeUsed $currentSongUriToBeUsed $songListToBeUsed")
    LaunchedEffect(key1 = currentSongUriToBeUsed, block = {
        if (currentSongUriToBeUsed.isNotEmpty() && index == currentSongIndexToBeUsed) {
            try {
                mediaPlayer.reset()
                Log.d("setting data source", "$index    $trackId  ${songCategory} ${currentSongIndexToBeUsed}  ${currentSongUriToBeUsed}")
                mediaPlayer!!.setDataSource(currentSongUriToBeUsed)
                mediaPlayer!!.prepare()
            } catch (ex: Exception) {
                throw Exception(ex.message)
            }

        }
    })

    if (spotifyViewModel.currentTrackInDisplay!=null) {
        Log.d("not null","${spotifyViewModel.currentTrackInDisplay}")
        var  itemDisplay=spotifyViewModel.currentTrackInDisplay
        if(itemDisplay!=null){
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), contentAlignment = Alignment.TopStart
            ) {
                Row(Modifier.fillMaxWidth()) {
                    IconButton(modifier = Modifier
                        .weight(1f)
                        .padding(end = 30.dp), onClick = {
                        spotifyViewModel.currentSongIndex = null
                        spotifyViewModel.playlistCurrentSongIndex=null
                        mediaPlayer!!.pause()
                        navController.navigate("home")
                    }) {
                        Icon(
                            modifier = Modifier.weight(1f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(modifier = Modifier
                        .weight(1f)
                        .padding(start = 50.dp),
                        onClick =
                        { spotifyViewModel.menuButtonExpanded = true }) {
                        Icon(
                            modifier = Modifier.weight(1f),
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = "Menu",
                            tint = Color.White,
                        )
                    }
                }
                if (spotifyViewModel.menuButtonExpanded)
                    index?.let {
                        DropDownMenuSong(
                            navController,
                            spotifyViewModel,
                            it,
                            mediaPlayer
                        )
                    }
            }

                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                ) {
                    AsyncImage(
                        model = itemDisplay.tracks[0]?.album?.images?.get(0)?.url,
                        contentDescription = itemDisplay.tracks[0].name,
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp)
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${itemDisplay.tracks[0].name}",
                        color = Color.White,
                        style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.basicMarquee(
                            animationMode = MarqueeAnimationMode.Immediately,
                            delayMillis = 50,
                            velocity = 13.dp,
                            spacing = MarqueeSpacing.fractionOfContainer(0.2f / 3f),
                        ),
                    )
                }
                var artists =
                    itemDisplay.tracks?.let { getArtistListFromTrackResponse(it[0].artists) }
                if (artists != null) {
                    var listArtist = artists.split(" ,")
                    LazyRow(Modifier.padding(horizontal = 60.dp)) {
                        itemsIndexed(listArtist) { index, item ->
                            Text(
                                text = if (index == listArtist.size - 1) "$item" else "$item , ",
                                color = Color.White,
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                    }
                }

                Row(
                    Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "play",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                if (trackId != null) {
                                    Log.d(
                                        "on click",
                                        "$index    $trackId    ${spotifyViewModel.currentSongUri}  ${mediaPlayer}"
                                    )
                                    try {
                                        mediaPlayer!!.start()
                                    } catch (ex: Exception) {
                                        throw Exception(ex.message)
                                    }
                                }
                            })
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = "",
                        Modifier
                            .size(40.dp)
                            .clickable { mediaPlayer!!.pause() }, tint = Color.LightGray
                    )
                    if (index != null) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_next_24),
                            contentDescription = "",
                            Modifier
                                .size(40.dp)
                                .clickable(
                                    enabled = nextSongButtonEnabled
                                ) {
                                    if (index != null && nextSongButtonEnabled) {
                                        if (songCategory == SongCategory.TRENDING_LIST.name)
                                            spotifyViewModel.currentSongIndex = index + 1
                                        else spotifyViewModel.playlistCurrentSongIndex = index + 1

                                        var nextTrackId = getTrackId(
                                            songListToBeUsed[index + 1], spotifyViewModel
                                        )
                                        Log.d("song---", "${songCategory}")
                                        navController.navigate("song/$songCategory/${index + 1}/$nextTrackId")

                                    }
                                },
                            tint = if (nextSongButtonEnabled) Color.LightGray else Color.LightGray.copy(
                                0.5f
                            )
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SecondScreenSong1(
    spotifyViewModel: SpotifyViewModel,
    index: Int?,
    trackId: String?,
    navController: NavHostController,
    mediaPlayer: MediaPlayer,
) {

    if (trackId != null && (spotifyViewModel.currentSongUri.isEmpty() || index != spotifyViewModel.currentSongIndex)) {
        spotifyViewModel.getTrackDetails(trackId, index, "COUNTRY_WISE")
    }
    var nextSongButtonEnabled by remember { mutableStateOf((index?.plus(1)) != spotifyViewModel.spotifySongsList.size) }


    LaunchedEffect(key1 = spotifyViewModel.currentSongUri, block = {
        if (spotifyViewModel.currentSongUri.isNotEmpty() && index == spotifyViewModel.currentSongIndex) {
            try {
                mediaPlayer.reset()
                mediaPlayer!!.setDataSource(spotifyViewModel.currentSongUri)
                mediaPlayer!!.prepare()
            } catch (ex: Exception) {
                throw Exception(ex.message)
            }
            Log.d("setting data source", "$index    $trackId    ${spotifyViewModel.currentSongUri}")
        }
    })

    if (spotifyViewModel.currentTrackInDisplay!=null) {
        Log.d("not null","${spotifyViewModel.currentTrackInDisplay}")
        var  itemDisplay=spotifyViewModel.currentTrackInDisplay
        if(itemDisplay!=null){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), contentAlignment = Alignment.TopStart
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        IconButton(modifier = Modifier
                            .weight(1f)
                            .padding(end = 30.dp), onClick = {
                            spotifyViewModel.currentSongIndex = null
                            mediaPlayer!!.pause()
                            navController.navigate("home")
                        }) {
                            Icon(
                                modifier = Modifier.weight(1f),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        IconButton(modifier = Modifier
                            .weight(1f)
                            .padding(start = 50.dp),
                            onClick =
                            { spotifyViewModel.menuButtonExpanded = true }) {
                            Icon(
                                modifier = Modifier.weight(1f),
                                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                                contentDescription = "Menu",
                                tint = Color.White,
                            )
                        }
                    }
                    if (spotifyViewModel.menuButtonExpanded)
                        index?.let {
                            DropDownMenuSong(
                                navController,
                                spotifyViewModel,
                                it,
                                mediaPlayer
                            )
                        }
                }

                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                ) {
                    AsyncImage(
                        model = itemDisplay.tracks[0]?.album?.images?.get(0)?.url,
                        contentDescription = itemDisplay.tracks[0].name,
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp)
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${itemDisplay.tracks[0].name}",
                        color = Color.White,
                        style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.basicMarquee(
                            animationMode = MarqueeAnimationMode.Immediately,
                            delayMillis = 50,
                            velocity = 13.dp,
                            spacing = MarqueeSpacing.fractionOfContainer(0.2f / 3f),
                        ),
                    )
                }
                var artists =
                    itemDisplay.tracks?.let { getArtistListFromTrackResponse(it[0].artists) }
                if (artists != null) {
                    var listArtist = artists.split(" ,")
                    LazyRow(Modifier.padding(horizontal = 60.dp)) {
                        itemsIndexed(listArtist) { index, item ->
                            Text(
                                text = if (index == listArtist.size - 1) "$item" else "$item , ",
                                color = Color.White,
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                    }
                }

                Row(
                    Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "play",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                if (trackId != null) {
                                    Log.d(
                                        "on click",
                                        "$index    $trackId    ${spotifyViewModel.currentSongUri}  ${mediaPlayer}"
                                    )
                                    try {
                                        mediaPlayer!!.start()
                                    } catch (ex: Exception) {
                                        throw Exception(ex.message)
                                    }
                                }
                            })
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = "",
                        Modifier
                            .size(40.dp)
                            .clickable { mediaPlayer!!.pause() }, tint = Color.LightGray
                    )
                    if (index != null) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_next_24),
                            contentDescription = "",
                            Modifier
                                .size(40.dp)
                                .clickable(
                                    enabled = nextSongButtonEnabled
                                ) {
                                    if (index != null && nextSongButtonEnabled) {
                                        spotifyViewModel.currentSongIndex = index + 1
                                        var nextTrackId = getTrackId(
                                            spotifyViewModel.spotifySongsList[index + 1],
                                            spotifyViewModel
                                        )
                                        navController.navigate("song/${index + 1}/$nextTrackId")

                                    }
                                },
                            tint = if (nextSongButtonEnabled) Color.LightGray else Color.LightGray.copy(
                                0.5f
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownMenuSong(
    navController: NavHostController,
    spotifyViewModel: SpotifyViewModel,
    index: Int,
    mediaPlayer: MediaPlayer
) {
    Log.d("index song added","$index")
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
        Popup(alignment = Alignment.Center, onDismissRequest = {spotifyViewModel.menuButtonExpanded=false}) {
            DropdownMenu(
                expanded = spotifyViewModel.menuButtonExpanded,
                onDismissRequest = { spotifyViewModel.menuButtonExpanded = false },
                Modifier.background(color=Color.DarkGray.copy(0.1f))
            ) {
                spotifyViewModel.dropDownMenuItemsSongDisplay.forEachIndexed() { it, item ->
                    DropdownMenuItem(
                        text = { Text(item, fontSize = 15.sp, color = Color.Black) },
                        onClick = {
                            if (it == 0) {
                                spotifyViewModel.playlistSongs += spotifyViewModel.spotifySongsList[index]
                                spotifyViewModel.menuButtonExpanded=false
                            }

                        })
                }
            }

        }
    }
}
@Composable
fun PlaylistScreen(
    spotifyViewModel: SpotifyViewModel,
    navController: NavHostController,
    mediaPlayer: MediaPlayer
){
    var reloadCounter by remember { mutableStateOf(0) }
    Log.d("counter","${reloadCounter}")
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)){

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), contentAlignment = Alignment.TopStart){
                Text("Playlist", fontSize = 35.sp,color=Color.White, fontWeight = FontWeight.Bold)
            }
            if(spotifyViewModel.playlistSongs.isNotEmpty()) {
                LazyColumn(Modifier.padding(10.dp)) {
                    itemsIndexed(spotifyViewModel.playlistSongs) { index, item ->
                        EachPlaylistSong(item, index, spotifyViewModel, mediaPlayer, increaseReload =  { reloadCounter++ },
                            getReload = {reloadCounter},navController)

                    }
                }
            }
            else{
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_playlist_add_24),
                            contentDescription = "add playlist",
                            tint = Color.White,
                            modifier = Modifier
                                .size(70.dp)
                                .clickable { navController.navigate("home") }
                        )

                        Text(
                            text = "No Songs added",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.LightGray
                            ),
                            modifier = Modifier
                        )


                    }

                }

        }
    }
}

@Composable
fun EachPlaylistSong(
    item: SpotifyTrendingSongsResponse,
    index: Int,
    spotifyViewModel: SpotifyViewModel,
    mediaPlayer: MediaPlayer,
    increaseReload: () -> Unit,
    getReload: () -> Int,
    navController: NavHostController
) {




    var trackId = getTrackId(item, spotifyViewModel)
    LaunchedEffect(key1 = spotifyViewModel.playlistCurrentSongUri, block = {
        if (spotifyViewModel.playlistCurrentSongUri.isNotEmpty() && index == spotifyViewModel.playlistCurrentSongIndex) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(spotifyViewModel.playlistCurrentSongUri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    })


    Card(
        modifier = Modifier
            .clickable { navController.navigate("song/${SongCategory.PLAYLIST.name}/$index/$trackId") }
            .padding(6.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.DarkGray.copy(0.5f))


    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.DarkGray.copy(0.5f))
                .padding(vertical = 10.dp)
        ) {
            Column(Modifier.background(Color.DarkGray.copy(0.5f))) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = item.trackMetadata?.displayImageUri)
                            .apply(block = fun ImageRequest.Builder.() {
                                scale(Scale.FILL)
                            }).build()
                    ),
                    contentDescription = item.trackMetadata?.trackName,
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(100.dp)
                        .padding(horizontal = 10.dp)

                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.DarkGray.copy(0.5f))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {

                Text(
                    "Track Name : ${item.trackMetadata?.trackName}",
                    color = Color.White,
                    fontSize = 15.sp
                )
                Row(
                    Modifier
                        .background(Color.DarkGray.copy(0.5f))
                        .padding(top = 20.dp)
                        .padding(horizontal = 70.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "play",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (!trackId.isNullOrEmpty()) {
                                    spotifyViewModel.getTrackDetails(trackId, index, "PLAYLIST")
                                }
                            })
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = "",
                        Modifier
                            .size(28.dp)
                            .clickable { mediaPlayer!!.pause() }, tint = Color.LightGray
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_playlist_remove_24),
                        contentDescription = "",
                        Modifier
                            .size(28.dp)
                            .clickable {
                                spotifyViewModel.playlistSongs.removeAt(index)
                                mediaPlayer!!.pause()
                                increaseReload()
                                Log.d("counter", "${getReload()}")
                                Log.d("onclick remove", "${spotifyViewModel.playlistSongs}")
                            },
                        tint = Color.LightGray

                    )
                }
            }
        }
    }
}


