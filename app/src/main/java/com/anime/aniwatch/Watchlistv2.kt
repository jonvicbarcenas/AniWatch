package com.anime.aniwatch

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ListView
import com.anime.aniwatch.data.Watchlists
import com.anime.aniwatch.helper.WatchListViewAdapter


class Watchlistv2: Activity() {
    lateinit var listOfSubjects: MutableList<Watchlists>
    lateinit var arrayAdapter: WatchListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)

        val listview = findViewById<ListView>(R.id.listview)

        listOfSubjects = mutableListOf(
            Watchlists(R.drawable.brook, "One Piece", "Description Ni", "asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.sanji, "One Piece", "Description Ni","asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.law, "One Piece", "Description Ni","asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.luffy, "One Piece", "Description Ni",  "asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.franky, "One Piece", "Description Ni", "asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.nami, "One Piece", "Description Ni",  "asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
            Watchlists(R.drawable.robin, "One Piece", "Description Ni", "asfsafdsaf dsafdsafs asfdsaf adsfsa fdsafdsa fsaf dsaf s"),
        )

        arrayAdapter = WatchListViewAdapter(
            this,
            listOfSubjects,
            onClickShowMore = { subject ->
                showOtherDetailsDialog(subject)
            },
            onClickItem = { subject ->
                listOfSubjects.add(subject)
                arrayAdapter.notifyDataSetChanged()
            },
            onLongPressed = { position ->
                showDeleteDialog(position)
            }
        )
        listview.adapter = arrayAdapter

    }

    private fun showOtherDetailsDialog(subject: Watchlists) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("${subject.code}")
        builder.setMessage("${subject.otherDetails}")
        builder.setPositiveButton("Okay", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete item")
        builder.setMessage("Are you sure to delete ${listOfSubjects[position]}")
        builder.setPositiveButton("Remove") { _, _ ->
            val removeItem = listOfSubjects.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()

    }
}