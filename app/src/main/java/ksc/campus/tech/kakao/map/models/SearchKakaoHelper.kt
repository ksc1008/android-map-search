package ksc.campus.tech.kakao.map.models

import android.util.Log
import ksc.campus.tech.kakao.map.models.dto.KeywordSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.security.Key

interface KakaoSearchRetrofitService{
    @GET("/v2/local/search/keyword.json")
    fun requestSearchResultByKeyword(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<KeywordSearchResponse>
}

object SearchKakaoHelper{
    private const val KAKAO_LOCAL_URL = "https://dapi.kakao.com/"
    private var lastSearchedQuery: String = ""

    private fun isQueryValid(query: String):Boolean = query.isNotBlank()

    private fun getRetrofit(url: String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun parseCategory(category: String) = category.split('>').first().trim()

    private fun responseToResultArray(response: Response<KeywordSearchResponse>): List<SearchResult>{
        val result = mutableListOf<SearchResult>()

        for(doc in response.body()?.documents?: listOf()){
            result.add(SearchResult(doc.place_name, doc.address_name, parseCategory(doc.category_name)))
        }
        return result
    }

    private fun getResponse(query:String, call: Call<KeywordSearchResponse>, response: Response<KeywordSearchResponse>, onSuccess: ((results:List<SearchResult>) -> Unit)?) {
        // 새로운 검색어를 검색한 후 이전 검색 결과가 도착했을 경우, 무시
        if (query != lastSearchedQuery) {
            return
        }

        if(!response.isSuccessful){
            Log.e("KSC", "request failed")
            Log.e("KSC", "error message: ${response.message()}")
            Log.e("KSC", "error code: ${response.code()}")
            return
        }

        onSuccess?.invoke(responseToResultArray(response))
    }

    fun batchSearchByKeyword(query: String, apiKey:String, batchCount: Int, onResponse: ((results:List<SearchResult>) -> Unit)?){
        lastSearchedQuery = query
        val retrofit = getRetrofit(KAKAO_LOCAL_URL)
        val retrofitService = retrofit.create(KakaoSearchRetrofitService::class.java)
        retrofitService.requestSearchResultByKeyword("KakaoAK $apiKey", query, 1).enqueue(
            object: Callback<KeywordSearchResponse>{
                override fun onResponse(
                    call: Call<KeywordSearchResponse>,
                    response: Response<KeywordSearchResponse>
                ) {
                    getResponse(query, call, response, onResponse)
                }

                override fun onFailure(call: Call<KeywordSearchResponse>, p1: Throwable) {
                    if(call.isCanceled) {
                        Log.e("KSC", "request canceled")
                    }
                    if(call.isExecuted) {
                        Log.e("KSC", "request was executed but failed")
                    }
                    Log.e("KSC", "Message: ${p1.message}")
                }

            }
        )
    }
}