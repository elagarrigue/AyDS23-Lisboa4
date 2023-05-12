package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class Biography {

    data class ArtistBiography(
        var artistInfo: String,
        var url: String,
        var isLocallyStored: Boolean
    ): Biography()

    object EmptyBiography: Biography()
}

