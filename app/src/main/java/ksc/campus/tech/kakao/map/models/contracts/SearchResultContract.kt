package ksc.campus.tech.kakao.map.models.contracts

import android.provider.BaseColumns._ID

object SearchResultContract {
    const val TABLE_NAME = "SEARCH_RESULT"
    const val COLUMN_NAME = "name"
    const val COLUMN_ADDRESS = "address"
    const val COLUMN_TYPE = "type"

    const val CREATE_QUERY = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_NAME TEXT, " +
            "$COLUMN_ADDRESS TEXT, " +
            "$COLUMN_TYPE TEXT)"

    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
}