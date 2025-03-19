package com.anime.aniwatch.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.anime.aniwatch.R
import com.anime.aniwatch.data.Watchlists

class WatchListViewAdapter(
    val context: Context,
    val watchList: List<Watchlists>,
    val onClickShowMore : (Watchlists) -> Unit,
    val onClickItem: (Watchlists) -> Unit,
    val onLongPressed: (position: Int) -> Unit
): BaseAdapter() {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        val view = contentView ?: LayoutInflater.from(context).inflate(
            R.layout.item_list_of_watchlist,
            parent,
            false
        )

        val photo = view.findViewById<ImageView>(R.id.imageview_logo)
        val code = view.findViewById<TextView>(R.id.textview_code)
        val desc = view.findViewById<TextView>(R.id.textview_desc)
        val showMore = view.findViewById<TextView>(R.id.textview_showmore)

        val subject = watchList[position]

        photo.setImageResource(subject.photo)
        code.setText(subject.code)
        desc.setText(subject.desc)

        showMore.setOnClickListener {
            onClickShowMore(subject)
        }

        view.setOnClickListener {
            onClickItem(subject)
        }

        view.setOnLongClickListener {
            onLongPressed(position)
            true
        }

       return view
    }

    override fun getCount(): Int = watchList.size

    override fun getItem(position: Int): Any = watchList[position]

    override fun getItemId(position: Int): Long = position.toLong()
}