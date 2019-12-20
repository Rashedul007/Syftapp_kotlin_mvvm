package syft.com.syftapp_kotlin_mvvm.models



data class SearchQuery (var filter_search: String,var filter_topics: String?,var filter_language: String?,var page_number:Int =0){
       override fun toString(): String {
        return "SearchQuery(fSearch=$filter_search, fTopic=$filter_topics,  fLang=$filter_language,  fPg=${page_number.toString()})"
    }
}