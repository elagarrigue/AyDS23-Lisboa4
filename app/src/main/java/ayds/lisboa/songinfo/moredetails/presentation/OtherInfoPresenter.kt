package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.ArtistBiography
import ayds.observer.Observable
import ayds.observer.Subject

private const val PREFIX = "[*]"
private const val DEFAULT_STRING = ""
interface OtherInfoPresenter {

    val uiStateObservable: Observable<OtherInfoUiState>

    fun searchArtistBiography(artistName: String)
}
internal class OtherInfoPresenterImpl (
    private val biographyRepository: BiographyRepository,
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper
) : OtherInfoPresenter {

    override val uiStateObservable = Subject<OtherInfoUiState>()
    override fun searchArtistBiography(artistName: String) {
        Thread {
            searchArtistInfo(artistName)
        }.start()
    }

    private fun searchArtistInfo(artistName: String) {
        val artistBiography = biographyRepository.getArtistBiography(artistName)
        when(artistBiography){
            is Biography.ArtistBiography -> updateUiState(artistBiography, artistName)
            is Biography.EmptyBiography -> updateNoResultsUiState()
        }
    }

    private fun updateUiState(artistBiography: ArtistBiography, artistName: String) {
        val uiState = createUiState(artistBiography, artistName)
        uiStateObservable.notify(uiState)
    }

    private fun createUiState(artistBiography: ArtistBiography, artistName: String): OtherInfoUiState{
        val formattedArtistInfo = getFormattedArtistInfo(artistBiography)
        val htmlArtistInfo = otherInfoHtmlHelper.textToHtml(formattedArtistInfo, artistName)
        return OtherInfoUiState(
            htmlArtistInfo,
            artistBiography.url,
            OtherInfoUiState.URL_LAST_FM_IMAGE
        )
    }
    private fun getFormattedArtistInfo(artistBiography: ArtistBiography): String {
        val prefix =
            if (artistBiography.isLocallyStored)
                PREFIX
            else
                DEFAULT_STRING
        return "$prefix${artistBiography.artistInfo}"
    }

    private fun updateNoResultsUiState() {
        val emptyUiState = OtherInfoUiState(
                DEFAULT_STRING,
                DEFAULT_STRING,
                OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        uiStateObservable.notify(emptyUiState)
    }

}