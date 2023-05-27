package ayds.lisboa.songinfo.moredetails.presentation
import lisboa4LastFM.ArtistBiography.Companion.URL_LAST_FM_IMAGE as LOGO

private const val DEFAULT_STRING = ""

data class CardUiState (
    val artistInfoHTML: String = DEFAULT_STRING,
    val artistUrl: String = DEFAULT_STRING,
    val imageUrl: String = LOGO,
    val source: String = "xd"
)
