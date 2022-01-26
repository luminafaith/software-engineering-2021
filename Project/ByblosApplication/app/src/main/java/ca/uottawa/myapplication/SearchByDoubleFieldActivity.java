package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchByDoubleFieldActivity extends AppCompatActivity {
    ImageButton updateBranches;
    EditText searchByWH1;
    EditText searchByWH2;
    Spinner spinner;

    ListView listOfSearchedWH;
    String customerName;
    NewAvailableServicesList availableBranchesAdapter;

    ArrayList<String> displayedBranches = new ArrayList<>();

    String regexFor24HrTime = "([01]?[0-9]|2{1}[0-3]):[0-5][0-9]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_double_field);

        Bundle extras = getIntent().getExtras();
        customerName = extras.getString("customerName");

        listOfSearchedWH = (ListView) findViewById(R.id.listOfSearchedWH);
//        updateDisplayedBranches("");

        searchByWH1 = findViewById(R.id.searchByWH1);
        searchByWH2 = findViewById(R.id.searchByWH2);

        spinner = (Spinner) findViewById(R.id.dayOfTheWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.week_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        updateBranches = (ImageButton) findViewById(R.id.updateBranches);
        updateBranches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDisplayedBranches();
            }
        });

        initDisplayedBranches();

        listOfSearchedWH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("clicked branch", displayedBranches.get(position));
                Intent intent = new Intent(getApplicationContext(), CustomerChooseAction.class);
                intent.putExtra("branch_username", displayedBranches.get(position));
                intent.putExtra("customer_username", customerName);
                startActivity(intent);
                finish();
            }
        });

        availableBranchesAdapter = new NewAvailableServicesList(SearchByDoubleFieldActivity.this, new ArrayList<>(displayedBranches));
        listOfSearchedWH.setAdapter(availableBranchesAdapter);
    }

    boolean validateFields() {
        boolean isValid = true;

        if (!(searchByWH1.getText().toString()).matches(regexFor24HrTime)) {
            searchByWH1.setError("Use 24-hour format!");
            isValid = false;
        }

        if (!(searchByWH2.getText().toString()).matches(regexFor24HrTime)) {
            searchByWH2.setError("Use 24-hour format!");
            isValid = false;
        }

        if (!isValid) return false;

        String hourMinStart = searchByWH1.getText().toString();
        String hourMinEnd = searchByWH2.getText().toString();

        if (!isEndAfterStartTime(hourMinStart, hourMinEnd)) {
            searchByWH2.setError("End time cannot be before start time");
            isValid = false;
        }

//        if (!(valueHourEnd - valueHourStart >= 0)) {

        return isValid;
    }

    private void updateDisplayedBranches() {
        displayedBranches.clear();
        int selectedDay = spinner.getSelectedItemPosition();

        if (validateFields()) {
            String hourMinStart = searchByWH1.getText().toString();
            String hourMinEnd = searchByWH2.getText().toString();

            for (BranchAccount ba : AccountGenerator.branchAccounts) {
                String branchHourStart = ba.getWorkingHours().getStartingHours().get(selectedDay);
                String branchHourEnd = ba.getWorkingHours().getEndingHours().get(selectedDay);

                if (time1WithinTime2(hourMinStart, hourMinEnd, branchHourStart, branchHourEnd)) {
                    displayedBranches.add(ba.getUsername());
                }
            }

        } else {
            for (BranchAccount ba : AccountGenerator.branchAccounts) {
                displayedBranches.add(ba.getUsername());
            }
        }

        availableBranchesAdapter = new NewAvailableServicesList(SearchByDoubleFieldActivity.this, new ArrayList<>(displayedBranches));
        listOfSearchedWH.setAdapter(availableBranchesAdapter);

        Log.d("displayed branches", displayedBranches.toString());
    }

    private void initDisplayedBranches() {
        for (BranchAccount ba : AccountGenerator.branchAccounts) {
            displayedBranches.add(ba.getUsername());
        }
    }

    public static boolean isEndAfterStartTime(String start, String end) {
        return (convertTimeToMinutes(end) - convertTimeToMinutes(start)) >= 0;
    }

    public static boolean time1WithinTime2(String time1Start, String time1End, String time2Start, String time2End) { // time 1 is user input
        int valueTime1HourStart = convertTimeToMinutes(time1Start);
        int valueTime1HourEnd = convertTimeToMinutes(time1End);;
        int valueTime2HourStart = convertTimeToMinutes(time2Start);
        int valueTime2HourEnd = convertTimeToMinutes(time2End);;

        return valueTime2HourStart <= valueTime1HourStart && valueTime1HourEnd <= valueTime2HourEnd;
    }

    public static int convertTimeToMinutes(String time) {
        String[] splitTime = time.split(":");
        return (Integer.parseInt(splitTime[0]) * 60) + Integer.parseInt(splitTime[1]);
    }

}
