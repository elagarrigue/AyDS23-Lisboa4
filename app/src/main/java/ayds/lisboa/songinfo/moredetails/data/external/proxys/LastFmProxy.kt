package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService

class LastFmProxy(
    private val lastFMService: LastFMService
): Proxy {
    override fun getArtistBiography(artistName: String): Card? {
        var card: Card? = null
        val lastFmBiography = lastFMService.getArtistBiography(artistName)
        if (lastFmBiography != null)
        {
            card = Card(
                lastFmBiography.artistInfo,
                lastFmBiography.url,
                "LastFm",
                ArtistBiography.URL_LAST_FM_IMAGE,
                lastFmBiography.isLocallyStored
            )
        }
        return card
    }
}