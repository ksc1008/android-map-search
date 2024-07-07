package campus.tech.kakao.map.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchResultRepository(context: Context) {
    private val _searchResult: MutableLiveData<List<SearchResult>> = MutableLiveData(listOf())
    val searchResult: LiveData<List<SearchResult>>
        get() = _searchResult

    private lateinit var searchDb: SearchDbHelper

    init {
        searchDb = SearchDbHelper(context)
    }

    fun search(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = searchDb.querySearchResultsByName(text)
            _searchResult.postValue(result)
        }
    }

    companion object {
        private var instance: SearchResultRepository? = null

        fun getInstance(context: Context): SearchResultRepository {
            if (instance == null) {
                instance = SearchResultRepository(context)
            }
            return instance as SearchResultRepository
        }
    }
}