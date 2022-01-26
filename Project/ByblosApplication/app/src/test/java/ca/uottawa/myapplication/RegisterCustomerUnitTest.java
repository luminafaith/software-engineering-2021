package ca.uottawa.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RegisterCustomerUnitTest {

    @Test
    public void usernameEmailTest_passed() {

        boolean result = RegisterCustomerActivity.emailCheck("user_Name1.2@domain.ext");
        assertTrue(result);

    }

    @Test
    public void usernameEmailTest_failedNoDomain() {

        boolean result = RegisterCustomerActivity.emailCheck("user_Name1.2");
        assertFalse(result);

    }

    @Test
    public void usernameEmailTest_failedEmpty() {

        boolean result = RegisterCustomerActivity.emailCheck("");
        assertFalse(result);

    }

    @Test
    public void dateCheckTest_passed() {

        boolean result = RegisterCustomerActivity.dateCheck("2011-11-11");
        assertTrue(result);

    }

    @Test
    public void dateCheckTest_passedAltFormat() {

        boolean result = RegisterCustomerActivity.dateCheck("2011/11/11");
        assertTrue(result);

    }


    @Test
    public void dateCheckTest_failedEmpty() {

        boolean result = RegisterCustomerActivity.dateCheck("");
        assertFalse(result);

    }

    @Test
    public void dateCheckTest_failedBadFormat() {

        boolean result = RegisterCustomerActivity.dateCheck("2011~11~11");
        assertFalse(result);

    }

    @Test
    public void dateCheckTest_failedMissingPart() {

        boolean result = RegisterCustomerActivity.dateCheck("2011-11");
        assertFalse(result);

    }

}
