package ayds.lisboa.songinfo.moredetails.fulllogic.data.external

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject

private const val JSON_ARTIST = "artist"

interface LastFMService {
    fun getArtistBiography(artistName: String): ArtistBiography?
}

internal class LastFMServiceImpl(
    private val lastFMAPIToBiographyResolver: LastFMAPIToBiographyResolver,
    private val lastFMAPI: LastFMAPI
): LastFMService {

    override fun getArtistBiography(artistName: String): ArtistBiography? {
        val artist = getArtistFromLastFMAPI(artistName)
        val artistInfo = lastFMAPIToBiographyResolver.getArtistInfoFromJsonResponse(artist)
        val artistUrl = lastFMAPIToBiographyResolver.getArtistUrl(artist)
        return ArtistBiography(artistInfo, artistUrl, false)
    }

    private fun getArtistFromLastFMAPI(artistName: String): JsonObject {
        val callLastAPIResponse = lastFMAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callLastAPIResponse.body(), JsonObject::class.java)
        return jobj[JSON_ARTIST].asJsonObject
    }
}

