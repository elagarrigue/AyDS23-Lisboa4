package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.ArtistBiography
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.EmptyBiography
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

private const val PREFIX = "[*]"

class OtherInfoPresenterImplTest {

    private val biographyRepository: BiographyRepository = mockk()
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper = mockk()

    private val otherInfoPresenter: OtherInfoPresenterImpl by lazy {
        OtherInfoPresenterImpl(biographyRepository, otherInfoHtmlHelper)
    }

    @Test
    fun `searchArtistBiography with artistBiography should update UI state with artist biography`() {
        val artistName = "artist"
        every { biographyRepository.getArtistBiography(artistName) } returns ArtistBiography(
            "artistInfo",
            "url",
            true
        )
        every { otherInfoHtmlHelper.textToHtml(any(), any()) } returns "formattedHtml"

        otherInfoPresenter.searchArtistInfo(artistName)

        val expectedUiState = OtherInfoUiState(
            "formattedHtml",
            "url",
            OtherInfoUiState.URL_LAST_FM_IMAGE
            )

        val actualUiState = otherInfoPresenter.uiStateObservable.lastValue()
        assertEquals(expectedUiState, actualUiState)
    }

    @Test
    fun `searchArtistBiography with emptyBiography should update UI state with empty state`() {
        val artistName = "artist"
        every { biographyRepository.getArtistBiography(artistName) } returns EmptyBiography

        otherInfoPresenter.searchArtistInfo(artistName)

        val expectedUiState = OtherInfoUiState(
            artistInfoHTML = "",
            artistUrl = "",
            lastFMImage = OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        val actualUiState = otherInfoPresenter.uiStateObservable.lastValue()
        assertEquals(expectedUiState, actualUiState)
    }

    @Test
    fun `searchArtistBiography with artistBiography should call getFormattedArtistInfo and textToHtml`() {
        val artistName = "artist"
        every { biographyRepository.getArtistBiography(artistName) } returns ArtistBiography(
            "artistInfo",
            "url",
            true
        )
        every { otherInfoHtmlHelper.textToHtml(any(), any()) } returns "formattedHtml"

        otherInfoPresenter.searchArtistInfo(artistName)

        verify { otherInfoHtmlHelper.textToHtml("${PREFIX}artistInfo", artistName) }
    }
}
