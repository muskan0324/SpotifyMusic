package com.example.spotifymusic

sealed class Screens (var route:String){
    object Spash:Screens("splash")
    object Home : Screens("home")
    object SongDisplay    :Screens("song/{songCategory}/{index}/{trackId}")
    object PlayList : Screens("playlists")
}
