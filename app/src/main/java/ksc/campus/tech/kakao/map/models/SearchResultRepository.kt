package ksc.campus.tech.kakao.map.models

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


    init {
    }

    fun search(text: String, apiKey: String) {
        SearchKakaoHelper.batchSearchByKeyword(text, apiKey, 1){
            _searchResult.postValue(it)
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