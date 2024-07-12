package ksc.campus.tech.kakao.map.models

import android.util.Log
import ksc.campus.tech.kakao.map.models.dto.KeywordSearchResponse
import ksc.campus.tech.kakao.map.models.mynetwork.MyNetworkCallbacks
import ksc.campus.tech.kakao.map.models.mynetwork.MyHttpHelper

object KakaoSearchService {
    private val categoryGroupCodeToDescription: HashMap<String, String> = hashMapOf(
        Pair("MT1", "대형마트"),
        Pair("CS2", "편의점"),
        Pair("PS3", "어린이집, 유치원"),
        Pair("SC4", "학교"),
        Pair("AC5", "학원"),
        Pair("PK6", "주차장"),
        Pair("OL7", "주유소, 충전소"),
        Pair("SW8", "지하철역"),
        Pair("BK9", "은행"),
        Pair("CT1", "문화시설"),
        Pair("AG2", "중개업소"),
        Pair("PO3", "공공기관"),
        Pair("AT4", "관광명소"),
        Pair("AD5", "숙박"),
        Pair("FD6", "음식점"),
        Pair("CE7", "카페"),
        Pair("HP8", "병원"),
        Pair("PM9", "약국")
    )

    /**
     * 요청이 유효한지 검증하기 위해 사용.
     *
     * 현재 진행중인 쿼리가 유저가 마지막으로 보낸 쿼리인지 검증한다.
     *
     * 유저가 새로운 쿼리를 보낼 때마다 [lastSearchId] 값이 1씩 증가한다.
    */
    private var lastSearchId: Int = 0

    private fun isQueryValid(query: String): Boolean = query.isNotBlank()


    private fun parseCategory(category: String) =
        category.split('>').last().trim().replace(",", ", ")

    private fun responseToResultArray(response: KeywordSearchResponse?): List<SearchResult>{
        if(response == null)
            return mutableListOf()
        val result = mutableListOf<SearchResult>()

        for (doc in response.documents) {
            result.add(
                SearchResult(
                    doc.id,
                    doc.place_name,
                    doc.address_name,
                    categoryGroupCodeToDescription.getOrDefault(
                        doc.category_group_code,
                        parseCategory(doc.category_name)
                    )
                )
            )
        }
        return result
    }


    fun batchSearchByKeyword(
        query: String,
        apiKey: String,
        batchCount: Int,
        onResponse: ((results: List<SearchResult>) -> Unit)?
    ) {
        lastSearchId++

        batchSearchByKeyword(lastSearchId, query, apiKey, 1, batchCount, onResponse)
    }


    private fun batchSearchByKeyword(
        searchId: Int,
        query: String,
        apiKey: String,
        page: Int,
        batchCount: Int,
        onResponse: ((results: List<SearchResult>) -> Unit)?
    ) {
        if (page > batchCount)
            return

        if (!isQueryValid(query))
            return

        val myService = MyHttpHelper()
        myService.run("KakaoAK $apiKey", query, page, object:MyNetworkCallbacks<KeywordSearchResponse>{
            override fun onResponse(response: KeywordSearchResponse) {
                val result = responseToResultArray(response)
                if (lastSearchId != searchId) {
                    return
                }
                onResponse?.invoke(result)
                if (!response.meta.is_end) {
                    batchSearchByKeyword(
                        searchId,
                        query,
                        apiKey,
                        page + 1,
                        batchCount,
                        onResponse
                    )
                }
            }

            override fun onError(errorMessage: String) {
                Log.e("KSC", "request failed")
                Log.e("KSC", "Message: $errorMessage")
            }

            override fun onTimeout() {
                Log.e("KSC", "connection timeout")
            }

        })
    }
}