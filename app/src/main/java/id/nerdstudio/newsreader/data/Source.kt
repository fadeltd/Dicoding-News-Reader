package id.nerdstudio.newsreader.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String = ""
) : Parcelable