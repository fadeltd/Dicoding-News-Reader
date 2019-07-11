package id.nerdstudio.newsreader.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.nerdstudio.newsreader.R
import id.nerdstudio.newsreader.data.News
import kotlinx.android.synthetic.main.activity_detail.*
import android.view.MenuItem
import android.text.Html
import android.text.method.LinkMovementMethod
import com.koushikdutta.ion.Ion
import id.nerdstudio.newsreader.util.toRelativeTime

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val news = intent.getParcelableExtra<News>(ARG_NEWS)

        supportActionBar?.title = news.title
        render(news)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun render(news: News) {
        news_title.text = news.title
        Ion
            .with(this)
            .load(news.urlToImage)
            .asBitmap()
            .setCallback { e, result ->
                if (e == null) {
                    news_thumbnail.setImageBitmap(result)
                }
            }

        news_source.text = news.source?.name
        val text = "<a href=\"${news.url}\">${getString(R.string.full_article)}</a>"
        news_url.isClickable = true
        news_url.movementMethod = LinkMovementMethod.getInstance()
        news_url.text = Html.fromHtml(text)

        news_content.text = "${news.content.split("…")[0]}…"
        news_timestamp.text = news.publishedAt.toRelativeTime()
    }

    companion object {
        const val ARG_NEWS = "news"
    }
}
