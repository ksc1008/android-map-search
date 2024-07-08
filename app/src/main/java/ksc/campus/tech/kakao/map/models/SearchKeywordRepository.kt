package ksc.campus.tech.kakao.map.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchKeywordRepository(context: Context) {
    private val _keywords: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val keywords: LiveData<List<String>>
        get() = _keywords

    private var searchDb: SearchDbHelper

    init {
        searchDb = SearchDbHelper(context)
    }

    fun queryKeyWordAndPostValue() {
        val newData = searchDb.queryAllSearchKeywords()
        _keywords.postValue(newData)
    }

    fun addKeyword(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            searchDb.insertOrReplaceKeyword(keyword)
            queryKeyWordAndPostValue()
        }
    }

    fun deleteKeyword(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            searchDb.deleteKeyword(keyword)
            queryKeyWordAndPostValue()
        }
    }

    fun getKeywords() {
        CoroutineScope(Dispatchers.IO).launch {
            val newData = searchDb.queryAllSearchKeywords()
            _keywords.postValue(newData)
        }
    }

    companion object {
        private var instance: SearchKeywordRepository? = null

        fun getInstance(context: Context): SearchKeywordRepository {
            if (instance == null) {
                instance = SearchKeywordRepository(context)
            }
            return instance as SearchKeywordRepository
        }
    }
}