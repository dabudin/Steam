package net.azarquiel.steam.api

import net.azarquiel.steam.model.*

class MainRepository {
    val service = WebAccess.steamService

    suspend fun getDataGames(): List<Game> {
        val webResponse = service.getDataGames().await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.applist.apps
        }
        return emptyList()
    }

    suspend fun getDataNews(idgame: Int): List<New> {
        val webResponse = service.getDataNews(idgame).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.appnews.newsitems
        }
        return emptyList()
    }

    suspend fun getDataFavoritos(): List<Game> {
        val webResponse = service.getDataFavoritos().await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.games
        }
        return emptyList()
    }

    suspend fun saveFavorito(favorito: Game): Game? {
        val webResponse = service.saveFavorito(favorito).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.favorito
        }
        return null
    }

    suspend fun deleteFavorito(idgame: Int): Game? {
        val webResponse = service.deleteFavorito(idgame).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.favorito
        }
        return null
    }

    suspend fun getUsuario(nick:String, pass:String): Usuario? {
        val usuario: Usuario? = null
        val webResponse = service.getUsuario(nick, pass).await()
        if (webResponse.isSuccessful) {
            return webResponse.body()!!.usuario
        }
        return usuario
    }

    suspend fun saveUsuario(usuario: Usuario): Usuario? {
        var usuarioresponse: Usuario? = null
        val webResponse = service.saveUsuario(usuario).await()
        if (webResponse.isSuccessful) {
            usuarioresponse = webResponse.body()!!.usuario
        }
        return usuarioresponse
    }

}