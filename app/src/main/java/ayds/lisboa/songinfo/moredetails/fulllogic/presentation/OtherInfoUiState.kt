package ayds.lisboa.songinfo.moredetails.fulllogic.presentation;

private const val DEFAULT_STRING = ""

data class OtherInfoUiState (
    val artistInfoHTML :String = DEFAULT_STRING,
    val  artistUrl: String = DEFAULT_STRING,
    val lastFMImage:String =URL_LAST_FM_IMAGE
    ){

    companion object {
        const val URL_LAST_FM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

}
