package com.anime.aniwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Day : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data from strings.xml
        val movieTitles = resources.getStringArray(R.array.movie_titles)
        val movieDescriptions = resources.getStringArray(R.array.movie_descriptions)
        val movieDurations = resources.getStringArray(R.array.movie_durations)

        // Get images from MovieData.kt
        val movieImages = MovieData.movieImages

        // Dynamically create the list of Movie objects
        val movieList = mutableListOf<Movie>()
        for (i in movieTitles.indices) {
            movieList.add(
                Movie(
                    rank = i + 1,
                    name = movieTitles[i],
                    imageResId = movieImages[i],
                    duration = movieDurations[i],
                    description = movieDescriptions[i]
                )
            )
        }

        // Set up RecyclerView with adapter
        val adapter = MovieAdapter(movieList, requireContext())
        recyclerView.adapter = adapter

        return view
    }
}
