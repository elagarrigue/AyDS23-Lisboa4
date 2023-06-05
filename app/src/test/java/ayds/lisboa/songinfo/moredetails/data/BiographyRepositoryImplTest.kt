package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class BiographyRepositoryImplTest {

    private val localStorage: LocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: LastFMService = mockk(relaxUnitFun = true)

    private val biographyRepository: CardRepository by lazy {
        CardRepositoryImpl(localStorage, lastFMService)
    }

    @Test
    fun `given existing artist biography should return biography`() {
        val biography = Card.ArtistBiography("Artist biography", "url", true)
        every { localStorage.getArtistInfo("artist") } returns biography

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(biography, result)
    }

    @Test
    fun `given non-existing artist biography should fetch and save the biography`() {
        val biography = ArtistBiography("Artist biography", "url", false)
        every { localStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } returns biography

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(biography, result)
        assertFalse(biography.isLocallyStored)

        val artistBiography = Card.ArtistBiography(biography.artistInfo, biography.url, biography.isLocallyStored)
        verify { localStorage.saveArtist("artist", artistBiography) }
    }

    @Test
    fun `given non-existing artist biography should return empty biography`() {
        every { localStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } returns null

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Card.EmptyBiography, result)
    }

    @Test
    fun `given service exception should return empty biography`() {
        every { localStorage.getArtistInfo("artist") } returns null
        every { lastFMService.getArtistBiography("artist") } throws Exception()

        val result = biographyRepository.getArtistBiography("artist")

        assertEquals(Card.EmptyBiography, result)
    }
}
