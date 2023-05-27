package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService
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
        val biography = Card.ArtistBiography("Artist biography", "url", true)
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

        val artistBiography = Card.ArtistBiography(biography.artistInfo, biography.url, biography.isLocallyStored)
        verify { lastFmLocalStorage.saveArtist("artist", artistBiography) }
    }

    @Test
    fun `given non-existing artist biography should return empty biography`() {
        every { lastFmLocalStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } returns null

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Card.EmptyBiography, result)
    }

    @Test
    fun `given service exception should return empty biography`() {
        every { lastFmLocalStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } throws Exception()

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Card.EmptyBiography, result)
    }
}
