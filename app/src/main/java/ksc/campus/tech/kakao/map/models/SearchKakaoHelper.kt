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

    private fun isResponseSuccess(query:String, response: Response<KeywordSearchResponse>): Boolean{
        if (query != lastSearchedQuery) {
            return false
        }
        if(!response.isSuccessful){
            Log.e("KSC", "request failed")
            Log.e("KSC", "error message: ${response.message()}")
            Log.e("KSC", "error code: ${response.code()}")
            return false
        }
        return true
    }

    fun batchSearchByKeyword(query: String, apiKey:String, batchCount: Int, onResponse: ((results:List<SearchResult>) -> Unit)?){
        batchSearchByKeyword(query, apiKey, 1, batchCount, onResponse)
    }

    private fun batchSearchByKeyword(query: String, apiKey:String, page:Int, batchCount: Int, onResponse: ((results:List<SearchResult>) -> Unit)?){
        if(page > batchCount)
            return

        lastSearchedQuery = query
        val retrofit = getRetrofit(KAKAO_LOCAL_URL)
        val retrofitService = retrofit.create(KakaoSearchRetrofitService::class.java)
        retrofitService.requestSearchResultByKeyword("KakaoAK $apiKey", query, 1).enqueue(
            object: Callback<KeywordSearchResponse>{
                override fun onResponse(
                    call: Call<KeywordSearchResponse>,
                    response: Response<KeywordSearchResponse>
                ) {
                    if(!isResponseSuccess(query, response)){
                        return
                    }
                    if(response.body()?.meta?.is_end == false){
                        batchSearchByKeyword(query, apiKey, page+1, batchCount, onResponse)
                    }
                    onResponse?.invoke(responseToResultArray(response))
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