package net.azarquiel.steam.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import net.azarquiel.steam.R
import net.azarquiel.steam.adapters.AdapterGame
import net.azarquiel.steam.model.Favoritos
import net.azarquiel.steam.model.Game
import net.azarquiel.steam.model.Usuario
import net.azarquiel.steam.viewmodel.MainViewModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener  {

    private var isFavoritos: Boolean = false
    private lateinit var actionFavoritos: MenuItem
    private lateinit var favoritos: Favoritos
    private var existeUsuario: Boolean = false
    private lateinit var usuarioSP: SharedPreferences
    private var usuario: Usuario? = null
    private lateinit var games: List<Game>
    private lateinit var viewModel: MainViewModel
    private lateinit var rvGames: RecyclerView
    private lateinit var adapter: AdapterGame
    private lateinit var searchView: SearchView

//    companion object {
//        private const val REQUEST_NEW = 1
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        usuarioSP = getSharedPreferences("usuario", Context.MODE_PRIVATE)

        getUsuario()
        initRV()
        loadData()
    }

//    DEPRECATED
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_NEW) {
//            if (resultCode == RESULT_OK) {
//                val isFavoritosReturn = data!!.getBooleanExtra("isFavoritosReturn", false)
//                refreshFavoritos(isFavoritosReturn)
//            }
//        }
//    }

    // NUEVA FORMA DE HACERLO
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // There are no request codes
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val isFavoritosReturn = data!!.getBooleanExtra("isFavoritosReturn", false)
            refreshFavoritos(isFavoritosReturn)
        }
    }

    private fun refreshFavoritos(isFavoritosReturn: Boolean) {
        viewModel.getDataFavoritos().observe(this) { it ->
            it?.let {
                favoritos = Favoritos(it)
                if (isFavoritosReturn) {
                    adapter.setGames(favoritos.games)
                } else {
                    adapter.setGames(games)
                }
            }
        }

    }

    private fun getUsuario() {
        val nick = usuarioSP.getString("nick", "")!!
        val pass = usuarioSP.getString("pass", "")!!

        if (nick != "") {
            usuario = Usuario(nick, pass)
            existeUsuario = true
        } else {
            existeUsuario = false
        }
    }

    private fun initRV() {
        rvGames = findViewById(R.id.rvGames)
        adapter = AdapterGame(this, R.layout.row_game)
        rvGames.layoutManager = LinearLayoutManager(this)
        rvGames.adapter = adapter
    }

    private fun loadData() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getDataGames().observe(this) { it ->
            it?.let {
                games = it
                adapter.setGames(games)
            }
        }
        viewModel.getDataFavoritos().observe(this) { it ->
            it?.let {
                favoritos = Favoritos(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        actionFavoritos = menu.findItem(R.id.action_favoritos)

        // ************* <Filtro de búsqueda> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro de búsqueda> ************

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_login_register -> {
                onClickLoginRegister()
                true
            }
            R.id.action_favoritos -> {
                onClickFavoritos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClickLoginRegister() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login/Register")

        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        ll.setPadding(20)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0,40,0,40)

        val textInputLayoutNick = TextInputLayout(this)
        textInputLayoutNick.layoutParams = lp
        val etnick = EditText(this)
        etnick.setPadding(0, 80, 0, 80)
        etnick.textSize = 20.0F
        etnick.hint = "Nick"
        textInputLayoutNick.addView(etnick)

        val textInputLayoutPass = TextInputLayout(this)
        textInputLayoutPass.layoutParams = lp
        val etpass = EditText(this)
        etpass.setPadding(0, 80, 0, 80)
        etpass.textSize = 20.0F
        etpass.hint = "Pass"
        textInputLayoutPass.addView(etpass)

        ll.addView(textInputLayoutNick)
        ll.addView(textInputLayoutPass)
        builder.setView(ll)

        builder.setPositiveButton("Login/Register") { _, _ ->
            loginOrRegister(etnick.text.toString(), etpass.text.toString())
        }

        builder.setNegativeButton("Cancelar") { _, _ ->
        }

        builder.show()
    }

    private fun loginOrRegister(nick: String, pass: String) {
        viewModel.getUsuario(nick, pass).observe(this) {

            if (it != null) {
                usuario = it
                saveUsuarioSP(usuario!!)
                existeUsuario = true

                Snackbar.make(rvGames, "LOGIN CORRECTO:  '${usuario!!.nick}'", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                usuario = Usuario(nick, pass)

                viewModel.saveUsuario(usuario!!).observe(this) { it1 ->
                    it1?.let {
                        usuario = it1
                        saveUsuarioSP(usuario!!)
                        existeUsuario = true

                        Snackbar.make(
                            rvGames,
                            "USUARIO CREADO:  '${usuario!!.nick}'",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveUsuarioSP(usuario: Usuario) {
        val editor = usuarioSP.edit()
        //editor.putInt("id", usuario!!.id)
        editor.putString("nick", usuario.nick)
        editor.putString("pass", usuario.pass)
        editor.apply()
    }

    private fun onClickFavoritos() {
        isFavoritos = !isFavoritos

        if (isFavoritos && usuario != null) {
            adapter.setGames(favoritos.games)
            actionFavoritos.setIcon(android.R.drawable.star_big_on)
        } else if (usuario != null) {
            adapter.setGames(games)
            actionFavoritos.setIcon(android.R.drawable.star_big_off)
        }
    }

    // ************* <Filtro de búsqueda> ************
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Game>(games)
        adapter.setGames(original.filter { game ->
            game.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
        })
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }
    // ************* <Filtro de búsqueda> ************

    fun onClickGame(v: View) {
        if (existeUsuario) {
            val gamepulsado = v.tag as Game
            val intent = Intent(this, NewActivity::class.java)
            intent.putExtra("game", gamepulsado)
            intent.putExtra("favoritos", favoritos)
            intent.putExtra("isFavoritos", isFavoritos)

            //startActivityForResult(intent, REQUEST_NEW) --> deprecated
            resultLauncher.launch(intent) // --> nueva manera de hacerlo
        }
    }
}