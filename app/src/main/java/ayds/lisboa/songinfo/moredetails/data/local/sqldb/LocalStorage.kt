package ayds.lisboa.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface LocalStorage {
    fun saveArtistCard(artist: String, card: Card)
    fun getArtistCards(artist: String): List<Card>
}

internal class LocalStorageImpl(
    context: Context,
    private val cursorToCardMapper: CursorToCardMapper,
    ) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    LocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        SOURCE_COLUMN,
        URL_COLUMN,
        LOGO_URL_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtistCard(artistName: String, card: Card) {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artistName)
            put(INFO_COLUMN, card.description)
            put(SOURCE_COLUMN, card.source.ordinal)
            put(URL_COLUMN, card.infoUrl)
            put(LOGO_URL_COLUMN, card.sourceLogoUrl)
        }
        writableDatabase?.insert(ARTISTS_TABLE, null, values)
    }

    override fun getArtistCards(artist: String): List<Card> {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$ARTIST_COLUMN  = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN DESC"
        )
        return cursorToCardMapper.map(cursor)
    }
}