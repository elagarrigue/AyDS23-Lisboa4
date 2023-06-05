package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.external.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository

internal class CardRepositoryImpl(
    private val cardLocalStorage: LocalStorage,
    private val cardBroker: CardsBroker
): CardRepository {

    override fun getCards(artistName: String): List<Card> {
        var cards = cardLocalStorage.getArtistCards(artistName)
        if (cards.isEmpty()) {
            cards = cardBroker.getCards(artistName)
            if (cards.isNotEmpty()) {
                saveAllCards(artistName, cards)
            }
        }

        return cards
    }

    private fun saveAllCards(artistName: String, cards: List<Card>)
    {
        cards.forEach{
            cardLocalStorage.saveArtistCard(artistName, it)
        }
    }
}



