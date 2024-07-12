package ksc.campus.tech.kakao.map.models.mynetwork

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class MyConnectionResponse(val body:String, val isSuccess:Boolean, val isTimeout:Boolean)

class MyNetworkConnection(private val domain:String, private val path:String) {
    private var encodedProperties: String = ""
    private val timeout: Int = 1000
    private lateinit var httpsConnection: HttpsURLConnection
    private var headers: MutableList<Pair<String, String>> = mutableListOf()

    init{
        generateHttpConnection()
    }

    fun generateHttpConnection() {
        try {
            val connectUrl: URL = URL(domain + path + "?$encodedProperties")
            val urlConnection = (connectUrl.openConnection() as HttpsURLConnection)
            urlConnection.connectTimeout = timeout
            urlConnection.requestMethod = "GET"
            httpsConnection = urlConnection

            for(h in headers){
                urlConnection.addRequestProperty(h.first, h.second)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addRequestProperty(key:String, value:String){
        encodedProperties += "&$key=$value"
    }

    fun addRequestHeader(key:String, value:String){
        headers.add(Pair(key, value))
    }

    fun getHttpResponse(): MyConnectionResponse{
        Log.d("KSC", httpsConnection.contentType)
        Log.d("KSC", httpsConnection.headerFields.toString())
        try {
            return if (httpsConnection.responseCode == 200) {
                val data = readFromResponseStream(httpsConnection.inputStream)
                MyConnectionResponse(data, isSuccess = true, isTimeout = false)
            } else {
                Log.e("KSC", httpsConnection.responseCode.toString())
                Log.e("KSC", httpsConnection.responseMessage.toString())
                val errorData = readFromResponseStream(httpsConnection.errorStream)
                Log.e("KSC", errorData)
                MyConnectionResponse(errorData, isSuccess = false, isTimeout = false)
            }
        }
        catch(e: IOException){
            e.printStackTrace()
        }
        catch(e: SocketTimeoutException){
            e.printStackTrace()
            return MyConnectionResponse("", isSuccess = false, isTimeout = true)
        }
        finally {
            httpsConnection.disconnect()
        }
        return MyConnectionResponse("", isSuccess = false, isTimeout = false)
    }

    private fun readFromResponseStream(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()
        var line:String?
        try {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            while (bufferedReader.readLine().also {
                    line = it
                } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}