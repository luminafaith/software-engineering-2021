package ca.uottawa.myapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchFieldsTest {

    @Test
    public void timeToMinutesTestValidTime() {
        int result = SearchByDoubleFieldActivity.convertTimeToMinutes("11:15");
        assertEquals(675, result);
    }

    @Test
    public void isEndAfterStartTimeTestValid() {
        boolean result = SearchByDoubleFieldActivity.isEndAfterStartTime("11:15", "15:10");
        assertTrue(result);
    }

    @Test
    public void isEndAfterStartTimeTestInvalid() {
        boolean result = SearchByDoubleFieldActivity.isEndAfterStartTime("16:15", "15:10");
        assertFalse(result);
    }

    @Test
    public void time1WithinTime2Test_passed() {
        boolean result = SearchByDoubleFieldActivity.time1WithinTime2("10:00", "15:00", "09:00", "17:00");
        assertTrue(result);
    }

    @Test
    public void time1WithinTime2Test_failedEarlyStart() {
        boolean result = SearchByDoubleFieldActivity.time1WithinTime2("08:00", "15:00", "09:00", "17:00");
        assertFalse(result);
    }

    @Test
    public void time1WithinTime2Test_failedLateEnd() {
        boolean result = SearchByDoubleFieldActivity.time1WithinTime2("10:00", "18:00", "09:00", "17:00");
        assertFalse(result);
    }

    @Test
    public void searchSuccess_passed() {
        boolean result = SearchBySingleField.searchSuccess("testString", "teststring");
        assertTrue(result);
    }

    @Test
    public void searchSuccess_passedPartialStart() {
        boolean result = SearchBySingleField.searchSuccess("testString", "tes");
        assertTrue(result);
    }

    @Test
    public void searchSuccess_passedPartialMid() {
        boolean result = SearchBySingleField.searchSuccess("testString", "sts");
        assertTrue(result);
    }

    @Test
    public void searchSuccess_failed() {
        boolean result = SearchBySingleField.searchSuccess("testString", "sta");
        assertFalse(result);
    }

}
