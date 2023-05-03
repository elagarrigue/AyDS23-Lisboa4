package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.local.sqldb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.lisboa.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapper

internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToArtistMapper: CursorToArtistMapper,
    ) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {
    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}