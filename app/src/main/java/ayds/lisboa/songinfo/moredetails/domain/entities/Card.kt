package ayds.lisboa.songinfo.moredetails.domain.entities

data class Card(
        var description: String = "",
        var infoUrl: String = "",
        var source: String = "",
        var sourceLogoUrl: String = "",
        var isLocallyStored: Boolean) {
}
