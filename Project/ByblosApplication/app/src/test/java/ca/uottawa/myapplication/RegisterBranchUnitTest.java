package ca.uottawa.myapplication;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.CoreMatchers.*;

import android.content.Context;

//import androidx.test.ext.junit.runners.AndroidJUnit4;

//@RunWith(MockitoJUnitRunner.class)
//@RunWith(AndroidJUnit4::class)
public class RegisterBranchUnitTest {

//    @Mock
//    Context mMockContext;

    @Test
    public void usernameEmailText_passed() {
        boolean result = RegisterBranchActivity.emailCheck("user_Name1.2@domain.ext");
        assertTrue(result);
    }

    @Test
    public void usernameEmailTextNoDomain_failed() {
        boolean result = RegisterBranchActivity.emailCheck("user_Name1.2");
        assertFalse(result);
    }

    @Test
    public void workingHours_passed() {
        boolean result = RegisterBranchActivity.workingHoursCheck("11:15");
        assertTrue(result);
    }

    @Test
    public void timeToMinutesTestValidTime() {
        int result = RegisterBranchActivity.convertTimeToMinutes("11:15");
        assertEquals(675, result);
    }

    @Test
    public void timeToMinutesTestInvalidTime() {
        int result = RegisterBranchActivity.convertTimeToMinutes("11:");
        assertEquals(-1, result);
    }

}
