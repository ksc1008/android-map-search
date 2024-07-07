package campus.tech.kakao.map.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.models.SearchKeywordRepository
import campus.tech.kakao.map.models.SearchResult
import campus.tech.kakao.map.models.SearchResultRepository


class SearchActivityViewModel (application: Application): AndroidViewModel(application) {
    private val searchResultRepository: SearchResultRepository = SearchResultRepository.getInstance(application)
    private val keywordRepository: SearchKeywordRepository = SearchKeywordRepository.getInstance(application)

    private val _searchText: MutableLiveData<String> = MutableLiveData("")

    val searchResult: LiveData<List<SearchResult>>
        get() = searchResultRepository.searchResult
    val keywords: LiveData<List<String>>
        get() = keywordRepository.keywords
    val searchText: LiveData<String>
        get() = _searchText

    init{
        keywordRepository.getKeywords()
    }

    private fun search(query: String){
        searchResultRepository.search(query)
    }

    private fun addKeyword(keyword: String){
        keywordRepository.addKeyword(keyword)
    }

    private fun deleteKeyword(keyword: String){
        keywordRepository.deleteKeyword(keyword)
    }

    fun clickSearchResultItem(selectedItem: SearchResult){
        addKeyword(selectedItem.name)
    }

    fun submitQuery(value: String){
        search(value)
    }

    fun clickKeywordDeleteButton(keyword: String){
        deleteKeyword(keyword)
    }

    fun clickKeyword(keyword: String){
        _searchText.postValue(keyword)
        search(keyword)
    }
}