package net.azarquiel.steam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.azarquiel.steam.R
import net.azarquiel.steam.model.Game

class AdapterGame(val context: Context,
                    val layout: Int
) : RecyclerView.Adapter<AdapterGame.ViewHolder>() {

    private var dataList: List<Game> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setGames(games: List<Game>) {
        this.dataList = games
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Game){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            val ivrowgame = itemView.findViewById(R.id.ivrowgame) as ImageView
            val tvnombrerowgame = itemView.findViewById(R.id.tvnombrerowgame) as TextView

            tvnombrerowgame.text = dataItem.name

            // foto de internet a traves de Picasso
            Picasso.get().load("http://cdn.akamai.steamstatic.com/steam/apps/${dataItem.appid}/header.jpg").into(ivrowgame)

            itemView.tag = dataItem
        }
    }
}