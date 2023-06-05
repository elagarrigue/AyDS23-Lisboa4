package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WIKIPEDIA_LOGO
import wikipedia.external.external.entities.WikipediaArtist

internal class WikipediaProxy(
    private val wikipediaService: WikipediaArticleService
): CardProxy {
    override fun getArtistBiography(artistName: String): Card? {
        val card: Card? = try{
            val wikipediaArtist = wikipediaService.getArtist(artistName)
            wikipediaArtistToCard(wikipediaArtist)
        } catch (e: Exception){
            null
        }
        return card
    }

    private fun wikipediaArtistToCard(wikipediaArtist: WikipediaArtist) = Card(
        wikipediaArtist.artistInfo,
        wikipediaArtist.wikipediaUrl,
        Source.Wikipedia,
        WIKIPEDIA_LOGO,
        wikipediaArtist.isInDataBase,
    )
}