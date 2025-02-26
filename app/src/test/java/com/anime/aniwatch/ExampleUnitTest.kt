package com.anime.aniwatch

import org.junit.Test
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonArray

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://aniwatch-api-feby.onrender.com/api/v2/hianime/home")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string()

        if (response.isSuccessful && responseBody != null) {
            val gson = Gson()
            val json = gson.fromJson(responseBody, JsonObject::class.java)

            if (json.get("success").asBoolean) {
                val data = json.getAsJsonObject("data")
                val spotlightAnimes = data.getAsJsonArray("spotlightAnimes")

                println("Spotlight Animes:")
                spotlightAnimes.forEach { anime ->
                    val animeObj = anime.asJsonObject
                    println("Rank: ${animeObj.get("rank").asInt}")
                    println("ID: ${animeObj.get("id").asString}")
                    println("Name: ${animeObj.get("name").asString}")
                    println("Description: ${animeObj.get("description").asString}")
                    println("Poster: ${animeObj.get("poster").asString}")
                    println("Japanese Name: ${animeObj.get("jname").asString}")
                    println("Episodes (Sub): ${animeObj.getAsJsonObject("episodes").get("sub").asInt}")
                    println("Episodes (Dub): ${animeObj.getAsJsonObject("episodes").get("dub").asInt}")
                    println("Type: ${animeObj.get("type").asString}")
                    println("Other Info: ${animeObj.getAsJsonArray("otherInfo")}")
                    println("----------------------------")
                }
            } else {
                println("API request failed: success = false")
            }
        } else {
            println("Request failed: ${response.message()}")
        }
    }
}