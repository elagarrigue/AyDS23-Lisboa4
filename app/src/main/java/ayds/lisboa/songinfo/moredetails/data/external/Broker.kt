package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.data.external.proxys.Proxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Broker{
        fun getCards(artistName: String): List<Card>
}
internal class BrokerImp(
        private val proxyCollection: List<Proxy>
): Broker
{
        override fun getCards(artistName: String): List<Card> {
                val cards = mutableListOf<Card>()
                proxyCollection.forEach {
                        val card = it.getArtistBiography(artistName)
                        if(card != null) {
                                cards.add(card)
                        }
                }
                return cards
        }
}