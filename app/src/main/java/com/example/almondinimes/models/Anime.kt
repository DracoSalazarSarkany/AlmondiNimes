package com.example.almondinimes.models

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Anime(
    @SerializedName("mal_id") val mal_id: Int,
    val title: String,
    val synopsis: String?,
    val score: Double?,
    val status: String?,
    val episodes: Int?,
    val chapters: Int?,
    val volumes: Int?,
    val type: String?,
    val images: JikanImages,
    val genres: List<Genre>?,
    val aired: Aired?
)

