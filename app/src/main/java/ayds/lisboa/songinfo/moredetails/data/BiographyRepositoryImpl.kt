package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.LastFMService
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.Biography
import ayds.lisboa.songinfo.moredetails.domain.BiographyRepository
import ayds.lisboa.songinfo.moredetails.domain.Biography.EmptyBiography

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



