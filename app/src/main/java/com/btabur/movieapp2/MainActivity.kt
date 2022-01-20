package com.btabur.movieapp2

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.btabur.movieapp2.databinding.ActivityMainBinding
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

class MainActivity : AppCompatActivity() {

    val JSON_URL_NOWPLAYING =
        "https://api.themoviedb.org/3/movie/now_playing?api_key=438e6d6ac773d044fff5ed75db7608bd"
    val JSON_URL_UPCOMING =
        "https://api.themoviedb.org/3/movie/upcoming?api_key=438e6d6ac773d044fff5ed75db7608bd"
    val JSON_URL_SEARCH =
        "https://api.themoviedb.org/3/search/movie?api_key=438e6d6ac773d044fff5ed75db7608bd&query="

    lateinit var movieModelList: MutableList<MovieModel>
    lateinit var adaptor_slider: Adaptor_Slider
    lateinit var str_search: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieModelList = ArrayList()
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                } else {

                    binding.viewpager.setVisibility(View.GONE)
                    binding.recycler.setVisibility(View.GONE)
                    binding.circleindicator.setVisibility(View.GONE)
                    binding.pullToRefresh.setVisibility(View.GONE)
                    binding.imgClose.setVisibility(View.VISIBLE)

                    str_search = JSON_URL_SEARCH + binding.editSearch.getText().toString().trim()
                    val getData_search = GetData(str_search)
                    getData_search.execute()
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.imgClose.setOnClickListener(View.OnClickListener {
            binding.editSearch.setText("")
        })
        val getData = GetData(JSON_URL_UPCOMING)
        getData.execute()
        val getData1 = GetData(JSON_URL_NOWPLAYING)
        getData1.execute()
        binding.pullToRefresh.setOnRefreshListener {
            val getData3 = GetData(JSON_URL_UPCOMING)
            getData3.execute()
            binding.pullToRefresh.isRefreshing = false
        }
    }

    inner class GetData(private val str_url: String) :
        AsyncTask<String?, String?, String>() {

        override fun doInBackground(vararg params: String?): String {
            var current = ""
            try {
                val url: URL
                var urlConnection: URLConnection? = null
                try {
                    url = URL(str_url)
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
                    model.id = (jsonObject1.getString("id"))
                    model.description = (jsonObject1.getString("overview"))
                    model.title = (jsonObject1.getString("title"))
                    model.date = (jsonObject1.getString("release_date"))
                    model.img = (jsonObject1.getString("poster_path"))
                    model.vote_avrage = (jsonObject1.getString("vote_average"))
                    movieModelList.add(model)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (str_url == JSON_URL_UPCOMING) {
                PutDataIntoRecyclerView(movieModelList)
            } else if (str_url == JSON_URL_NOWPLAYING) {
                SetSlider(movieModelList)
            } else if (str_url == str_search) {
                SetlistSearch(movieModelList)
            }
        }


    }

    fun SetlistSearch(movieModelList: List<MovieModel>) {
        val adaptor_search = Adaptor_search(this, movieModelList)
        binding.listSearch.adapter = adaptor_search
    }

    private fun PutDataIntoRecyclerView(movieModelList: List<MovieModel>) {
        val adaptary = Adaptor_Recycler(this, movieModelList)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adaptary
    }

    private fun SetSlider(movieModelList: List<MovieModel>) {
        adaptor_slider = Adaptor_Slider(this, movieModelList)
        binding.viewpager.adapter = adaptor_slider
        binding.circleindicator.setViewPager(binding.viewpager)
        adaptor_slider.registerDataSetObserver(binding.circleindicator.dataSetObserver)
    }

    override fun onBackPressed() {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}