package ayds.lisboa.songinfo.moredetails.data
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import ayds.lisboa.songinfo.moredetails.domain.entities.Biography.EmptyBiography
import lisboa4LastFM.LastFMService

class BiographyRepositoryImpl(
    private val lastFmLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
): BiographyRepository {

    override fun getArtistBiography(artistName: String): Biography {
        var artistBiography = lastFmLocalStorage.getArtistInfo(artistName)
        if (artistBiography == null) {
            try {
                val lastFmBiography = lastFMService.getArtistBiography(artistName)
                artistBiography = lastFmBiography?.let {
                    Biography.ArtistBiography(
                        it.artistInfo,
                        it.url,
                        it.isLocallyStored
                    )
                }
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



