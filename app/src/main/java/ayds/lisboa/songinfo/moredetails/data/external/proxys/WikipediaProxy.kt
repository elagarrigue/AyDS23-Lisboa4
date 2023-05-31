package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import wikipedia.external.external.WikipediaArticleService

const val  WIKIPEDIA_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"


internal class WikipediaProxy(
    private val wikipediaService: WikipediaArticleService
): Proxy {
    override fun getArtistBiography(artistName: String): Card? {
        var card: Card? = null
        try{
            val wikipediaArtist = wikipediaService.getArtist(artistName)
            card = Card(
                wikipediaArtist.artistInfo,
                wikipediaArtist.wikipediaUrl,
                Source.Wikipedia,
                WIKIPEDIA_LOGO,
                wikipediaArtist.isInDataBase,
            )
        } catch (e: Exception){
            card = null
        }

        return card
    }
}