package com.gauravtyagisrm.newsfresh

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.gauravtyagisrm.memeshare.MySingleton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemsClicked {
override lateinit var mAdapter: NewsListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

    }
    private fun fetchData() {
        val url =" https://saurav.tech/NewsAPI/everything/cnn.json"
        val jsonObjectRequest = JsonObjectRequest(
            com.android.volley.Request.Method.GET, url,null,
            Response.Listener {
                              val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")

                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

            },
            Response.ErrorListener {
                Toast.makeText(this, "Something went Wrong",Toast.LENGTH_LONG).show()


            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue((jsonObjectRequest))
    }


   override fun onItemClicked(item: News) {

       val builder = CustomTabsIntent.Builder()
       val customTabsIntent = builder.build()
       customTabsIntent.launchUrl(this, Uri.parse(item.url))
   }
    }
