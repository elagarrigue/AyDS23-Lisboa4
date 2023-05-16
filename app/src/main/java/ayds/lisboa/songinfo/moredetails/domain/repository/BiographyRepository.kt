package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Biography

interface BiographyRepository {
    fun getArtistBiography(artistName: String): Biography
}



