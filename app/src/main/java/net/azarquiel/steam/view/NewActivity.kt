package net.azarquiel.steam.view

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import net.azarquiel.steam.R
import net.azarquiel.steam.model.Favoritos
import net.azarquiel.steam.model.Game
import net.azarquiel.steam.model.New
import net.azarquiel.steam.viewmodel.MainViewModel

class NewActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private var isFavorito: Boolean = false
    private var isFavoritos = false
    private lateinit var favoritos: Favoritos
    private var news: List<New>? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var game: Game
    private lateinit var tvtitlenew: TextView
    private lateinit var tvcontentnew: TextView
    private lateinit var ivgamenew: ImageView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Game news"
        setSupportActionBar(toolbar)

        getIntentData()
        initViews()
        loadData()

        fab = findViewById(R.id.fab)
        setFabIcon()

        fab.setOnClickListener {
            onClickFav()
        }
    }

    override fun onBackPressed() {
        val intentReturn = Intent(this, MainActivity::class.java)
        intentReturn.putExtra("isFavoritosReturn", isFavoritos)
        setResult(RESULT_OK, intentReturn)

        //La llamada al 'super' debe ir al final para que luego no tengas errores
        //con el resultCode en el método 'onActivityResult' en la activity original.
        super.onBackPressed()
    }


    private fun getIntentData() {
        game = intent.getSerializableExtra("game") as Game
        favoritos = intent.getSerializableExtra("favoritos") as Favoritos
        isFavoritos = intent.getBooleanExtra("isFavoritos", false)
    }

    private fun initViews() {
        ivgamenew = findViewById(R.id.ivgamenew)
        tvtitlenew = findViewById(R.id.tvtitlenew)
        tvcontentnew = findViewById(R.id.tvcontentnew)
    }
    private fun loadData() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getDataNews(game.appid).observe(this) { it ->
            it?.let {
                news = it
                paintViews(it)
            }
        }
    }

    private fun paintViews(news: List<New>) {
        Picasso.get().load("http://cdn.akamai.steamstatic.com/steam/apps/${game.appid}/header.jpg").into(ivgamenew)
        tvtitlenew.text = news[0].title
        tvcontentnew.text = Html.fromHtml(news[0].contents, 0)
    }

    private fun setFabIcon() {
        if (favoritos.games.contains(game)) {
            fab.setImageResource(android.R.drawable.btn_star_big_on)
            isFavorito = true
        } else {
            fab.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    private fun onClickFav() {
        if (isFavorito) {
            viewModel.deleteFavorito(game.appid).observe(this) {
                it?.let {
                    deleteFavorito()
                }
            }
            fab.setImageResource(android.R.drawable.btn_star_big_off)
        } else {
            viewModel.saveFavorito(game).observe(this) {
                it?.let {
                    addFavorito()
                }
            }
            fab.setImageResource(android.R.drawable.btn_star_big_on)
        }

        isFavorito = !isFavorito
    }

    private fun deleteFavorito() {
        val favoritosAL = favoritos.games as ArrayList<Game>
        favoritosAL.removeAt(favoritos.games.indexOf(game))
        favoritos.games = favoritosAL.toList()
    }

    private fun addFavorito() {
        val favoritosAL = favoritos.games as ArrayList<Game>
        favoritosAL.add(game)
        favoritos.games = favoritosAL.toList()
    }

}