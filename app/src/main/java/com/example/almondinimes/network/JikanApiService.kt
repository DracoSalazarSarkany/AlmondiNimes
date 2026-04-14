package com.example.almondinimes.network

import com.example.almondinimes.models.JikanFullResponse
import com.example.almondinimes.models.JikanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    @GET("top/anime")
    suspend fun getTopAnime(@Query("page") page: Int): JikanResponse

    @GET("anime")
    suspend fun searchAnime(@Query("q") query: String): JikanResponse

    @GET("seasons/now")
    suspend fun getCurrentSeason(@Query("page") page: Int): JikanResponse

    @GET("top/manga")
    suspend fun getTopManga(@Query("page") page: Int): JikanResponse

    @GET("manga")
    suspend fun searchManga(@Query("q") query: String, @Query("page") page: Int): JikanResponse

    @GET("top/manga")
    suspend fun getTopMangaByFilter(
        @Query("filter") filter: String,
        @Query("page") page: Int
    ): JikanResponse

    @GET("anime/{id}/full")
    suspend fun getAnimeFull(@Path("id") id: Int): JikanFullResponse

    @GET("manga/{id}/full")
    suspend fun getMangaFull(@Path("id") id: Int): JikanFullResponse


}