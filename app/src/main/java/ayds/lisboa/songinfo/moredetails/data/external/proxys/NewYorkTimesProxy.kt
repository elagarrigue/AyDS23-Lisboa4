package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

const val NYT_IMAGE = "https: //encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class NewYorkTimesProxy(
    private val newYorkTimesService: NYTArtistInfoService
): Proxy {
    override fun getArtistBiography(artistName: String): Card? {
        var card: Card? = null
        var newYorkTimesArtistInfo: ArtistInformationExternal? = try{
            newYorkTimesService.getArtistInfo(artistName)
        }catch (e: Exception){
            null
        }
        if (newYorkTimesArtistInfo != null)
        {
            if(newYorkTimesArtistInfo is ArtistInformationExternal.ArtistInformationDataExternal) {
                card = Card(
                    newYorkTimesArtistInfo.abstract.toString(),
                    newYorkTimesArtistInfo.url.toString(),
                    "NewYorkTimes",
                    NYT_IMAGE,
                    newYorkTimesArtistInfo.isLocallyStored
                )
            }
        }
        return card
    }
}