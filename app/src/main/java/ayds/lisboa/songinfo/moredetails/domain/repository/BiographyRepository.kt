package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface BiographyRepository {
    fun getArtistBiography(artistName: String): MutableCollection<Card>
}



