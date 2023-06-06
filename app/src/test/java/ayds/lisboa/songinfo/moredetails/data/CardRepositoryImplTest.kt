package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class CardRepositoryImplTest {

    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val cardsBroker: CardsBroker = mockk(relaxUnitFun = true)

    private val biographyRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, cardsBroker)
    }

    @Test
    fun `given existing artist cards should return cards`() {
        val cards = mutableListOf<Card>()
        val card = Card(
            "artistInfo",
            "url",
            Source.LastFm,
            "logo",
            false
        )
        cards.add(card)

        every { cardLocalStorage.getArtistCards("artist") } returns cards

        val result = biographyRepository.getCards("artist")

        assertEquals(cards, result)
    }

    @Test
    fun `given non-existing artist cards should fetch and save the cards`() {
        val cards = mutableListOf<Card>()
        val card = Card(
            "artistInfo",
            "url",
            Source.LastFm,
            "logo",
            false
        )
        cards.add(card)

        val emptyCards = mutableListOf<Card>()

        every { cardLocalStorage.getArtistCards("artist") } returns emptyCards
        every { cardsBroker.getCards("artist") } returns cards

        val result = biographyRepository.getCards("artist")

        assertEquals(cards, result)
        assertFalse(cards[0].isLocallyStored)

        verify { cardLocalStorage.saveArtistCard("artist", card) }
    }

    @Test
    fun `given non-existing artist cards should return empty cards`() {
        val emptyCards = mutableListOf<Card>()

        every { cardLocalStorage.getArtistCards("artist") } returns emptyCards
        every { cardsBroker.getCards("artist") } returns emptyCards

        val result = biographyRepository.getCards("artist")

        assertEquals(emptyCards, result)
    }

}
