package ayds.lisboa.songinfo.moredetails.domain

interface BiographyRepository {
    fun getArtistBiography(artistName: String): Biography
}



