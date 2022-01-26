package ca.uottawa.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ProfileFieldsTest {

    @Test
    public void addressLine2CheckTest_passed() {
        boolean result = BranchProfileEditor.addressLine2Check("22");
        assertTrue(result);
    }

    @Test
    public void addressLine2CheckTest_passedBlank() {
        boolean result = BranchProfileEditor.addressLine2Check("");
        assertTrue(result);
    }

    @Test
    public void addressLine2CheckTest_failed() {
        boolean result = BranchProfileEditor.addressLine2Check("asdf1");
        assertFalse(result);
    }

    @Test
    public void provinceCheckTest_passed() {
        boolean result = BranchProfileEditor.provinceCheck("AB");
        assertTrue(result);
    }

    @Test
    public void provinceCheckTest_failed() {
        boolean result = BranchProfileEditor.provinceCheck("ABC");
        assertFalse(result);
    }

    @Test
    public void postalCodeCheckTest_passed1() {
        boolean result = BranchProfileEditor.postalCodeCheck("A0A0A0");
        assertTrue(result);
    }

    @Test
    public void postalCodeCheckTest_passed2() {
        boolean result = BranchProfileEditor.postalCodeCheck("12345");
        assertTrue(result);
    }

    @Test
    public void postalCodeCheckTest_failed() {
        boolean result = BranchProfileEditor.postalCodeCheck("ABC");
        assertFalse(result);
    }

}
