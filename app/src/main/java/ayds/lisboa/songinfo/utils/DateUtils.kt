package ayds.lisboa.songinfo.utils

object DateUtils {

    fun numberToMonthName(month: Int?) =
        when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month"
        }

    fun isLeapYear(year: Int) =
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
            "(leap year)"
        else
            "(not a leap year)"
}