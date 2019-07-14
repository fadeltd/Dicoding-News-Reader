package id.nerdstudio.newsreader.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import id.nerdstudio.newsreader.MainActivity
import id.nerdstudio.newsreader.R
import id.nerdstudio.newsreader.data.News
import id.nerdstudio.newsreader.detail.DetailActivity
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_NEWS
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_THUMBNAIL
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_TITLE
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_SOURCE
import id.nerdstudio.newsreader.detail.DetailActivity.Companion.EXTRA_TRANSITION_TIMESTAMP
import id.nerdstudio.newsreader.util.toRelativeTime
import id.nerdstudio.newsreader.util.setTransition
import id.nerdstudio.newsreader.util.transitionName
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.item_news.view.news_source
import kotlinx.android.synthetic.main.item_news.view.news_thumbnail
import kotlinx.android.synthetic.main.item_news.view.news_timestamp
import kotlinx.android.synthetic.main.item_news.view.news_title

class NewsAdapter(
    private val context: Context,
    private val data: List<News>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(news: News) {
            itemView.run {
                news.also {
                    news_title.text = it.title
                    Ion.with(context)
                        .load(it.urlToImage)
                        .asBitmap()
                        .setCallback { e, result ->
                            if (e == null) {
                                news_thumbnail.setImageBitmap(result)
                            }
                        }
                    news_author.text = it.author
                    news_source.text = it.source?.name
                    news_timestamp.text = it.publishedAt.toRelativeTime()

                    news_thumbnail.setTransition(it.urlToImage)
                    news_title.setTransition(it.title)
                    news_source.setTransition("source $adapterPosition")
                    news_timestamp.setTransition("${it.publishedAt} $adapterPosition")
                }
                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                        .putExtra(EXTRA_NEWS, news)
                        .putExtra(EXTRA_TRANSITION_THUMBNAIL, it.news_thumbnail.transitionName())
                        .putExtra(EXTRA_TRANSITION_TITLE, it.news_title.transitionName())
                        .putExtra(EXTRA_TRANSITION_SOURCE, it.news_source.transitionName())
                        .putExtra(EXTRA_TRANSITION_TIMESTAMP, it.news_timestamp.transitionName())

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as MainActivity,
                        Pair.create(it.news_thumbnail, it.news_thumbnail.transitionName()),
                        Pair.create(it.news_title, it.news_title.transitionName()),
                        Pair.create(it.news_source, it.news_source.transitionName()),
                        Pair.create(it.news_timestamp, it.news_timestamp.transitionName())
                    )
                    startActivity(context, intent, options.toBundle())
                }
            }
        }
    }
}