package com.example.spotifymusic.dto.response

import com.google.gson.annotations.SerializedName

data class SpotifyTrackDetailsResponse(
    @SerializedName("tracks" ) var tracks : ArrayList<Tracks> = arrayListOf()
){
    data class ExternalUrls (

        @SerializedName("spotify" ) var spotify : String? = null

    )
    data class Artists (

        @SerializedName("external_urls" ) var externalUrls : ExternalUrls? = ExternalUrls(),
        @SerializedName("href"          ) var href         : String?       = null,
        @SerializedName("id"            ) var id           : String?       = null,
        @SerializedName("name"          ) var name         : String?       = null,
        @SerializedName("type"          ) var type         : String?       = null,
        @SerializedName("uri"           ) var uri          : String?       = null

    )
    data class Images (

        @SerializedName("height" ) var height : Int?    = null,
        @SerializedName("url"    ) var url    : String? = null,
        @SerializedName("width"  ) var width  : Int?    = null

    )
    data class Album (

        @SerializedName("album_type"             ) var albumType            : String?            = null,
        @SerializedName("artists"                ) var artists              : ArrayList<Artists> = arrayListOf(),
        @SerializedName("available_markets"      ) var availableMarkets     : ArrayList<String>  = arrayListOf(),
        @SerializedName("external_urls"          ) var externalUrls         : ExternalUrls?      = ExternalUrls(),
        @SerializedName("href"                   ) var href                 : String?            = null,
        @SerializedName("id"                     ) var id                   : String?            = null,
        @SerializedName("images"                 ) var images               : ArrayList<Images>  = arrayListOf(),
        @SerializedName("name"                   ) var name                 : String?            = null,
        @SerializedName("release_date"           ) var releaseDate          : String?            = null,
        @SerializedName("release_date_precision" ) var releaseDatePrecision : String?            = null,
        @SerializedName("total_tracks"           ) var totalTracks          : Int?               = null,
        @SerializedName("type"                   ) var type                 : String?            = null,
        @SerializedName("uri"                    ) var uri                  : String?            = null

    )
    data class ExternalIds (

        @SerializedName("isrc" ) var isrc : String? = null

    )
    data class Tracks (

        @SerializedName("album"             ) var album            : Album?             = Album(),
        @SerializedName("artists"           ) var artists          : ArrayList<Artists> = arrayListOf(),
        @SerializedName("available_markets" ) var availableMarkets : ArrayList<String>  = arrayListOf(),
        @SerializedName("disc_number"       ) var discNumber       : Int?               = null,
        @SerializedName("duration_ms"       ) var durationMs       : Int?               = null,
        @SerializedName("explicit"          ) var explicit         : Boolean?           = null,
        @SerializedName("external_ids"      ) var externalIds      : ExternalIds?       = ExternalIds(),
        @SerializedName("external_urls"     ) var externalUrls     : ExternalUrls?      = ExternalUrls(),
        @SerializedName("href"              ) var href             : String?            = null,
        @SerializedName("id"                ) var id               : String?            = null,
        @SerializedName("is_local"          ) var isLocal          : Boolean?           = null,
        @SerializedName("name"              ) var name             : String?            = null,
        @SerializedName("popularity"        ) var popularity       : Int?               = null,
        @SerializedName("preview_url"       ) var previewUrl       : String?            = null,
        @SerializedName("track_number"      ) var trackNumber      : Int?               = null,
        @SerializedName("type"              ) var type             : String?            = null,
        @SerializedName("uri"               ) var uri              : String?            = null

    )
}
