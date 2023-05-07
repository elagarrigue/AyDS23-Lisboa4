package ayds.lisboa.songinfo.moredetails.fulllogic.domain

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

interface BiographyRepository {
    fun getArtistBiography(artistName: String): Biography
}



