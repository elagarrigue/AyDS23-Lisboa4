package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.text.Html
import ayds.lisboa.songinfo.home.model.repository.SongRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.BiographyRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography
import ayds.observer.Observable
import ayds.observer.Subject

private const val PREFIX = "[*]"

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
            is Biography.ArtistBiography -> updateUiState(artistBiography)
            is Biography.EmptyBiography -> updateNoResultsUiState()
        }
    }

    private fun updateUiState(artistBiography: Biography.ArtistBiography) {

    }

    private fun updateNoResultsUiState() {
        val  emptyUiState (
                    DEFAULT_STRING,
                    DEFAULT_STRING,
                    ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoUiState.URL_LAST_FM_IMAGE
        )
    }


    private fun getFormattedArtistInfo(artistBiography: ayds.lisboa.songinfo.moredetails.fulllogic.Biography): String {
        val prefix =
            if (artistBiography.isLocallyStored)
                PREFIX
            else
                ayds.lisboa.songinfo.moredetails.fulllogic.DEFAULT_STRING
        return "$prefix${artistBiography.artistInfo}"
    }

    private fun loadArtistInfo(artistInfo: String) {
        artistInfoTextView.text = Html.fromHtml(artistInfo)
    }

}