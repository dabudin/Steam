package net.azarquiel.steam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.azarquiel.steam.api.MainRepository
import net.azarquiel.steam.model.*

class MainViewModel : ViewModel() {
    private var repository: MainRepository = MainRepository()

    fun getDataGames(): MutableLiveData<List<Game>> {
        val games = MutableLiveData<List<Game>>()
        GlobalScope.launch(Dispatchers.Main) {
            games.value = repository.getDataGames()
        }
        return games
    }

    fun getDataNews(idgame: Int): MutableLiveData<List<New>> {
        val news = MutableLiveData<List<New>>()
        GlobalScope.launch(Dispatchers.Main) {
            news.value = repository.getDataNews(idgame)
        }
        return news
    }

    fun getDataFavoritos(): MutableLiveData<List<Game>> {
        val favoritos = MutableLiveData<List<Game>>()
        GlobalScope.launch(Dispatchers.Main) {
            favoritos.value = repository.getDataFavoritos()
        }
        return favoritos
    }

    fun saveFavorito(favorito: Game): MutableLiveData<Game> {
        val fav = MutableLiveData<Game>()
        GlobalScope.launch(Dispatchers.Main) {
            fav.value = repository.saveFavorito(favorito)
        }
        return fav
    }

    fun deleteFavorito(idgame: Int): MutableLiveData<Game> {
        val fav = MutableLiveData<Game>()
        GlobalScope.launch(Dispatchers.Main) {
            fav.value = repository.deleteFavorito(idgame)
        }
        return fav
    }

    fun getUsuario(nick: String, pass: String): MutableLiveData<Usuario?> {
        val usuario = MutableLiveData<Usuario?>()
        GlobalScope.launch(Dispatchers.Main) {
            usuario.value = repository.getUsuario(nick, pass)
        }
        return usuario
    }

    fun saveUsuario(usuario: Usuario): MutableLiveData<Usuario> {
        val usuarioresponse = MutableLiveData<Usuario>()
        GlobalScope.launch(Dispatchers.Main) {
            usuarioresponse.value = repository.saveUsuario(usuario)
        }
        return usuarioresponse
    }
}