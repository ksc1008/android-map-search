package ksc.campus.tech.kakao.map.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SearchResultRepository() {
    private val _searchResult: MutableLiveData<List<SearchResult>> = MutableLiveData(listOf())
    val searchResult: LiveData<List<SearchResult>>
        get() = _searchResult

    fun clearResults(){
        _searchResult.postValue(listOf())
    }

    fun search(text: String, apiKey: String) {
        clearResults()
        SearchKakaoHelper.batchSearchByKeyword(text, apiKey, 10){
            _searchResult.postValue((_searchResult.value?: listOf()) + it)
        }
    }

    companion object {
        private var instance: SearchResultRepository? = null

        fun getInstance(context: Context): SearchResultRepository {
            if (instance == null) {
                instance = SearchResultRepository()
            }
            return instance as SearchResultRepository
        }
    }
}