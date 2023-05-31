package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
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
         val cards = biographyRepository.getArtistBiography(artistName)
         if(cards.isEmpty())
             updateNoResultsUiState()
         else {
             updateUiState(cards, artistName)
         }
    }

    private fun updateUiState(cards: MutableCollection<Card>, artistName: String) {
        var cardsUiState: MutableCollection<CardUiState> = mutableListOf()
        cards.forEach()
        {
            val cardUiSate = createCardUiState(
                it,
                artistName
            )
            cardsUiState.add(cardUiSate)
        }
        var uiState = OtherInfoUiState(cardsUiState)
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
        var cardsUiState: MutableCollection<CardUiState> = mutableListOf()
        val emptyUiState = OtherInfoUiState(cardsUiState)
        emptyUiState.cardsUiState.add(CardUiState())
        uiStateObservable.notify(emptyUiState)
    }

}