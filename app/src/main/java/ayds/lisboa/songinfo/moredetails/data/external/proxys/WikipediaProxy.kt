package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WIKIPEDIA_LOGO

internal class WikipediaProxy(
    private val wikipediaService: WikipediaArticleService
): Proxy {
    override fun getArtistBiography(artistName: String): Card? {
        val card: Card? = try{
            val wikipediaArtist = wikipediaService.getArtist(artistName)
            Card(
                wikipediaArtist.artistInfo,
                wikipediaArtist.wikipediaUrl,
                Source.Wikipedia,
                WIKIPEDIA_LOGO,
                wikipediaArtist.isInDataBase,
            )
        } catch (e: Exception){
            null
        }
        return card
    }
}