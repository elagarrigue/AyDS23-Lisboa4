package ayds.lisboa.songinfo.moredetails.presentation

private const val DEFAULT_STRING = ""

data class CardUiState (
    val artistInfoHTML: String = DEFAULT_STRING,
    val artistUrl: String = DEFAULT_STRING,
    val imageUrl: String = DEFAULT_STRING,
    val source: String = DEFAULT_STRING
)
