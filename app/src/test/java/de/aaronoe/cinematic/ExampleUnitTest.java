package de.aaronoe.cinematic;

import org.junit.Test;

import de.aaronoe.cinematic.Database.Utilities;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testDateFormatting() throws Exception {
        String date = "2015-12-15";
        assertEquals("2015", Utilities.convertDateToYear(date));
        assertEquals("December 15, 2015", Utilities.convertDate(date));
    }

    @Test
    public void datesNotNull() throws Exception {
        assertNull(Utilities.convertDate(null));
        assertNull(Utilities.convertDateToYear(null));

        String date = "2015-12-15";
        assertNotNull(Utilities.convertDateToYear(date));
        assertNotNull(Utilities.convertDate(date));
    }

}