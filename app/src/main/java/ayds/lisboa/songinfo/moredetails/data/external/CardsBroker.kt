package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.data.external.proxys.CardProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardsBroker {
    fun getCards(artistName: String): List<Card>
}

internal class CardsBrokerImp(
    private val proxyCollection: List<CardProxy>
) : CardsBroker {
    override fun getCards(artistName: String): List<Card> {
        val cards = mutableListOf<Card>()
        proxyCollection.forEach {
            val card = it.getCard(artistName)
            if (card != null) {
                cards.add(card)
            }
        }
        return cards
    }
}