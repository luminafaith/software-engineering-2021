package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BranchProfileEditor extends AppCompatActivity {
    EditText branchNameEditText, addressLine1EditText, addressLine2EditText, addressCityEditText,
            addressProvinceEditText, addressCountryEditText, addressPostalCodeEditText, phoneNumberEditText;

    Button confirmBranchEdit;
    BranchAccount branchAccountSelected;

    private DatabaseReference databaseAccounts;
    private static final String TAG = "RegisterBranch";
    private BranchAccount a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_profile_editor);

        retrieveMessage();

        Bundle extras = getIntent().getExtras();
        String branchName = extras.getString("branch_name");
        a = findAccount(branchName);

//    // establishing database reference on right path
        databaseAccounts = FirebaseDatabase.getInstance().getReference("branch-accounts");

        confirmBranchEdit = (Button)  findViewById(R.id.buttonConfirm);
        confirmBranchEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleBranchEdit();
            }
        });

        ArrayList<EditText> editableFields = new ArrayList<>();
        ArrayList<String>   branchValues   = new ArrayList<>();

        LinearLayout ll = (LinearLayout) findViewById(R.id.editBranchLL);

        editableFields.add(branchNameEditText);
        editableFields.add(addressLine1EditText);
        editableFields.add(addressLine2EditText);
        editableFields.add(addressCityEditText);
        editableFields.add(addressProvinceEditText);
        editableFields.add(addressCountryEditText);
        editableFields.add(addressPostalCodeEditText);
        editableFields.add(phoneNumberEditText);

        branchValues.add(a.getBranchName());
        branchValues.add(a.getAddress().getLine1());
        branchValues.add(a.getAddress().getLine2());
        branchValues.add(a.getAddress().getCity());
        branchValues.add(a.getAddress().getProvince());
        branchValues.add(a.getAddress().getCity());
        branchValues.add(a.getAddress().getPostalCode());
        branchValues.add(a.getPhoneNumber());

        // to load into text fields

        for (int i = 0; i < 8; i ++) {
            editableFields.get(i).setText(branchValues.get(i));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    void retrieveMessage() {
        branchNameEditText = findViewById(R.id.editBranchNameText);
        addressLine1EditText = findViewById(R.id.editStreetText);
        addressLine2EditText = findViewById(R.id.editUnitText);
        addressCityEditText = findViewById(R.id.editCityText);
        addressProvinceEditText = findViewById(R.id.editProvinceText);
        addressCountryEditText = findViewById(R.id.editCountryText);
        addressPostalCodeEditText = findViewById(R.id.editPostalText);
        phoneNumberEditText = findViewById(R.id.editPNText);

        confirmBranchEdit = findViewById(R.id.buttonConfirm);

    }

    private void handleBranchEdit() {
        String branchName = branchNameEditText.getText().toString().trim();
        String branchPN = phoneNumberEditText.getText().toString().trim();
        String line1 = addressLine1EditText.getText().toString().trim();
        String line2 = addressLine2EditText.getText().toString().trim();
        String city = addressCityEditText.getText().toString().trim();
        String province = addressProvinceEditText.getText().toString().trim();
        String country = addressCountryEditText.getText().toString().trim();
        String postal = addressPostalCodeEditText.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(branchName)) {
            branchNameEditText.setError("Branch name is required.");
            isValid = false;
        } else if (!isValidBranchName(branchName)){
            branchNameEditText.setError("Invalid branch name");
            isValid = false;
        }
        // Validating address
        if (TextUtils.isEmpty(line1)) {
            // checks if line 1 is empty
            addressLine1EditText.setError("Street address required");
            isValid = false;
        }
        if (!addressLine2Check(line2)) {
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
        } else if (!provinceCheck(province)) {
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
        } else if (!postalCodeCheck(postal)) {
            addressPostalCodeEditText.setError("Invalid postal/ZIP code format.");
            isValid = false;
        }

        if (TextUtils.isEmpty(branchPN)) {
            phoneNumberEditText.setError("Phone number is required.");
            isValid = false;
        } else if (!isValidPN(branchPN)){
            phoneNumberEditText.setError("Invalid phone number");
            isValid = false;
        }

        if (isValid) {
            BranchAccount ba = a;
            Address addy = new Address(line1, line2, city, province, country, postal);
            ba.setBranchName(branchNameEditText.getText().toString());
            ba.setAddress(addy);
            ba.setPhoneNumber(branchPN);

            databaseAccounts.child(ba.getUsername()).setValue(ba);
            finish();
        }
    }

    /* sourced from: https://www.regextester.com/108849 */
    public static boolean isValidPN(String phoneNumber) {
        return phoneNumber.matches("^\\+?(0|[1-9]\\d*)$");
    }

    public static boolean isValidBranchName(String branchBranchName) {
        return branchBranchName.matches("^[0-9A-Za-z ]+$");
    }

    BranchAccount findAccount (String branchName) {
        for (BranchAccount ba : AccountGenerator.branchAccounts) {
            if (ba.getBranchName() != null && ba.getBranchName().equals(branchName)) return ba;
        }
        return null;
    }

    public static boolean addressLine2Check(String line2) {
        return line2.matches("[0-9]*");
    }

    public static boolean provinceCheck(String province) {
        return province.matches("[A-Z][A-Z]");
    }

    public static boolean postalCodeCheck(String postal) {
        return postal.matches("[A-Z][0-9][A-Z][0-9][A-Z][0-9]") || postal.matches("[0-9][0-9][0-9][0-9][0-9]");
    }
}