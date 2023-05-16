package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response


private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"
private const val NEW_LINE = "\n"
private const val ESCAPED_NEW_LINE = "\\n"
private const val DEFAULT_STRING = ""
private const val JSON_ARTIST = "artist"

internal class LastFMAPIToBiographyResolver {

    fun getArtistBiography(callResponse: Response<String>): ArtistBiography {
        val artist = getArtistFromCallResponse(callResponse)
        val artistInfo = getArtistInfoFromResponse(artist)
        val artistUrl = getArtistUrl(artist)
        return ArtistBiography(artistInfo, artistUrl, false)
    }
    private fun getArtistFromCallResponse(callLastAPIResponse: Response<String>): JsonObject {
        val gson = Gson()
        val jobj = gson.fromJson(callLastAPIResponse.body(), JsonObject::class.java)
        return jobj[JSON_ARTIST].asJsonObject
    }

    private fun getArtistInfoFromResponse(artist: JsonObject): String {
        val bioContent = getBioContent(artist)
        return bioContent?.asString?.replace(ESCAPED_NEW_LINE, NEW_LINE) ?: DEFAULT_STRING
    }

    private  fun getBioContent(artist: JsonObject): JsonElement?{
        val bio = artist[JSON_BIO].asJsonObject
        return bio[JSON_CONTENT]
    }

    private fun getArtistUrl(artist: JsonObject): String {
        return artist[JSON_URL].asString
    }
}