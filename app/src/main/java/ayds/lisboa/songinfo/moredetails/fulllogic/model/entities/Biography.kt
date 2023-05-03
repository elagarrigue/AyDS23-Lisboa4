package ayds.lisboa.songinfo.moredetails.fulllogic.model.entities

sealed class Biography {

    data class ArtistBiography(
        var artistInfo: String,
        var url: String,
        var isInDataBase: Boolean
    ): Biography()

    object EmptyBiography: Biography()
}

