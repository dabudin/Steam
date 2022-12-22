package net.azarquiel.steam.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import net.azarquiel.steam.model.*

interface SteamService {

    @GET("game")
    fun getDataGames(): Deferred<Response<Result>>

    @GET("game/{idgame}/new")
    fun getDataNews(@Path("idgame") game: Int): Deferred<Response<Result>>

    @GET("game/favoritos")
    fun getDataFavoritos(): Deferred<Response<Result>>

    @POST("game/favorito")
    fun saveFavorito(@Body favorito: Game): Deferred<Response<Result>>

    @DELETE("game/{idgame}/favorito")
    fun deleteFavorito(@Path("idgame") game: Int): Deferred<Response<Result>>

    @GET("usuario")
    fun getUsuario(
        @Query("nick") nick: String,
        @Query("pass") pass: String): Deferred<Response<Result>>

    @POST("usuario")
    fun saveUsuario(@Body usuario: Usuario): Deferred<Response<Result>>
}