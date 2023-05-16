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
        return lastFMAPIToBiographyResolver.getArtistBiography(callResponse)
    }

    private fun getArtistFromLastFMAPI(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }
}

