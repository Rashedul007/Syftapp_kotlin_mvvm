package syft.com.syftapp_kotlin_mvvm.utils

import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.ItemList
import syft.com.syftapp_kotlin_mvvm.models.SearchQuery

class TestUtil{

    companion object{

        var obj_query_search1: SearchQuery = SearchQuery("java", "","",1)
        var obj_query_search_topic: SearchQuery = SearchQuery("java", "ruby","",1)
        var obj_query_search_topic_language: SearchQuery = SearchQuery("java", "ruby","java",1)


        var obj_itemList1 :ItemList = ItemList(36069920 , "vim-javacomplete2", "Vim script", "A JPA 2.1 compliant Polyglot Object-Datastore Mapping Library", "https://github.com/bazaarvoice")
        var obj_itemList2 :ItemList = ItemList(14176513 , "spoon", "Java", "Spoon is a metaprogramming library to analyze and transform Java source code", "https://github.com/INRIA/spoon")
        var obj_itemList3 :ItemList = ItemList(78658602 , "react-native-create-bridge", "JavaScript  ", "Gameboy emulator in Java 8", "https://github.com/trekawek/coffee-gb")


        var obj_items = mutableListOf<ItemList>(obj_itemList1, obj_itemList2, obj_itemList3)


        var obj_gitresult = GitResult(250, true , obj_items)


      //  var obj_gitResult1 :GitResult = listOf(11, 5, 3, 8, 1, 9, 6, 2)
    }









}