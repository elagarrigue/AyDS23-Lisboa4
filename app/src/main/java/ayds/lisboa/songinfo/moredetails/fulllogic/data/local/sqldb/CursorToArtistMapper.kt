package ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.fulllogic.data.Biography.ArtistBiography

interface CursorToArtistMapper {
    fun map(cursor: Cursor): ArtistBiography?
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun map(cursor: Cursor): ArtistBiography? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistBiography(
                        getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        getString(getColumnIndexOrThrow(URL_COLUMN)),
                        true
                    )
                } else {
                    null
                }
            }
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            null
        }
}