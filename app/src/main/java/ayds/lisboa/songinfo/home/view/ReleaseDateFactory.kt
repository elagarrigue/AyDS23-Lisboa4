package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.DateUtils
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import ayds.lisboa.songinfo.utils.DateUtils.isLeapYear

object ReleaseDateFactory {
    fun get(song: SpotifySong) =
        when (song.releaseDatePrecision) {
            "year" -> yearFormatter(song.releaseDate)
            "month" -> monthFormatter(song.releaseDate)
            "day" -> dayFormatter(song.releaseDate)
            else
            -> emptyFormatter(song.releaseDate)

        }
}

sealed class ReleaseDateFormatter(releaseDate: String) {
    val splitReleaseDate: List<String> = releaseDate.split("-");
    abstract fun formatDate(): String
}


class dayFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    val month = splitReleaseDate.get(1)
    val day = splitReleaseDate.get(2)
    override fun formatDate() = "${day}/${month}/${year}"
}

class monthFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    val month = splitReleaseDate.get(1)
    override fun formatDate() = "${DateUtils.numberToMonthName(month?.toIntOrNull())}, $year"
}

class yearFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    override fun formatDate() = "${year}+${isLeapYear(year.toInt())}"
}

class emptyFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    override fun formatDate() = ""
}
