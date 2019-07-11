package id.nerdstudio.newsreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import id.nerdstudio.newsreader.R
import id.nerdstudio.newsreader.data.News
import id.nerdstudio.newsreader.util.toRelativeTime
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(
    private val context: Context,
    private val data: List<News>,
    private val onClick: (news: News) -> Unit
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
                }
                setOnClickListener {
                    onClick(news)
                }
            }
        }
    }
}