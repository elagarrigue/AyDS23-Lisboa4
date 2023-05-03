package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.local.sqldb

import android.database.Cursor

interface CursorToArtistMapper {
    fun map(cursor: Cursor): String?
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun map(cursor: Cursor): String? =
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
}