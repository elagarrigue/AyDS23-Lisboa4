package ayds.lisboa.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CursorToCardMapper {
    fun map(cursor: Cursor): MutableCollection<Card>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): MutableCollection<Card> {
        var cards = mutableListOf<Card>()
        try {
            with(cursor) {
                if (moveToNext()) {
                    cards.add(Card(
                            getString(getColumnIndexOrThrow(INFO_COLUMN)),
                            getString(getColumnIndexOrThrow(URL_COLUMN)),
                            getString(getColumnIndexOrThrow(SOURCE_COLUMN)),
                            getString(getColumnIndexOrThrow(LOGO_URL_COLUMN)),
                        true)
                    )
                } else {
                    null
                }
            }
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            null
        }

        return cards
    }
}