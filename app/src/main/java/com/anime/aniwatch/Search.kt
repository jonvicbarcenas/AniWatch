package com.anime.aniwatch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anime.aniwatch.databinding.SearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var movieList: MutableList<Movie>  // List of Movie objects
    private lateinit var movieAdapter: ArrayAdapter<Movie>
    lateinit var binding: SearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the binding to inflate the layout
        binding = SearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up back button
        val backButton: ImageView = binding.backButton
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize views
        searchView = binding.search
        listView = binding.list

        // Fetch movie details from resources (same as in Day fragment)
        val movieTitles = resources.getStringArray(R.array.movie_titles)
        val movieDescriptions = resources.getStringArray(R.array.movie_descriptions)
        val movieDurations = resources.getStringArray(R.array.movie_durations)
        val movieImages = MovieData.movieImages

        // Dynamically create the list of Movie objects
        movieList = mutableListOf()
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

        // Set up the adapter for ListView (show name and image)
        movieAdapter = object : ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, movieList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)

                val movie = getItem(position)

                val movieName = view.findViewById<TextView>(R.id.movieTitle)
                val movieImage = view.findViewById<ImageView>(R.id.movieImage)

                // Set movie data (name and image)
                movieName.text = movie?.name
                movieImage.setImageResource(movie?.imageResId ?: R.drawable.logo_no_bg)

                return view
            }
        }

        listView.adapter = movieAdapter

        // Handle item click in ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMovie = movieList[position]
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("movie", selectedMovie)  // Pass the entire Movie object
            startActivity(intent)
        }

        // Handle the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the search query
                newText?.let {
                    val filteredList = movieList.filter { movie ->
                        movie.name.contains(it, ignoreCase = true)
                    }
                    movieAdapter.clear()
                    movieAdapter.addAll(filteredList)
                    movieAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }
}
