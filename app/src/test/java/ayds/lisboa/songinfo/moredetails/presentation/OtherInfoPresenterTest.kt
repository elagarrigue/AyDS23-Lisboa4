package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val PREFIX = "[*]"

class OtherInfoPresenterImplTest {

    private val biographyRepository: BiographyRepository = mockk()
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper = mockk()

    private lateinit var otherInfoPresenter: OtherInfoPresenterImpl

    private val uiStateSubject: Subject<OtherInfoUiState> = Subject()

    @Before
    fun setup() {
        otherInfoPresenter = OtherInfoPresenterImpl(biographyRepository, otherInfoHtmlHelper)
        every { otherInfoHtmlHelper.textToHtml(any(), any()) } returns "formattedHtml"
        every { biographyRepository.getArtistBiography(any()) } returns Biography.ArtistBiography(
            "artistInfo",
            "url",
            true
        )
    }

    @Test
    fun `searchArtistBiography should update UI state with artist biography`() {
        val artistName = "artist"

        otherInfoPresenter.searchArtistBiography(artistName)

        val expectedUiState = OtherInfoUiState(
            artistInfoHTML = "formattedHtml",
            artistUrl = "url",
            lastFMImage = OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        val actualUiState = uiStateSubject.lastValue()
        assertEquals(expectedUiState, actualUiState)
    }

    @Test
    fun `searchArtistBiography with empty biography should update UI state with empty state`() {
        val artistName = "artist"
        every { biographyRepository.getArtistBiography(artistName) } returns Biography.EmptyBiography

        otherInfoPresenter.searchArtistBiography(artistName)

        val expectedUiState = OtherInfoUiState(
            artistInfoHTML = "",
            artistUrl = "",
            lastFMImage = OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        val actualUiState = uiStateSubject.lastValue()
        assertEquals(expectedUiState, actualUiState)
    }

    @Test
    fun `searchArtistBiography should call getFormattedArtistInfo and textToHtml`() {
        val artistName = "artist"

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { otherInfoHtmlHelper.textToHtml("[$PREFIX]artistInfo", artistName) }
    }
}
