package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.DateUtils
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import ayds.lisboa.songinfo.utils.DateUtils.isLeapYear

object ReleaseDateFactory {
    fun get(song: SpotifySong) =
        when (song.releaseDatePrecision) {
            "year" -> YearFormatter(song.releaseDate)
            "month" -> MonthFormatter(song.releaseDate)
            "day" -> DayFormatter(song.releaseDate)
            else -> EmptyFormatter(song.releaseDate)
        }
}

sealed class ReleaseDateFormatter(releaseDate: String) {
    val splitReleaseDate: List<String> = releaseDate.split("-");
    abstract fun formatDate(): String
}

class DayFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    val month = splitReleaseDate.get(1)
    val day = splitReleaseDate.get(2)
    override fun formatDate() = "${day}/${month}/${year}"
}

class MonthFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    val month = splitReleaseDate.get(1)
    override fun formatDate() = "${DateUtils.numberToMonthName(month?.toIntOrNull())}, $year"
}

class YearFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    val year = splitReleaseDate.get(0)
    override fun formatDate() = "$year ${isLeapYear(year.toInt())}"
}

class EmptyFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    override fun formatDate() = ""
}
