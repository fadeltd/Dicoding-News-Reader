package id.nerdstudio.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.Pair
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import id.nerdstudio.newsreader.adapter.NewsAdapter
import id.nerdstudio.newsreader.config.AppConfig
import id.nerdstudio.newsreader.data.News
import id.nerdstudio.newsreader.util.getCurrentDayDate
import id.nerdstudio.newsreader.util.transitionName
import id.nerdstudio.newsreader.detail.DetailActivity
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_NEWS
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_SOURCE
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_THUMBNAIL
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_TIMESTAMP
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_TITLE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_news.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.today_news, getCurrentDayDate())

        loadData()
    }

    private fun loadData() {
        Ion
            .with(this)
            .load(AppConfig.TOP_HEADLINES)
            .asJsonObject()
            .withResponse()
            .setCallback { e, response ->
                news_loading.visibility = View.GONE
                if (e != null) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                } else {
                    val code = response.headers.code()
                    if (code == 200) {
                        val result = response.result
                        val status = result["status"].asString
                        if (status == "ok") {
                            val articles = result["articles"].asJsonArray
                            var list = listOf<News>()
                            for (i in 0 until articles.size()) {
                                val news = Gson().fromJson(articles[i].asJsonObject, News::class.java)
                                list = list + news
                            }
                            render(list)
                        } else {
                            val message = result["message"].asString
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "http error code $code", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun render(list: List<News>) {
        news_list.adapter = NewsAdapter(this, list)
        news_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        news_list.setHasFixedSize(true)
    }
}