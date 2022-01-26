package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterBranchActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText, branchNameEditText, emailEditText, workingHoursStartEditText, workingHoursEndEditText, phoneNumberEditText,
            addressLine1EditText, addressLine2EditText, addressCityEditText, addressProvinceEditText, addressCountryEditText, addressPostalCodeEditText;
    ;
    Button registerBranchButton;
    private DatabaseReference databaseAccounts;
    private static final String TAG = "RegisterBranch";
    private BranchAccount a;

    public static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[a-z0-9_.]+@[a-z0-9_]+\\.[a-z0-9_]+(\\.[a-z0-9_]+)*$", Pattern.CASE_INSENSITIVE);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_branch);

        retrieveMessage();

        // establishing database reference on right path
        databaseAccounts = FirebaseDatabase.getInstance().getReference("branch-accounts");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // STUB: Used for testing
    void retrieveMessage() {
        usernameEditText = findViewById(R.id.registerBranchUsernameField);
        passwordEditText = findViewById(R.id.registerBranchPasswordField);
        branchNameEditText = findViewById(R.id.registerBranchNameField);
        emailEditText = findViewById(R.id.registerBranchEmailField);
        workingHoursStartEditText = findViewById(R.id.registerBranchWorkingHoursStartField);
        workingHoursEndEditText = findViewById(R.id.registerBranchWorkingHoursEndField);
//        addressEditText = findViewById(R.id.registerBranchAddressField);
        addressLine1EditText = findViewById(R.id.registerBranchAddressLine1Field);
        addressLine2EditText = findViewById(R.id.registerBranchAddressLine2Field);
        addressCityEditText = findViewById(R.id.registerBranchAddressCityField);
        addressProvinceEditText = findViewById(R.id.registerBranchAddressProvinceField);
        addressCountryEditText = findViewById(R.id.registerBranchAddressCountryField);
        addressPostalCodeEditText = findViewById(R.id.registerBranchAddressPostalCodeField);
        phoneNumberEditText = findViewById((R.id.registerBranchPhoneNumberField));

        registerBranchButton = findViewById(R.id.registerBranchButton);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);

        registerBranchButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleBranchRegistration();
                    i.putExtra("BranchAccount", a);
                    setResult(RESULT_OK, i);
                } catch (NoSuchAlgorithmException e) {
                    // e.printStackTrace();
                    // setResult(RESULT_CANCELED, i);
                    Toast.makeText(getApplicationContext(), "Errors exist on the form, please try again!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }
            }
        }));

    }

    private void handleBranchRegistration() throws NoSuchAlgorithmException {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String branchName = branchNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String workingHoursStart = workingHoursStartEditText.getText().toString().trim();
        String workingHoursEnd = workingHoursEndEditText.getText().toString().trim();
//        String address = addressEditText.getText().toString().trim();
        String line1 = addressLine1EditText.getText().toString().trim();
        String line2 = addressLine2EditText.getText().toString().trim();
        String city = addressCityEditText.getText().toString().trim();
        String province = addressProvinceEditText.getText().toString().trim();
        String country = addressCountryEditText.getText().toString().trim();
        String postal = addressPostalCodeEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            isValid = false;
        } else if (!emailCheck(email)) {
            emailEditText.setError("Invalid email.");
            isValid = false;
        }
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required.");
            isValid = false;
        } else if (findEmployeeUsername(username)) {
            usernameEditText.setError("Username is already taken.");
            isValid = false;
        } else if (findRestrictedAccessUsername(username)) {
            usernameEditText.setError("Can not use this username. Please pick another one.");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(branchName)) {
            branchNameEditText.setError("Branch name is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(workingHoursStart)) {
            workingHoursStartEditText.setError("Working start hours is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(workingHoursEnd)) {
            workingHoursEndEditText.setError("Working end hours is required.");
            isValid = false;
        }

        if (!workingHoursCheck(workingHoursStart)) {
            workingHoursStartEditText.setError("Use 24-hour format!");
            isValid = false;
        }

        if (!workingHoursCheck(workingHoursEnd)) {
            workingHoursEndEditText.setError("Use 24-hour format!");
            isValid = false;
        }

        int valueHourStart = convertTimeToMinutes(workingHoursStart);
        int valueHourEnd = convertTimeToMinutes(workingHoursEnd);

        if (valueHourEnd == -1) {
            workingHoursEndEditText.setError("Input a valid time");
        }

        if (valueHourStart == -1) {
            workingHoursStartEditText.setError("Input a valid time");
        }

        if (!(valueHourEnd - valueHourStart >= 0)) {
            workingHoursEndEditText.setError("End time cannot be before start time");
            isValid = false;
        }

//        if (TextUtils.isEmpty(address)) {
//            addressEditText.setError("Address is required.");
//            isValid = false;
//        }
        // Validating address
        System.out.println("WORKING");
        if (TextUtils.isEmpty(line1)) {
            // checks if line 1 is empty
            addressLine1EditText.setError("Street address required");
            isValid = false;
        }

        if (!line2.matches("[0-9]*")) {
            // checks if line 2 is not numeric and if line 2 is not empty
            addressLine2EditText.setError("Invalid unit number.");
            isValid = false;
        }
        if (TextUtils.isEmpty(city)) {
            // checks if city is empty
            addressCityEditText.setError("City is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(province)) {
            // checks if province is empty
            addressProvinceEditText.setError("Province is required.");
            isValid = false;
        } else if (!province.matches("[A-Z][A-Z]")) {
            // checks if not in proper region format
            addressProvinceEditText.setError("Input in AA format.");
            isValid = false;
        }
        if (TextUtils.isEmpty(country)) {
            // checks if country is empty
            addressCountryEditText.setError("Country is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(postal)) {
            addressPostalCodeEditText.setError("Postal/ZIP code is required.");
            isValid = false;
        } else if (!postal.matches("[A-Z][0-9][A-Z][0-9][A-Z][0-9]") && !postal.matches("[0-9][0-9][0-9][0-9][0-9]")) {
            addressPostalCodeEditText.setError("Invalid postal/ZIP code format.");
            isValid = false;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Phone Number is required");
            isValid = false;
        } else if (!phoneNumber.matches("^\\+?(0|[1-9]\\d*)$")) {
            phoneNumberEditText.setError("Invalid phone number format, use '1234567890'");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Check for branch username uniqueness
        for (BranchAccount account : AccountGenerator.branchAccounts) {
            if (account.getUsername().equals(username)) {
                return;
            }
        }

        Address addressCreated = handleAddressCreation(line1, line2, city, province, country, postal);

        a = (BranchAccount) AccountGenerator.generate(username, password, email, AccountGenerator.AccountType.BRANCH);
        a.setBranchName(branchName);
        a.setWorkingHours(new WorkingHours(workingHoursStart, workingHoursEnd));
        a.setPhoneNumber(phoneNumber);
        a.setAddress(addressCreated);

        // saving account
        databaseAccounts.child(username).setValue(a);
        finish();
    }

    /* sourced from: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext */
    public static boolean oldEmailCheck(CharSequence e) {
        return (Patterns.EMAIL_ADDRESS.matcher(e).matches());
    }

    public static boolean emailCheck(String email) {
        Matcher matcher = VALID_EMAIL_PATTERN.matcher(email.trim());
        return matcher.find();

    }

    /* sourced from: https://www.regextester.com/108849 */
    public static boolean workingHoursCheck(String workingHours) {
        // return workingHours.matches("^\\+?(0|[1-9]\\d*)$");
        String regexFor24HrTime = "([01]?[0-9]|2{1}[0-3]):[0-5][0-9]";
        return workingHours.matches(regexFor24HrTime);
    }

    private Address handleAddressCreation(String line1Input, String line2Input, String cityInput, String provinceInput, String countryInput, String postalInput) {
        // Fields have already been validated
        Address newAddress = new Address(line1Input, line2Input, cityInput, provinceInput, countryInput, postalInput);
        return newAddress;
    }

    public boolean findEmployeeUsername(String username) {
        for (BranchAccount ba : AccountGenerator.branchAccounts) {
            if (ba.getUsername() != null && ba.getUsername().equals(username)) return true;
        }
        return false;
    }

    public boolean findRestrictedAccessUsername(String username) {
        for (Account ra : AccountGenerator.localAccounts) {
            if (ra.getUsername() != null && ra.getUsername().equals(username)) return true;
        }
        return false;
    }

    public static int convertTimeToMinutes(String time) {
        int minutes;
        try {
            String[] splitTime = time.split(":");
            minutes = (Integer.parseInt(splitTime[0]) * 60) + Integer.parseInt(splitTime[1]);
        } catch (Exception e) {
            minutes = -1;
        }
        return minutes;
    }
}
