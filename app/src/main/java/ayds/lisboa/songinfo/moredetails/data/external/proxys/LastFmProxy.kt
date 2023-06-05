package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService

internal class LastFmProxy(
    private val lastFMService: LastFMService
): CardProxy {
    override fun getCard(artistName: String): Card? {
        var card: Card? = null
        val lastFmBiography = lastFMService.getArtistBiography(artistName)
        if (lastFmBiography != null)
        {
            card = artistBiographyToCard(lastFmBiography)
        }
        return card
    }

    private fun artistBiographyToCard(lastFmBiography: ArtistBiography): Card = Card(
        lastFmBiography.artistInfo,
        lastFmBiography.url,
        Source.LastFm,
        ArtistBiography.URL_LAST_FM_IMAGE,
        lastFmBiography.isLocallyStored
    )

}