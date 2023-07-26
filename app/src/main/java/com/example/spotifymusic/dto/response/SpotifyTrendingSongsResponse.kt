package com.example.spotifymusic.dto.response

import com.google.gson.annotations.SerializedName

data class SpotifyTrendingSongsResponse (
    @SerializedName("chartEntryData"        ) var chartEntryData        : ChartEntryData? = ChartEntryData(),
    @SerializedName("missingRequiredFields" ) var missingRequiredFields : Boolean?        = null,
    @SerializedName("trackMetadata"         ) var trackMetadata         : TrackMetadata?  = TrackMetadata()

){
    data class RankingMetric (

        @SerializedName("value" ) var value : String? = null,
        @SerializedName("type"  ) var type  : String? = null

    )
    data class ChartEntryData (

        @SerializedName("currentRank"                   ) var currentRank                   : Int?           = null,
        @SerializedName("previousRank"                  ) var previousRank                  : Int?           = null,
        @SerializedName("peakRank"                      ) var peakRank                      : Int?           = null,
        @SerializedName("peakDate"                      ) var peakDate                      : String?        = null,
        @SerializedName("appearancesOnChart"            ) var appearancesOnChart            : Int?           = null,
        @SerializedName("consecutiveAppearancesOnChart" ) var consecutiveAppearancesOnChart : Int?           = null,
        @SerializedName("rankingMetric"                 ) var rankingMetric                 : RankingMetric? = RankingMetric(),
        @SerializedName("entryStatus"                   ) var entryStatus                   : String?        = null,
        @SerializedName("entryRank"                     ) var entryRank                     : Int?           = null,
        @SerializedName("entryDate"                     ) var entryDate                     : String?        = null

    )
    data class Artists (

        @SerializedName("name"        ) var name        : String? = null,
        @SerializedName("spotifyUri"  ) var spotifyUri  : String? = null,
        @SerializedName("externalUrl" ) var externalUrl : String? = null

    )
    data class Labels (

        @SerializedName("name"        ) var name        : String? = null,
        @SerializedName("spotifyUri"  ) var spotifyUri  : String? = null,
        @SerializedName("externalUrl" ) var externalUrl : String? = null

    )
    data class TrackMetadata (

        @SerializedName("trackName"       ) var trackName       : String?            = null,
        @SerializedName("trackUri"        ) var trackUri        : String?            = null,
        @SerializedName("displayImageUri" ) var displayImageUri : String?            = null,
        @SerializedName("artists"         ) var artists         : ArrayList<Artists> = arrayListOf(),
        @SerializedName("producers"       ) var producers       : ArrayList<String>  = arrayListOf(),
        @SerializedName("labels"          ) var labels          : ArrayList<Labels>  = arrayListOf(),
        @SerializedName("songWriters"     ) var songWriters     : ArrayList<String>  = arrayListOf(),
        @SerializedName("releaseDate"     ) var releaseDate     : String?            = null

    )

}