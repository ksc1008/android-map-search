package ksc.campus.tech.kakao.map.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import ksc.campus.tech.kakao.map.models.contracts.SearchKeywordContract
import ksc.campus.tech.kakao.map.models.contracts.SearchResultContract

data class SearchResult(val id: String, val name: String, val address: String, val type: String)

class SearchDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SearchKeywordContract.CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db?.execSQL(SearchKeywordContract.CREATE_QUERY)
        }
        if (oldVersion in 2..2) {
            db?.execSQL(SearchKeywordContract.DROP_QUERY)
            db?.execSQL(SearchKeywordContract.CREATE_QUERY)
        }
        if(oldVersion in 1 .. 3){
            db?.execSQL(SearchResultContract.DROP_QUERY)
        }
    }

    fun insertOrReplaceKeyword(keyword: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(SearchKeywordContract.COLUMN_KEYWORD, keyword)
        }

        db.replace(SearchKeywordContract.TABLE_NAME, null, contentValues)
    }

    fun deleteKeyword(keyword: String) {
        val db = writableDatabase
        db.delete(
            SearchKeywordContract.TABLE_NAME,
            "${SearchKeywordContract.COLUMN_KEYWORD} = ?",
            arrayOf(keyword)
        )
    }

    private fun getAllSearchKeywordFromCursor(cursor: Cursor?): List<String> {
        val result = mutableListOf<String>()

        try {
            while (cursor?.moveToNext() == true) {
                result.add(cursor.getString(1))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }

        return result
    }

    fun queryAllSearchKeywords(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${SearchKeywordContract.TABLE_NAME}", null)

        return getAllSearchKeywordFromCursor(cursor)
    }

    companion object {
        private var instance: SearchDbHelper? = null
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "MapSearch2"

        fun getInstance(context: Context): SearchDbHelper {
            if (instance == null) {
                instance = SearchDbHelper(context)
            }
            return instance as SearchDbHelper
        }
    }
}