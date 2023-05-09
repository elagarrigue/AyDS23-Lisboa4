package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.BiographyRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography
import ayds.observer.Observable
import ayds.observer.Subject

private const val PREFIX = "[*]"
private const val DEFAULT_STRING = ""
interface OtherInfoPresenter {

    val uiStateObservable: Observable<OtherInfoUiState>

    fun searchArtistBiography(artistName: String)
}
class OtherInfoPresenterImpl (private val biographyRepository: BiographyRepository): OtherInfoPresenter
{
    override val uiStateObservable = Subject<OtherInfoUiState>()

    override fun searchArtistBiography(artistName: String) {
        val artistBiography = biographyRepository.getArtistBiography(artistName)
        when(artistBiography){
            is Biography.ArtistBiography -> updateUiState(artistBiography, artistName)
            is Biography.EmptyBiography -> updateNoResultsUiState()
        }
    }

    private fun updateUiState(artistBiography: ArtistBiography, artistName: String) {
        val formattedArtistInfo = getFormattedArtistInfo(artistBiography)
        val htmlArtistInfo = OtherInfoHtmlHelper.textToHtml(formattedArtistInfo, artistName)
        val uiState = OtherInfoUiState(
            htmlArtistInfo,
            artistBiography.url,
            OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        uiStateObservable.notify(uiState)
    }

    private fun updateNoResultsUiState() {
        val emptyUiState = OtherInfoUiState(
                DEFAULT_STRING,
                DEFAULT_STRING,
                OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        uiStateObservable.notify(emptyUiState)
    }


    private fun getFormattedArtistInfo(artistBiography: ArtistBiography): String {
        val prefix =
            if (artistBiography.isLocallyStored)
                PREFIX
            else
                DEFAULT_STRING
        return "$prefix${artistBiography.artistInfo}"
    }
}