package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

private const val PREFIX = "[*]"

class OtherInfoPresenterImplTest {

    private val cardRepository: CardRepository = mockk()
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper = mockk()
    private val sourceEnumHelper: SourceEnumHelper = mockk()

    private val otherInfoPresenter: OtherInfoPresenter by lazy {
        OtherInfoPresenterImpl(cardRepository, otherInfoHtmlHelper, sourceEnumHelper)
    }

    @Test
    fun `searchArtistCards with artistCards should update UI state with not stored artist cards `() {
        val artistName = "artist"
        val cards = mutableListOf<Card>()
        val card = Card(
            "artistInfo",
            "url",
            Source.LastFm,
            "logo",
            false
        )
        cards.add(card)
        every { cardRepository.getCards(artistName) } returns cards
        every { otherInfoHtmlHelper.textToHtml("artistInfo", artistName) } returns "formattedHtml"
        every { sourceEnumHelper.sourceEnumToString(Source.LastFm) } returns "Last FM"

        val cardUiState = CardUiState(
            "formattedHtml",
            "url",
            "logo",
            "Last FM"
        )
        val cardsUiState = mutableListOf<CardUiState>()
        cardsUiState.add(cardUiState)

        val expectedUiState = OtherInfoUiState(cardsUiState)

        val uiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiStateObservable.subscribe {
            uiStateTester(it)
        }

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { uiStateTester(expectedUiState) }
    }
    @Test
    fun `searchArtistCards with artistCards should update UI state with artist cards`() {
        val artistName = "artist"
        val cards = mutableListOf<Card>()
        val card = Card(
            "artistInfo",
            "url",
            Source.LastFm,
            "logo",
            true
        )
        cards.add(card)
        every { cardRepository.getCards(artistName) } returns cards
        every { otherInfoHtmlHelper.textToHtml("${PREFIX}artistInfo", artistName) } returns "formattedHtml"
        every { sourceEnumHelper.sourceEnumToString(Source.LastFm) } returns "Last FM"

        val cardUiState = CardUiState(
            "formattedHtml",
            "url",
            "logo",
            "Last FM"
        )
        val cardsUiState = mutableListOf<CardUiState>()
        cardsUiState.add(cardUiState)

        val expectedUiState = OtherInfoUiState(cardsUiState)

        val uiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiStateObservable.subscribe {
            uiStateTester(it)
        }

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { uiStateTester(expectedUiState) }
    }

    @Test
    fun `searchArtistCard with emptyCard should update UI state with empty state`() {
        val artistName = "artist"
        val cards = mutableListOf<Card>()
        every { cardRepository.getCards(artistName) } returns cards


        val cardsUiState = mutableListOf<CardUiState>()
        val expectedUiState = OtherInfoUiState(cardsUiState)

        val uiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiStateObservable.subscribe {
            uiStateTester(it)
        }

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { uiStateTester(expectedUiState) }
    }
}