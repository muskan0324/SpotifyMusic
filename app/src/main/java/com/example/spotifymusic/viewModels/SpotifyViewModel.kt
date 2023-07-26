package com.example.spotifymusic.viewModels
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifymusic.SongCategory
import com.example.spotifymusic.apiService.SpotifyAPIService
import com.example.spotifymusic.dto.response.SpotifyTrackDetailsResponse
import com.example.spotifymusic.dto.response.SpotifyTrendingSongsResponse
import com.locon.core.data.CountryPhoneCode
import com.locon.core.data.PhoneCodesUtil
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class SpotifyViewModel:ViewModel() {
    var spotifySongsList:List<SpotifyTrendingSongsResponse> by mutableStateOf(listOf())

    private var spotifyAPIService= SpotifyAPIService.getInstance()

    var selectedCountry :CountryPhoneCode by mutableStateOf(PhoneCodesUtil.defaultPhoneCode)

    var trackIdsAudioUrls:MutableList<Pair<String,String>> by mutableStateOf( mutableListOf())

    var currentSongUri:String by mutableStateOf("")

    var currentSongIndex:Int? by mutableStateOf(null)

    var showCountryDropDown by mutableStateOf(false)

    var menuButtonExpanded by  mutableStateOf(false)

    var dropDownMenuItemsSongDisplay by mutableStateOf(listOf("Add to playlist","Play next"))

    var playlistSongs:MutableList<SpotifyTrendingSongsResponse> by mutableStateOf(mutableListOf())

    var playlistCurrentSongUri :String by mutableStateOf("")

    var playlistCurrentSongIndex:Int? by mutableStateOf(null)

    var currentTrackInDisplay :SpotifyTrackDetailsResponse? by  mutableStateOf(null)



    fun getSpotifySongs(){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                Log.d("c",selectedCountry.urlName)
                spotifySongsList=spotifyAPIService.getTrendingSongs(selectedCountry.urlName)
                Log.d("inside view model-list",spotifySongsList.toString())
            }
            catch (ex:Exception){
                throw Exception(ex.message)
            }
        }

    }
    fun getTrackDetails(trackId: String, index: Int?, s: String){

        viewModelScope.launch (Dispatchers.IO){
            try{
                Log.d("gettrackdetails","$trackId   $index  $s")
                var trackDetailsResponse= spotifyAPIService.getTrackDetails(trackId)
                Log.d("gettrackdetails","${trackDetailsResponse}")

                if(s==SongCategory.TRENDING_LIST.name){
                    currentSongUri= trackDetailsResponse.tracks[0].previewUrl!!
                    currentSongIndex=index
                }
                else
                {
                    playlistCurrentSongUri= trackDetailsResponse.tracks[0].previewUrl!!
                    playlistCurrentSongIndex=index
                }
                currentTrackInDisplay=trackDetailsResponse
            }
            catch (ex:Exception){
                throw Exception(ex.message)
            }
        }

    }


}