package ksc.campus.tech.kakao.map.models.mynetwork

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ksc.campus.tech.kakao.map.models.dto.KeywordSearchResponse
import retrofit2.Call
import kotlin.concurrent.thread

interface MyNetworkCallbacks<T> {
    fun onResponse(response: T)

    fun onError(errorMessage: String)

    fun onTimeout()
}


class MyNetworkService {
    private fun deserializeJson(jsonString: String): KeywordSearchResponse =
        Json.decodeFromString<KeywordSearchResponse>(jsonString)

    fun run(
        kakaoAuthCode: String,
        searchQuery: String,
        page: Int,
        networkCallback: MyNetworkCallbacks<KeywordSearchResponse>
    ) {
        thread {
            startConnection(kakaoAuthCode, searchQuery, page, networkCallback)
        }
    }

    private fun startConnection(
        kakaoAuthCode: String,
        searchQuery: String,
        page: Int,
        networkCallback: MyNetworkCallbacks<KeywordSearchResponse>
    ) {
        val connection = MyNetworkConnection(KAKAO_DOMAIN, KAKAO_PATH)
        connection.addRequestHeader(KEY_AUTH, kakaoAuthCode)
        connection.addRequestProperty(KEY_QUERY, searchQuery)
        connection.addRequestProperty(KEY_PAGE, page.toString())
        connection.generateHttpConnection()

        val response = connection.getHttpResponse()

        if (response.isTimeout) {
            networkCallback.onTimeout()
            return
        } else if (!response.isSuccess) {
            networkCallback.onError(response.body)
        } else {
            val data = deserializeJson(response.body)
            networkCallback.onResponse(data)
        }

    }

    companion object {
        private const val KAKAO_DOMAIN = "https://dapi.kakao.com/"
        private const val KAKAO_PATH = "v2/local/search/keyword.json"

        private const val KEY_AUTH = "Authorization"
        private const val KEY_QUERY = "query"
        private const val KEY_PAGE = "page"
    }
}