package ayds.lisboa.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface CursorToCardMapper {
    fun map(cursor: Cursor): List<Card>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): List<Card> {
        var cards = mutableListOf<Card>()
        try {
            with(cursor) {
                while(moveToNext()) {
                    cards.add(Card(
                            getString(getColumnIndexOrThrow(INFO_COLUMN)),
                            getString(getColumnIndexOrThrow(URL_COLUMN)),
                            Source.values()[getInt(getColumnIndexOrThrow(SOURCE_COLUMN))],
                            getString(getColumnIndexOrThrow(LOGO_URL_COLUMN)),
                        true)
                    )
                }
            }
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            null
        }

        return cards
    }
}