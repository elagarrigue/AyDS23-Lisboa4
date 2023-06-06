package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

const val NYT_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

internal class NewYorkTimesProxy(
    private val newYorkTimesService: NYTArtistInfoService
): CardProxy {
    override fun getCard(artistName: String): Card? {
        var card: Card? = null
        var newYorkTimesArtistInfo: ArtistInformationExternal? = null
        try{
            newYorkTimesArtistInfo = newYorkTimesService.getArtistInfo(artistName)
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (newYorkTimesArtistInfo != null)
        {
            if(newYorkTimesArtistInfo is ArtistInformationExternal.ArtistInformationDataExternal) {
                card = artistInformationExternalToCard(newYorkTimesArtistInfo)
            }
        }
        return card
    }

    private fun artistInformationExternalToCard(newYorkTimesArtistInfo: ArtistInformationExternal.ArtistInformationDataExternal): Card = Card(
        newYorkTimesArtistInfo.abstract.toString(),
        newYorkTimesArtistInfo.url.toString(),
        Source.NewYorkTimes,
        NYT_IMAGE,
        newYorkTimesArtistInfo.isLocallyStored
    )

}