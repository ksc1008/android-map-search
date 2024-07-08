package ksc.campus.tech.kakao.map.models.contracts

import android.provider.BaseColumns

object SearchKeywordContract : BaseColumns {
    const val TABLE_NAME = "SEARCH_KEYWORD"
    const val COLUMN_KEYWORD = "keyword"

    const val CREATE_QUERY = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_KEYWORD TEXT," +
            "UNIQUE($COLUMN_KEYWORD))"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
}