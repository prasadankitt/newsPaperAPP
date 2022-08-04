package com.example.newspaper

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {
    //private val currNewsUrl : String? = null
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //val items = fetchData()
        fetchData()
        //val adapter : NewsListAdapter = NewsListAdapter(item,this)
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

/*  private fun fetchData() : ArrayList<String> {
        val list = ArrayList<String>()
        for(i in 0 until 1000)
        {
            list.add("Item $i")
        }
        return list
    }
*/

/*    private fun fetchData(){
        val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=05cedf3838c64280a36089a75746093b"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
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
            {

            })
        /*    {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String>
                {
                    val params: MutableMap<String, String> = HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }*/
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    } */
private fun fetchData() {
    val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=05cedf3838c64280a36089a75746093b"
    val getRequest: JsonObjectRequest = object : JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        Response.Listener {
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for(i in 0 until  newsJsonArray.length()){
                val newsJsonObject = newsJsonArray.getJSONObject(i)
                val news = News(
                    newsJsonObject.getString("author"),
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("urlToImage")
                )

                newsArray.add(news)
            }
            mAdapter.updateNews(newsArray)
        },
        Response.ErrorListener { error ->

        }
    ) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val params: MutableMap<String, String> = HashMap()
            params["User-Agent"] = "Mozilla/5.0"
            return params
        }
    }
    MySingleton.getInstance(this).addToRequestQueue(getRequest)
}

    override fun onItemClicked(item: News) {
        //Toast.makeText(this, "clicked item is $item", Toast.LENGTH_LONG).show()
        //instead of using intent and leaving app we can use custom tabs
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}