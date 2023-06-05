package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository

internal class CardRepositoryImpl(
    private val CardLocalStorage: LocalStorage,
    private val CardBroker: Broker
): CardRepository {

    override fun getArtistBiography(artistName: String): List<Card> {
        var cards = CardLocalStorage.getArtistCards(artistName)
        if (cards.isEmpty()) {
            cards = CardBroker.getCards(artistName)
            if (cards.isNotEmpty()) {
                saveAllCards(artistName, cards)
            }
        }

        return cards
    }

    private fun saveAllCards(artistName: String, cards: List<Card>)
    {
        cards.forEach{
            CardLocalStorage.saveArtistCard(artistName, it)
        }
    }
}



