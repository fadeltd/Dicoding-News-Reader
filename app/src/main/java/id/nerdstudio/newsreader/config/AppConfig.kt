package id.nerdstudio.newsreader.config

import id.nerdstudio.newsreader.BuildConfig

object AppConfig {
    private const val SERVER_URL = BuildConfig.SERVER_URL
    private const val API_KEY = BuildConfig.API_KEY
    const val TOP_HEADLINES = "${SERVER_URL}top-headlines?country=id&apiKey=$API_KEY"
}