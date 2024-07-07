package campus.tech.kakao.map.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import campus.tech.kakao.map.models.contracts.SearchKeywordContract
import campus.tech.kakao.map.models.contracts.SearchResultContract

data class SearchResult(val name: String, val address: String, val type: String)

class SearchDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SearchResultContract.CREATE_QUERY)

        if (db != null) {
            insertInitialData(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db?.execSQL(SearchKeywordContract.CREATE_QUERY)
        }
        if (oldVersion in 2..2) {
            db?.execSQL(SearchKeywordContract.DROP_QUERY)
            db?.execSQL(SearchKeywordContract.CREATE_QUERY)
        }
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val dataList = InitialDataset.initialData
        for (data in dataList) {
            val contentValues = ContentValues().apply {
                put(SearchResultContract.COLUMN_NAME, data.name)
                put(SearchResultContract.COLUMN_ADDRESS, data.address)
                put(SearchResultContract.COLUMN_TYPE, data.type)
            }
            db.insert(SearchResultContract.TABLE_NAME, null, contentValues)
        }
    }

    fun insertSearchResult(name: String, address: String, type: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(SearchResultContract.COLUMN_NAME, name)
            put(SearchResultContract.COLUMN_ADDRESS, address)
            put(SearchResultContract.COLUMN_TYPE, type)
        }
        db.insert(SearchResultContract.TABLE_NAME, null, contentValues)
    }

    fun insertSearchResult(searchResult: SearchResult) {
        insertSearchResult(searchResult.name, searchResult.address, searchResult.type)
    }

    fun insertOrReplaceKeyword(keyword: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(SearchKeywordContract.COLUMN_KEYWORD, keyword)
        }

        db.replace(SearchKeywordContract.TABLE_NAME, null, contentValues)
    }

    fun updateSearchResult(id: String, name: String, address: String, type: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(BaseColumns._ID, id)
            put(SearchResultContract.COLUMN_NAME, name)
            put(SearchResultContract.COLUMN_ADDRESS, address)
            put(SearchResultContract.COLUMN_TYPE, type)
        }
        db.update(
            SearchResultContract.TABLE_NAME,
            contentValues,
            "${BaseColumns._ID} = ?",
            arrayOf(id)
        )
    }

    fun updateSearchResult(id: String, searchResult: SearchResult) {
        updateSearchResult(id, searchResult.name, searchResult.address, searchResult.type)
    }

    fun deleteSearchResult(id: String) {
        val db = writableDatabase
        db.delete(SearchResultContract.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id))
    }

    fun deleteKeyword(keyword: String) {
        val db = writableDatabase
        db.delete(
            SearchKeywordContract.TABLE_NAME,
            "${SearchKeywordContract.COLUMN_KEYWORD} = ?",
            arrayOf(keyword)
        )
    }

    private fun getAllSearchResultFromCursor(cursor: Cursor?): List<SearchResult> {
        val result = mutableListOf<SearchResult>()

        try {
            while (cursor?.moveToNext() == true) {
                result.add(
                    SearchResult(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                    )
                )
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

    fun queryAllSearchResults(): List<SearchResult> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${SearchResultContract.TABLE_NAME}", null)

        return getAllSearchResultFromCursor(cursor)
    }

    fun querySearchResultsByName(name: String): List<SearchResult> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${SearchResultContract.TABLE_NAME} WHERE ${SearchResultContract.COLUMN_NAME} LIKE \"%$name%\"",
            null
        )

        return getAllSearchResultFromCursor(cursor)
    }

    fun queryAllSearchKeywords(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${SearchKeywordContract.TABLE_NAME}", null)

        return getAllSearchKeywordFromCursor(cursor)
    }

    companion object {
        private var instance: SearchDbHelper? = null
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "MapSearch"

        fun getInstance(context: Context): SearchDbHelper {
            if (instance == null) {
                instance = SearchDbHelper(context)
            }
            return instance as SearchDbHelper
        }
    }
}