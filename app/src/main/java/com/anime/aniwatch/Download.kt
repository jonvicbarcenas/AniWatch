package com.anime.aniwatch

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ListView
import com.anime.aniwatch.data.DownloadLists
import com.anime.aniwatch.helper.SubjectListViewAdapter

class Download : Activity() {
    lateinit var listOfSubjects: MutableList<DownloadLists>
    lateinit var arrayAdapter: SubjectListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val listview = findViewById<ListView>(R.id.listview)

        listOfSubjects = mutableListOf(
            DownloadLists("Episode 1", "Description 1", "2023-01-01"),
            DownloadLists("Episode 2", "Description 2", "2023-01-02"),
            DownloadLists("Episode 3", "Description 3", "2023-01-03")
        )

        arrayAdapter = SubjectListViewAdapter(
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

    private fun showOtherDetailsDialog(subject: DownloadLists) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(subject.name)
        builder.setMessage(subject.desc)
        builder.setPositiveButton("Okay", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete item")
        builder.setMessage("Are you sure to delete ${listOfSubjects[position].name}?")
        builder.setPositiveButton("Remove") { _, _ ->
            listOfSubjects.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}