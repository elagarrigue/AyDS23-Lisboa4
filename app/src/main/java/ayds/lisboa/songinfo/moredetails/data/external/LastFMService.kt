package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.ArtistBiography
import retrofit2.Response


interface LastFMService {
    fun getArtistBiography(artistName: String): ArtistBiography?
}

internal class LastFMServiceImpl(
    private val lastFMAPIToBiographyResolver: LastFMAPIToBiographyResolver,
    private val lastFMAPI: LastFMAPI
): LastFMService {

    override fun getArtistBiography(artistName: String): ArtistBiography? {
        val callResponse = getArtistFromLastFMAPI(artistName)
        val artist = lastFMAPIToBiographyResolver.getArtistFromCallResponse(callResponse)
        val artistInfo = lastFMAPIToBiographyResolver.getArtistInfoFromJsonResponse(artist)
        val artistUrl = lastFMAPIToBiographyResolver.getArtistUrl(artist)
        return ArtistBiography(artistInfo, artistUrl, false)
    }

    private fun getArtistFromLastFMAPI(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }
}

