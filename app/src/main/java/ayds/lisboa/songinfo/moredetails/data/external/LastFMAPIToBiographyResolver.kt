package ayds.lisboa.songinfo.moredetails.data.external

import com.google.gson.JsonElement
import com.google.gson.JsonObject

private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"
private const val NEW_LINE = "\n"
private const val ESCAPED_NEW_LINE = "\\n"
private const val DEFAULT_STRING = ""

internal class LastFMAPIToBiographyResolver {

    fun getArtistInfoFromJsonResponse(artist: JsonObject): String {
        val bioContent = getBioContent(artist)
        return bioContent?.asString?.replace(ESCAPED_NEW_LINE, NEW_LINE) ?: DEFAULT_STRING
    }

    private  fun getBioContent(artist: JsonObject): JsonElement?{
        val bio = artist[JSON_BIO].asJsonObject
        return bio[JSON_CONTENT]
    }

    fun getArtistUrl(artist: JsonObject): String {
        return artist[JSON_URL].asString
    }
}