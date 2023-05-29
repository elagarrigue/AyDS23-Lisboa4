package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import wikipedia.external.external.WikipediaArticleService

const val  WIKIPEDIA_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"


class WikipediaProxy(
    private val wikipediaService: WikipediaArticleService
): Proxy {
    override fun getArtistBiography(artistName: String): Card? {
        var card: Card? = null
        try{
            val wikipediaArtist = wikipediaService.getArtist(artistName)
            if (wikipediaArtist != null)
            {
                card = Card(
                    wikipediaArtist.artistInfo,
                    wikipediaArtist.wikipediaUrl,
                    "Wikipedia",
                    WIKIPEDIA_LOGO,
                    wikipediaArtist.isInDataBase,
                )
            }
        } catch (e: Exception){
            card = null
        }

        return card
    }
}