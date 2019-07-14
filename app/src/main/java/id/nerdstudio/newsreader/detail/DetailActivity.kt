package id.nerdstudio.newsreader.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Build
import android.view.MenuItem
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.koushikdutta.ion.Ion
import id.nerdstudio.newsreader.R
import id.nerdstudio.newsreader.data.News
import id.nerdstudio.newsreader.util.toRelativeTime
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        render()
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
    private fun render() {
        val news = intent.getParcelableExtra<News>(EXTRA_NEWS)
        supportActionBar?.title = news.title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            news_thumbnail.transitionName = intent.getStringExtra(EXTRA_TRANSITION_THUMBNAIL)
            news_title.transitionName = intent.getStringExtra(EXTRA_TRANSITION_TITLE)
            news_source.transitionName = intent.getStringExtra(EXTRA_TRANSITION_SOURCE)
            news_timestamp.transitionName = intent.getStringExtra(EXTRA_TRANSITION_TIMESTAMP)
            startPostponedEnterTransition()
        }
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
        const val EXTRA_NEWS = "news"
        const val EXTRA_TRANSITION_THUMBNAIL = "thumbnail"
        const val EXTRA_TRANSITION_TITLE = "title"
        const val EXTRA_TRANSITION_SOURCE = "source"
        const val EXTRA_TRANSITION_TIMESTAMP = "timestamp"
    }
}
