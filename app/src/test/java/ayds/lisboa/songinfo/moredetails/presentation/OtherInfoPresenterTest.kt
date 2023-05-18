package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.ArtistBiography
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.EmptyBiography
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

private const val PREFIX = "[*]"

class OtherInfoPresenterImplTest {

    private val biographyRepository: BiographyRepository = mockk()
    private val otherInfoHtmlHelper: OtherInfoHtmlHelper = mockk()

    private val otherInfoPresenter: OtherInfoPresenter by lazy {
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
        every { otherInfoHtmlHelper.textToHtml("[*]artistInfo", artistName) } returns "formattedHtml"

        val expectedUiState = OtherInfoUiState(
            "formattedHtml",
            "url",
            OtherInfoUiState.URL_LAST_FM_IMAGE
        )

        val uiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiStateObservable.subscribe {
            uiStateTester(it)
        }

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { uiStateTester(expectedUiState) }
    }

    @Test
    fun `searchArtistBiography with emptyBiography should update UI state with empty state`() {
        val artistName = "artist"
        every { biographyRepository.getArtistBiography(artistName) } returns EmptyBiography

        val expectedUiState = OtherInfoUiState(
            artistInfoHTML = "",
            artistUrl = "",
            lastFMImage = OtherInfoUiState.URL_LAST_FM_IMAGE
        )
        val uiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiStateObservable.subscribe {
            uiStateTester(it)
        }

        otherInfoPresenter.searchArtistBiography(artistName)

        verify { uiStateTester(expectedUiState) }
    }
}