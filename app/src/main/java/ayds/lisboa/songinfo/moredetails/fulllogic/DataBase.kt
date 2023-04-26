package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val ARTISTS_TABLE = "artists"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"

class DataBase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        val query: String =
            "create table $ARTISTS_TABLE (" +
                    "$ID_COLUMN integer PRIMARY KEY AUTOINCREMENT, " +
                    "$ARTIST_COLUMN string, " +
                    "$INFO_COLUMN string, " +
                    "$SOURCE_COLUMN integer)"
        db.execSQL(query)
    }

    fun saveArtist(artist: String, info: String) {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artist)
            put(INFO_COLUMN, info)
            put(SOURCE_COLUMN, 1)
        }
        writableDatabase?.insert(ARTISTS_TABLE, null, values)
    }

    fun getArtistInfo(artist: String): String? {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$ARTIST_COLUMN  = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN DESC"
        )
        return cursorToInfoColumn(cursor)
    }

    private fun cursorToInfoColumn(cursor: Cursor): String? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    getString(getColumnIndexOrThrow(INFO_COLUMN))
                } else {
                    null
                }
            }
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            null
        }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}