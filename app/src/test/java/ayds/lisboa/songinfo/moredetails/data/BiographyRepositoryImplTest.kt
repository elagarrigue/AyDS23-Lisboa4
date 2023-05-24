package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import lisboa4_LastFM.ArtistBiography
import lisboa4_LastFM.LastFMService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class BiographyRepositoryImplTest {

    private val lastFmLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: LastFMService = mockk(relaxUnitFun = true)

    private val biographyRepository: BiographyRepository by lazy {
        BiographyRepositoryImpl(lastFmLocalStorage, lastFMService)
    }

    @Test
    fun `given existing artist biography should return biography`() {
        val biography = Biography.ArtistBiography("Artist biography", "url", true)
        every { lastFmLocalStorage.getArtistInfo("artist") } returns biography

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(biography, result)
    }

    @Test
    fun `given non-existing artist biography should fetch and save the biography`() {
        val biography = ArtistBiography("Artist biography", "url", false)
        every { lastFmLocalStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } returns biography

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(biography, result)
        assertFalse(biography.isLocallyStored)

        val artistBiography = Biography.ArtistBiography(biography.artistInfo, biography.url, biography.isLocallyStored)
        verify { lastFmLocalStorage.saveArtist("artist", artistBiography) }
    }

    @Test
    fun `given non-existing artist biography should return empty biography`() {
        every { lastFmLocalStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } returns null

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Biography.EmptyBiography, result)
    }

    @Test
    fun `given service exception should return empty biography`() {
        every { lastFmLocalStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } throws Exception()

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Biography.EmptyBiography, result)
    }
}
