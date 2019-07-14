package id.nerdstudio.newsreader.util

import android.view.View
import androidx.core.view.ViewCompat

fun View.transitionName() = ViewCompat.getTransitionName(this)

fun View.setTransition(name: String) = ViewCompat.setTransitionName(this, name)