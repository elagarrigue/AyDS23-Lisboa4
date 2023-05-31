package ayds.lisboa.songinfo.moredetails.domain.entities

data class Card(
        var description: String = "",
        var infoUrl: String = "",
        var source: Source,
        var sourceLogoUrl: String = "",
        var isLocallyStored: Boolean) {
}

enum class Source{
        LastFm,
        Wikipedia,
        NewYorkTimes;
}
