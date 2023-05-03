package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository

import ayds.lisboa.songinfo.moredetails.fulllogic.model.entities.Biography

interface BiographyRepository {
    fun getArtistBiography(): Biography
}



