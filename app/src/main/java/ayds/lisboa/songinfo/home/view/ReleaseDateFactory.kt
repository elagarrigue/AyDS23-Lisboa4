package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.DateUtils
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import ayds.lisboa.songinfo.utils.DateUtils.isLeapYear

interface ReleaseDateFactory {
    fun get(song: SpotifySong): ReleaseDateFormatter
}

internal object ReleaseDateFactoryImpl : ReleaseDateFactory {
    override fun get(song: SpotifySong) =
        when (song.releaseDatePrecision) {
            "year" -> YearFormatter(song.releaseDate)
            "month" -> MonthFormatter(song.releaseDate)
            "day" -> DayFormatter(song.releaseDate)
            else -> DefaultFormatter(song.releaseDate)
        }
}

sealed class ReleaseDateFormatter(releaseDate: String) {
    protected val splitReleaseDate: List<String> = releaseDate.split("-")
    abstract fun formatDate(): String
}

private class DayFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    private val year = splitReleaseDate[0]
    private val month = splitReleaseDate[1]
    private val day = splitReleaseDate[2]
    override fun formatDate() = "${day}/${month}/${year}"
}

private class MonthFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    private val year = splitReleaseDate[0]
    private val month = splitReleaseDate[1]
    override fun formatDate() = "${DateUtils.numberToMonthName(month.toInt())}, $year"
}

private class YearFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    private val year = splitReleaseDate[0]
    override fun formatDate() = "$year ${isLeapYear(year.toInt())}"
}

private class DefaultFormatter(releaseDate: String) : ReleaseDateFormatter(releaseDate) {
    private val defaultReleaseDate = releaseDate
    override fun formatDate() = defaultReleaseDate
}
