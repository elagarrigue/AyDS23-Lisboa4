package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import lisboa4LastFM.ArtistBiography.Companion.URL_LAST_FM_IMAGE as LOGO

class BiographyRepositoryImpl(
    private val lastFmLocalStorage: LastFMLocalStorage,
    private val broker: Broker
): BiographyRepository {

    override fun getArtistBiography(artistName: String): MutableCollection<Card> {
        var cards = lastFmLocalStorage.getArtistCards(artistName)
        if (cards.isEmpty()) {
            try {
                val cards = broker.getCards(artistName)
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

    private fun saveAllCards(artistName: String, cards: Collection<Card>)
    {
        cards.forEach()
        {
            lastFmLocalStorage.saveArtistCard(artistName, it)
        }
    }
}



