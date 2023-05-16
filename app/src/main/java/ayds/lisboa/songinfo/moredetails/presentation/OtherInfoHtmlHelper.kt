package ayds.lisboa.songinfo.moredetails.presentation

import java.util.*

private const val HTML_HTML_OPEN = "<html>"
private const val HTML_HTML_CLOSE = "</html>"
private const val HTML_FONT_FACE_ARIAL_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font>"
private const val HTML_DIV_W400_OPEN = "<div width=400>"
private const val HTML_DIV_CLOSE = "</div>"
private const val HTML_B_OPEN = "<b>"
private const val HTML_B_CLOSE = "</b>"
private const val HTML_BR = "<br>"
private const val HTML_SPACE = " "
private const val SIMPLE_QUOTE = "'"
private const val NEW_LINE = "\n"
private const val FLAG_INSENSITIVE_UPPER_LOWER_CASE = "(?i)"

interface OtherInfoHtmlHelper
{
    fun textToHtml(text:String, term: String?): String
}
internal class OtherInfoHtmlHelperImpl: OtherInfoHtmlHelper {
    override fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("$HTML_HTML_OPEN}$HTML_DIV_W400_OPEN")
        builder.append(HTML_FONT_FACE_ARIAL_OPEN)
        val textWithBold = text
            .replace(SIMPLE_QUOTE, HTML_SPACE)
            .replace(NEW_LINE, HTML_BR)
            .replace(
                "$FLAG_INSENSITIVE_UPPER_LOWER_CASE$term".toRegex(),
                HTML_B_OPEN + term!!.uppercase(Locale.getDefault()) + HTML_B_CLOSE
            )
        builder.append(textWithBold)
        builder.append("$HTML_FONT_CLOSE$HTML_DIV_CLOSE$HTML_HTML_CLOSE")
        return builder.toString()
    }
}