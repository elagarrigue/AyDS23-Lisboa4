package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

private const val PREFIX = "[*]"
private const val DEFAULT_STRING = ""
interface OtherInfoPresenter {

    val uiStateObservable: Observable<OtherInfoUiState>

    fun searchArtistBiography(artistName: String)
}
internal class OtherInfoPresenterImpl (
    private val biographyRepository: CardRepository,
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper,
    private val sourceEnumHelper: SourceEnumHelper
) : OtherInfoPresenter {

    override val uiStateObservable = Subject<OtherInfoUiState>()
    override fun searchArtistBiography(artistName: String) {
        Thread {
            searchArtistInfo(artistName)
        }.start()
    }

     private fun searchArtistInfo(artistName: String) {
         val cards = biographyRepository.getCards(artistName)
         if(cards.isEmpty())
             updateNoResultsUiState()
         else {
             updateUiState(cards, artistName)
         }
    }

    private fun updateUiState(cards: List<Card>, artistName: String) {
        val cardsUiState = mutableListOf<CardUiState>()
        cards.forEach()
        {
            val cardUiSate = createCardUiState(it, artistName)
            cardsUiState.add(cardUiSate)
        }
        val uiState = OtherInfoUiState(cardsUiState)
        uiStateObservable.notify(uiState)
    }

    private fun createCardUiState(card: Card, artistName: String): CardUiState {
        val formattedArtistInfo = getFormattedArtistInfo(card)
        val htmlArtistInfo = otherInfoHtmlHelper.textToHtml(formattedArtistInfo, artistName)
        return CardUiState(
            htmlArtistInfo,
            card.infoUrl,
            card.sourceLogoUrl,
            sourceEnumHelper.sourceEnumToString(card.source)
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
        val cardsUiState = mutableListOf<CardUiState>()
        val emptyUiState = OtherInfoUiState(cardsUiState)
        uiStateObservable.notify(emptyUiState)
    }

}