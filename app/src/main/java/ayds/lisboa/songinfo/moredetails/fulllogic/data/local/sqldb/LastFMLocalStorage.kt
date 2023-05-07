package ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

interface LastFMLocalStorage {
    fun saveArtist(artist: String, artistBiography: ArtistBiography)
    fun getArtistInfo(artist: String): ArtistBiography?
}

internal class LastFMLocalStorageImpl(
    context: Context,
    private val cursorToArtistMapper: CursorToArtistMapper,
    ) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    LastFMLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        URL_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artistName: String, artistBiography: ArtistBiography) {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artistName)
            put(INFO_COLUMN, artistBiography.artistInfo)
            put(SOURCE_COLUMN, 1)
            put(URL_COLUMN, artistBiography.url)
        }
        writableDatabase?.insert(ARTISTS_TABLE, null, values)
    }

    override fun getArtistInfo(artist: String): ArtistBiography? {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$ARTIST_COLUMN  = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN DESC"
        )
        return cursorToArtistMapper.map(cursor)
    }
}