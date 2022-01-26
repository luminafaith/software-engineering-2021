package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchBySingleField extends AppCompatActivity {

    ImageButton updateBranches;
    EditText searchByAddressField;

    ListView listOfSearchedAddresses;
    String customerName;
    NewAvailableServicesList availableBranchesAdapter;

    ArrayList<String> displayedBranches = new ArrayList<>();

    public enum SearchType {
        ADDRESS, SERVICE
    }

    ;

    SearchType searchType = SearchType.ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_single_field);

        Bundle extras = getIntent().getExtras();
        customerName = extras.getString("customerName");

        // customer_username
        // branch_username

        String attributeType = extras.getString("attributeType");
        TextView searchBySingleField = findViewById(R.id.searchBySingleField);
        if (attributeType.equals("address")) {
            searchBySingleField.setText("Search by: Address");
            searchType = SearchType.ADDRESS;
        } else if (attributeType.equals("type of service")) {
            searchBySingleField.setText("Search by: Type of Service");
            searchType = SearchType.SERVICE;
        }

        listOfSearchedAddresses = (ListView) findViewById(R.id.listOfSearchedAddresses);
        updateDisplayedBranches("");

        searchByAddressField = findViewById(R.id.searchByAddressField);
        updateBranches = (ImageButton) findViewById(R.id.updateBranches);
        updateBranches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDisplayedBranches(searchByAddressField.getText().toString());
            }
        });

        listOfSearchedAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        availableBranchesAdapter = new NewAvailableServicesList(SearchBySingleField.this, new ArrayList<>(displayedBranches));
        listOfSearchedAddresses.setAdapter(availableBranchesAdapter);
    }

    public void updateDisplayedBranches(String sub) {

        int length = sub.length();

        Set<String> set = new HashSet<String>();
        displayedBranches.clear();

        if (searchType == SearchType.ADDRESS) {

            Log.d("sub", AccountGenerator.branchAccounts.toString());

            for (BranchAccount ba : AccountGenerator.branchAccounts) {
                if (searchSuccess(ba.getAddress().getCountry(), sub) ||
                        searchSuccess(ba.getAddress().getCity(), sub) ||
                        searchSuccess(ba.getAddress().getLine1(), sub) ||
                        searchSuccess(ba.getAddress().getLine2(), sub) ||
                        searchSuccess(ba.getAddress().getProvince(), sub) ||
                        searchSuccess(ba.getAddress().getPostalCode(), sub)
                ) {
                    displayedBranches.add(ba.getUsername());
                }
            }

        } else if (searchType == SearchType.SERVICE) {
            for (AvailableService as : AccountGenerator.availableServices) {
                for (String service : as.getListOfServices().split("\t")) {
                    if (searchSuccess(service, sub)) {
                        set.add(as.getUsername());
                    }
                }
            }
        }

        displayedBranches.addAll(set);

        availableBranchesAdapter = new NewAvailableServicesList(SearchBySingleField.this, new ArrayList<>(displayedBranches));
        listOfSearchedAddresses.setAdapter(availableBranchesAdapter);

        Log.d("displayed branches", displayedBranches.toString());

    }

    public static boolean searchSuccess(String original, String sub) {
        int length = sub.length();
        String s1 = original.substring(0, length).toLowerCase();
        String s2 = sub.toLowerCase();
        return original.length() >= length && original.toLowerCase().contains(sub.toLowerCase());
    }

}
