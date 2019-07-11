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
import id.nerdstudio.newsreader.detail.DetailActivity
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
                                list += news
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
        news_list.adapter = NewsAdapter(this, list) {
            val intent = Intent(this, DetailActivity::class.java)
                .putExtra(DetailActivity.ARG_NEWS, it)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(news_thumbnail, "thumbnail"),
                Pair.create(news_title, "title"),
                Pair.create(news_source, "source"),
                Pair.create(news_timestamp, "timestamp")
            )
            startActivity(intent, options.toBundle())
        }
        news_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        news_list.setHasFixedSize(true)
    }
}
