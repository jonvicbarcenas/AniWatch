package com.anime.aniwatch

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar



class SettingsActivity : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var listData: HashMap<String, List<List<String>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        expandableListView = findViewById(R.id.expandableListView)
        expandableListView.divider = resources.getDrawable(android.R.color.transparent, null)
        expandableListView.dividerHeight = 0
        listData = generateData()


        adapter = ExpandableListAdapter(this, listData)
        expandableListView.setAdapter(adapter)
    }

    private fun generateData(): HashMap<String, List<List<String>>> {
        val data = HashMap<String, List<List<String>>>()

        val developers = listOf(
            listOf(getString(R.string.person1_name), getString(R.string.person1_bio), getString(R.string.person1_experience)),  // Person 1 details
            listOf(getString(R.string.person2_name), getString(R.string.person2_bio), getString(R.string.person2_experience))  // Person 2 details
        )

        // Adding groups to the data
        data["Developers"] = developers

        return data
    }
}



