package ayds.lisboa.songinfo.moredetails.fulllogic.data

import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.BiographyRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography.EmptyBiography

class BiographyRepositoryImpl(
    private val lastFmLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
): BiographyRepository {

    override fun getArtistBiography(artistName: String): Biography {
        var artistBiography = lastFmLocalStorage.getArtistInfo(artistName)
        if (artistBiography == null) {
            try {
                artistBiography = lastFMService.getArtistBiography(artistName)
                if(artistBiography != null)
                    lastFmLocalStorage.saveArtist(artistName, artistBiography)
            }
            catch (e: Exception) {
                artistBiography = null
            }
        }
        return artistBiography ?: EmptyBiography
    }
}



