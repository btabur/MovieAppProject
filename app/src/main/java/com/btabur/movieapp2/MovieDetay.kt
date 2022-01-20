package com.btabur.movieapp2

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btabur.movieapp2.databinding.ActivityMainBinding
import com.btabur.movieapp2.databinding.ActivityMovieDetayBinding
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.ArrayList

class MovieDetay : AppCompatActivity() {

    private val JSON_URL_UPSAME1 = "https://api.themoviedb.org/3/movie/"
    private val JSON_URL_UPSAME2 =
        "/similar?api_key=438e6d6ac773d044fff5ed75db7608bd&language=en-US&page=1"
    var str_img: String? = null
    var str_id:String? = null

    lateinit var movieModelList: MutableList<MovieModel>
    private lateinit var binding: ActivityMovieDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detay)

        binding = ActivityMovieDetayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieModelList = ArrayList()

        str_img = intent.getStringExtra("image")
        str_id = intent.getStringExtra("id")

        Glide.with(applicationContext).load("https://image.tmdb.org/t/p/w500$str_img")
            .into(binding.imageDetay)
        binding.textTitleDetay.text = intent.getStringExtra("title") + " (" + intent.getStringExtra("date")!!
            .substring(0, 4) + ")"
        binding.textDescriptionDetay.text = intent.getStringExtra("description") + " "
       binding.textDateDetay.text = intent.getStringExtra("date")
       binding.textVote.text = intent.getStringExtra("vote_avarage")

        str_id = JSON_URL_UPSAME1 + str_id + JSON_URL_UPSAME2

        val getData = GetData()
        getData.execute()


        //for add status bar
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.statusBarColor = Color.TRANSPARENT

    }

   inner class GetData : AsyncTask<String?, String?, String>() {

        override fun doInBackground(vararg params: String?): String {
            var current = ""
            try {
                val url: URL
                var urlConnection: URLConnection? = null
                try {
                    url = URL(str_id)
                    urlConnection = url.openConnection() as HttpURLConnection
                    val inputStream = urlConnection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    var data = inputStreamReader.read()
                    while (data != -1) {
                        current += data.toChar()
                        data = inputStreamReader.read()
                    }
                    return current
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (urlConnection != null) {
                        (urlConnection as HttpURLConnection).disconnect()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return current
        }

        override fun onPostExecute(s: String) {
            movieModelList.clear()
            try {
                val jsonObject = JSONObject(s)
                val jsonArray = jsonObject.getJSONArray("results")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val model = MovieModel()
                    model.id=(jsonObject1.getString("id"))
                    model.description=(jsonObject1.getString("overview"))
                    model.title=(jsonObject1.getString("title"))
                    model.date=(jsonObject1.getString("release_date"))
                    model.img=(jsonObject1.getString("poster_path"))
                    model.vote_avrage=(jsonObject1.getString("vote_average"))
                    movieModelList.add(model)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            PutDataIntoRecyclerView(movieModelList)
        }


    }

    private fun PutDataIntoRecyclerView(movieModelList: List<MovieModel>) {
        val adaptary = Ataptor_Same(this, movieModelList)
        binding.recyclerDetay.layoutManager = LinearLayoutManager(this)
       binding.recyclerDetay.adapter = adaptary
       binding.recyclerDetay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}