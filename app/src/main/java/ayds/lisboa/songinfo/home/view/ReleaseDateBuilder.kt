package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.DateUtils
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

object ReleaseDateBuilder {

    public fun buildReleaseDate(song: SpotifySong): String {
        val releaseDate = song.releaseDate
        val precision = song.releaseDatePrecision
        val splitReleaseDate: List<String> = song.releaseDate.split("-");
        val year = splitReleaseDate.first()
        val month = splitReleaseDate.getOrNull(1)
        val day = splitReleaseDate.getOrNull(2)
        return when (precision) {
            "year" -> year
            "month" -> "${DateUtils.numberToMonthName(month?.toIntOrNull())}, $year"
            "day" -> "${day}/${month}/${year}"
            else -> ""
        }
    }

}