package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import lisboa4LastFM.LastFMService
import lisboa4LastFM.ArtistBiography.Companion.URL_LAST_FM_IMAGE as LOGO

class BiographyRepositoryImpl(
    private val lastFmLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
): BiographyRepository {

    override fun getArtistBiography(artistName: String): MutableCollection<Card> {
        var artistBiography = lastFmLocalStorage.getArtistCards(artistName)
        if (artistBiography.isEmpty()) {
            try {
                artistBiography = mutableListOf<Card>()
                val lastFmBiography = lastFMService.getArtistBiography(artistName)
                if (lastFmBiography != null)
                {
                    artistBiography.add(Card(
                        lastFmBiography.artistInfo,
                        lastFmBiography.url,
                        "LastFm",
                        LOGO,
                        lastFmBiography.isLocallyStored
                        )
                    )
                }
                if(artistBiography != null)
                {
                    saveAllCards(artistName, artistBiography)
                }
            }
            catch (e: Exception) {
            }
        }

        return artistBiography
    }

    private fun saveAllCards(artistName: String, cards: Collection<Card>)
    {
        cards.forEach()
        {
            lastFmLocalStorage.saveArtistCard(artistName, it)
        }
    }
}



