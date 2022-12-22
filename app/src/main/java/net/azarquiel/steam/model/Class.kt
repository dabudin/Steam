package net.azarquiel.steam.model

import java.io.Serializable

data class Applist(
    var applist: App
)

data class App(
    var apps: List<Game>
)

data class Game(
    var appid: Int,
    var name: String
): Serializable

data class Appnews(
    var appid: Int,
    var newsitems: List<New>,
    var count: Int
)

data class New(
    var title: String,
    var contents: String
)

data class Usuario(
    var nick: String,
    var pass: String
)

data class Favoritos(
        var games: List<Game>
): Serializable

data class Result (
    val applist: App,
    val appnews: Appnews,
    val favoritos: Favoritos,
    val games: List<Game>,
    val favorito: Game,
    val usuario: Usuario,
    val msg: String
)