package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository

class BiographyRepositoryImpl(
    private val localStorage: LocalStorage,
    private val broker: Broker
): BiographyRepository {

    override fun getArtistBiography(artistName: String): MutableCollection<Card> {
        var cards = localStorage.getArtistCards(artistName)
        if (cards.isEmpty()) {
            try {
                cards = broker.getCards(artistName)
                if (cards.isNotEmpty())
                {
                    saveAllCards(artistName, cards)
                }
            }
            catch (e: Exception) {

            }
        }
        return cards
    }

    private fun saveAllCards(artistName: String, cards: MutableCollection<Card>)
    {
        cards.forEach{
            localStorage.saveArtistCard(artistName, it)
        }
    }
}



