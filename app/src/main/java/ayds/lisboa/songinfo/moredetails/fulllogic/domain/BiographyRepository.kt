package ayds.lisboa.songinfo.moredetails.fulllogic.domain

import ayds.lisboa.songinfo.moredetails.fulllogic.data.Biography

interface BiographyRepository {
    fun getArtistBiography(): Biography
}



