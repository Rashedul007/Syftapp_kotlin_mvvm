package syft.com.syftapp_kotlin_mvvm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LiveResult (
    var respoList: GitResult?,
    var responseMessage: String
)