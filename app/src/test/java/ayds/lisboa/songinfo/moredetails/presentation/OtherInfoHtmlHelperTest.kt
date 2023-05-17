package ayds.lisboa.songinfo.moredetails.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class OtherInfoHtmlHelperImplTest {

    private val otherInfoHtmlHelper: OtherInfoHtmlHelper = OtherInfoHtmlHelperImpl()

    @Test
    fun `given text and term should return formatted HTML`() {
        val text = "This is a test. Test term is highlighted."
        val term = "test"

        val expectedHtml =
            "<html><div width=400><font face=\"arial\">This is a <b>TEST</b>. <b>TEST</b> term is highlighted.</font></div></html>"

        val result = otherInfoHtmlHelper.textToHtml(text, term)

        assertEquals(expectedHtml, result)
    }

    @Test
    fun `given text and null term should return formatted HTML without bolding`() {
        val text = "This is a test. Test term is highlighted."
        val term = ""

        val expectedHtml =
            "<html><div width=400><font face=\"arial\">This is a test. Test term is highlighted.</font></div></html>"

        // Act
        val result = otherInfoHtmlHelper.textToHtml(text, term)

        // Assert
        assertEquals(expectedHtml, result)
    }
}
