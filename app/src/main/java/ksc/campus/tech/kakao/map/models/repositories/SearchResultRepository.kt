package ksc.campus.tech.kakao.map.models.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.KakaoSearchService
import ksc.campus.tech.kakao.map.models.SearchResult

class SearchResultRepository() {
    private val _searchResult: MutableLiveData<List<SearchResult>> = MutableLiveData(listOf())
    val searchResult: LiveData<List<SearchResult>>
        get() = _searchResult

    private fun clearResults() {
        _searchResult.postValue(listOf())
    }

    fun search(text: String, apiKey: String) {
        clearResults()
        KakaoSearchService.batchSearchByKeyword(text, apiKey, BATCH_COUNT) {
            _searchResult.postValue((_searchResult.value ?: listOf()) + it)
        }
    }

    companion object {
        private const val BATCH_COUNT = 10
        private var _instance: SearchResultRepository? = null
        fun getInstance(): SearchResultRepository {
            if (_instance == null) {
                _instance = SearchResultRepository()
            }
            return _instance as SearchResultRepository
        }
    }
}