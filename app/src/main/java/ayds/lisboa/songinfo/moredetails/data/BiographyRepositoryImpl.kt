package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository

class BiographyRepositoryImpl(
    private val localStorage: LocalStorage,
    private val broker: Broker
): BiographyRepository {

    override fun getArtistBiography(artistName: String): List<Card> {
        var cards = localStorage.getArtistCards(artistName)
        if (cards.isEmpty()) {
            cards = broker.getCards(artistName)
            if (cards.isNotEmpty()) {
                saveAllCards(artistName, cards)
            }
        }

        return cards
    }

    private fun saveAllCards(artistName: String, cards: List<Card>)
    {
        cards.forEach{
            localStorage.saveArtistCard(artistName, it)
        }
    }
}



