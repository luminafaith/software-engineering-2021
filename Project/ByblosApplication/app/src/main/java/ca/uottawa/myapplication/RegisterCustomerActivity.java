package ca.uottawa.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterCustomerActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText, emailEditText, firstNameEditText, lastNameEditText, dobEditText,
            addressLine1EditText, addressLine2EditText, addressCityEditText, addressProvinceEditText, addressCountryEditText, addressPostalCodeEditText;
    Button registerCustomerButton;
    private DatabaseReference databaseAccounts;
    private static final String TAG = "RegisterCustomer";
    private CustomerAccount a;

    public static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[a-z0-9_.]+@[a-z0-9_]+\\.[a-z0-9_]+(\\.[a-z0-9_]+)*$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        usernameEditText = findViewById(R.id.registerCustomerUsernameField);
        passwordEditText = findViewById(R.id.registerCustomerPasswordField);
        emailEditText = findViewById(R.id.registerCustomerEmailField);
        firstNameEditText = findViewById(R.id.firstNameField);
        lastNameEditText = findViewById(R.id.lastNameField);
        dobEditText = findViewById(R.id.dateOfBirthField);
        addressLine1EditText = findViewById(R.id.registerCustomerAddressLine1Field);
        addressLine2EditText = findViewById(R.id.registerCustomerAddressLine2Field);
        addressCityEditText = findViewById(R.id.registerCustomerAddressCityField);
        addressProvinceEditText = findViewById(R.id.registerCustomerAddressProvinceField);
        addressCountryEditText = findViewById(R.id.registerCustomerAddressCountryField);
        addressPostalCodeEditText = findViewById(R.id.registerCustomerAddressPostalCodeField);

        registerCustomerButton = findViewById(R.id.registerCustomerButton);

        registerCustomerButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    handleCustomerRegistration();
                }
                catch (ParseException e) {
                    dobEditText.setError("Date of birth is required. Enter in YYYY/MM/DD format");
                }
            }
        }));

        // establishing database reference on right path
        databaseAccounts = FirebaseDatabase.getInstance().getReference("customer-accounts");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void handleCustomerRegistration() throws ParseException {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String line1 = addressLine1EditText.getText().toString().trim();
        String line2 = addressLine2EditText.getText().toString().trim();
        String city = addressCityEditText.getText().toString().trim();
        String province = addressProvinceEditText.getText().toString().trim();
        String country = addressCountryEditText.getText().toString().trim();
        String postal = addressPostalCodeEditText.getText().toString().trim();



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
        }
        else if (findCustomerUsername(username)){
            usernameEditText.setError("Username is already taken.");
            isValid = false;
        }
        else if(findRestrictedAccessUsername(username)) {
            usernameEditText.setError("Can not use this username. Please pick another one.");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First name is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(lastName)) {
            firstNameEditText.setError("Last name is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(dob)) {
            dobEditText.setError("Date of birth is required. Enter in YYYY-MM-DD format");
            isValid = false;
        } else if (!dateCheck(dob)) {
            dobEditText.setError("Invalid date of birth.");
            isValid = false;
        }

        // Validating address
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

        if (!isValid){
            return;
        }

        // Check for customer username uniqueness
        for (CustomerAccount account : AccountGenerator.customerAccounts) {
            if (account.getUsername().equals(username)) {
                return;
            }
        }

        String dateOfBirth = dob;
        Address addressCreated = handleAddressCreation(line1, line2, city, province, country, postal);

        a = (CustomerAccount) AccountGenerator.generate(username, password, email, AccountGenerator.AccountType.CUSTOMER);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setDateOfBirth(dateOfBirth);
        a.setAddress(addressCreated);

        // saving account
        databaseAccounts.child(username).setValue(a);
        finish();
    }

    /* sourced from: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext */
    public static boolean emailCheck(String email) {
        Matcher matcher = VALID_EMAIL_PATTERN.matcher(email.trim());
        return matcher.find();
    }

    /* sourced from: https://www.regextester.com/96683 */
    public static boolean dateCheck (String d) {
        return d.matches("([12]\\d{3}[-/](0[1-9]|1[0-2])[-/](0[1-9]|[12]\\d|3[01]))");
    }

    private Address handleAddressCreation(String line1Input, String line2Input, String cityInput, String provinceInput, String countryInput, String postalInput) {
        // Fields have already been validated
        Address newAddress = new Address(line1Input, line2Input, cityInput, provinceInput, countryInput, postalInput);
        return newAddress;
    }

    public boolean  findCustomerUsername (String username){
        for (CustomerAccount ca : AccountGenerator.customerAccounts){
            if ((ca.getUsername().equals(username)))return true;
        }
        return false;
    }

    public boolean findRestrictedAccessUsername (String username){
        for (Account ra : AccountGenerator.localAccounts){
            if ((ra.getUsername().equals(username))) return true;
        }
        return false;
    }
}
