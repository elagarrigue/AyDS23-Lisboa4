package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.data.external.proxys.Proxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Broker{
        fun getCards(artistName: String): MutableCollection<Card>
}
internal class BrokerImp(
        private val proxyCollection: MutableCollection<Proxy>
): Broker
{
        override fun getCards(artistName: String): MutableCollection<Card> {
                val cards: MutableCollection<Card> = mutableListOf()
                proxyCollection.forEach {
                        val card = it.getArtistBiography(artistName)
                        if(card != null) {
                                cards.add(card)
                        }
                }
                return cards
        }
}