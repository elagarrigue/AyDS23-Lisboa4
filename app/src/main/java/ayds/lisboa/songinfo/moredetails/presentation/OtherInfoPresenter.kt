package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import ayds.observer.Observable
import ayds.observer.Subject
import lisboa4LastFM.ArtistBiography.Companion.URL_LAST_FM_IMAGE as LOGO

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
        val cards = biographyRepository.getArtistBiography(artistName)

         when(cards.isEmpty())
         {
            false -> updateUiState(cards, artistName)
            true -> updateNoResultsUiState()
         }
    }

    private fun updateUiState(cards: MutableCollection<Card>, artistName: String) {
        var uiState = OtherInfoUiState()
        cards.forEach()
        {
            val cardUiSate = createCardUiState(
                it,
                artistName
            )
            uiState.CardsUiState.add(cardUiSate)
        }

        uiStateObservable.notify(uiState)
    }

    private fun createCardUiState(card: Card, artistName: String): CardUiState {
        val formattedArtistInfo = getFormattedArtistInfo(card)
        val htmlArtistInfo = otherInfoHtmlHelper.textToHtml(formattedArtistInfo, artistName)
        return CardUiState(
            htmlArtistInfo,
            card.infoUrl,
            card.sourceLogoUrl,
            card.source
        )
    }
    private fun getFormattedArtistInfo(card: Card): String {
        val prefix =
            if (card.isLocallyStored)
                PREFIX
            else
                DEFAULT_STRING
        return "$prefix${card.description}"
    }

    private fun updateNoResultsUiState() {
        val emptyUiState = OtherInfoUiState()
        emptyUiState.CardsUiState.add(CardUiState())
        uiStateObservable.notify(emptyUiState)
    }

}