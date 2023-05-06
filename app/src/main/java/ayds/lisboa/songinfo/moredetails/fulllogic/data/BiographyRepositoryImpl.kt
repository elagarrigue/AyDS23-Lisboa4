package ayds.lisboa.songinfo.moredetails.fulllogic.data

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.Biography
import ayds.lisboa.songinfo.moredetails.fulllogic.DEFAULT_STRING
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.BiographyRepository

class BiographyRepositoryImpl: BiographyRepository {

    override fun getArtistBiography(): Biography {
        val artistInfo = getArtistInfoFromDataBase()
        val artistBiography =
            if(artistInfo.isEmpty()){
                getArtistBiographyFromLastFMAPI().apply {
                    if(this.artistInfo.isNotEmpty())
                        saveArtistInfoInDataBase(this.artistInfo)
                }
            } else {
                Biography(artistInfo, DEFAULT_STRING, true)
            }
        return artistBiography
    }
}



