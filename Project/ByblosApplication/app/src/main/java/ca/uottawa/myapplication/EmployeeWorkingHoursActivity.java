package ca.uottawa.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class EmployeeWorkingHoursActivity extends AppCompatActivity {

    DatabaseReference databaseBranches;
    BranchAccount ba;
    String branchName;
    ArrayList<EditText> startingFields;
    ArrayList<EditText> endingFields;
    ArrayList<String> startingHours;
    ArrayList<String> endingHours;
    String regexFor24HrTime = "([01]?[0-9]|2{1}[0-3]):[0-5][0-9]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employee_hours);

        databaseBranches = FirebaseDatabase.getInstance().getReference("branch-accounts");

        Bundle extras = getIntent().getExtras();
        branchName = extras.getString("branch_name");
        ba = findAccount(branchName);

        startingFields = new ArrayList<>();
        endingFields = new ArrayList<>();

        EditText monStart = (EditText) findViewById(R.id.mondayStartingHour);
        EditText tueStart = (EditText) findViewById(R.id.tuesdayStartingHour);
        EditText wedStart = (EditText) findViewById(R.id.wednesdayStartingHour);
        EditText thuStart = (EditText) findViewById(R.id.thursdayStartingHour);
        EditText friStart = (EditText) findViewById(R.id.fridayStartingHour);
        EditText satStart = (EditText) findViewById(R.id.saturdayStartingHour);
        EditText sunStart = (EditText) findViewById(R.id.sundayStartingHour);
        EditText holStart = (EditText) findViewById(R.id.holidayStartingHour);

        startingFields.add(monStart);
        startingFields.add(tueStart);
        startingFields.add(wedStart);
        startingFields.add(thuStart);
        startingFields.add(friStart);
        startingFields.add(satStart);
        startingFields.add(sunStart);
        startingFields.add(holStart);

        EditText monEnd = (EditText) findViewById(R.id.mondayEndingHour);
        EditText tueEnd = (EditText) findViewById(R.id.tuesdayEndingHour);
        EditText wedEnd = (EditText) findViewById(R.id.wednesdayEndingHour);
        EditText thuEnd = (EditText) findViewById(R.id.thursdayEndingHour);
        EditText friEnd = (EditText) findViewById(R.id.fridayEndingHour);
        EditText satEnd = (EditText) findViewById(R.id.saturdayEndingHour);
        EditText sunEnd = (EditText) findViewById(R.id.sundayEndingHour);
        EditText holEnd = (EditText) findViewById(R.id.holidayEndingHour);

        endingFields.add(monEnd);
        endingFields.add(tueEnd);
        endingFields.add(wedEnd);
        endingFields.add(thuEnd);
        endingFields.add(friEnd);
        endingFields.add(satEnd);
        endingFields.add(sunEnd);
        endingFields.add(holEnd);

        startingHours = ba.getWorkingHours().getStartingHours();
        endingHours = ba.getWorkingHours().getEndingHours();

        populateFields();

        Button btnSave = (Button) findViewById(R.id.saveHourValues);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveHours();
            }
        });
    }

    void populateFields() {
        for (int i = 0 ; i < 8; i++) {
            EditText tmpS = startingFields.get(i);
            EditText tmpE = endingFields.get(i);
            tmpS.setText(startingHours.get(i));
            tmpE.setText(endingHours.get(i));
        }
    }

    boolean validateFields() {
        boolean isValid = true;

        EditText startF, endF;
        for (int i = 0; i < 8; i++) {
            startF = startingFields.get(i);
            endF = endingFields.get(i);

            if (!(startF.getText().toString()).matches(regexFor24HrTime)) {
                startF.setError("Use 24-hour format!");
                isValid = false;
            }

            if (!(endF.getText().toString()).matches(regexFor24HrTime)) {
                endF.setError("Use 24-hour format!");
                isValid = false;
            }

            String[] hourMinStart = startF.getText().toString().split(":");
            String[] hourMinEnd = endF.getText().toString().split(":");

            int valueHourStart = (Integer.parseInt(hourMinStart[0]) * 60) + Integer.parseInt(hourMinStart[1]);
            int valueHourEnd = (Integer.parseInt(hourMinEnd[0]) * 60) + Integer.parseInt(hourMinEnd[1]);

            if (!(valueHourEnd - valueHourStart >= 0)) {
                endF.setError("End time cannot be before start time");
                isValid = false;
            }
        }

        return isValid;
    }

    BranchAccount findAccount (String branchName) {
        for (BranchAccount ba : AccountGenerator.branchAccounts) {
            if (ba.getBranchName() != null && ba.getBranchName().equals(branchName)) return ba;
        }
        return null;
    }

    void saveHours() {
        boolean fieldsAreValid = validateFields();
        if (fieldsAreValid) {
            getValuesFromFields();
            WorkingHours wh = new WorkingHours(startingHours, endingHours);
            ba.setWorkingHours(wh);
            databaseBranches.child(ba.getBranchName()).setValue(ba);
            finish();
        }
    }

    private void getValuesFromFields() {
        for (int i = 0; i < 8; i++) {
            startingHours.set(i, startingFields.get(i).getText().toString());
            endingHours.set(i, endingFields.get(i).getText().toString());
        }
    }


}
