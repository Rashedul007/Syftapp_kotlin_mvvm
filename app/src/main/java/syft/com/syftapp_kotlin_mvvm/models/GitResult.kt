package syft.com.syftapp_kotlin_mvvm.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GitResult (

    @SerializedName("total_count")
    @Expose
    var total_count: Int,

    @SerializedName("incomplete_results")
    @Expose
    var incomplete_results: Boolean,

    @SerializedName("items")
    @Expose
    var items: MutableList<ItemList>
) {

 /*   override fun toString(): String {
        return "BlogPost(count=$total_count, resl=$incomplete_results)"
    }*/



}