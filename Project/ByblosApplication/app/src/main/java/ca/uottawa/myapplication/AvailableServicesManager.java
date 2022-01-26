package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvailableServicesManager extends AppCompatActivity {
    ListView listViewAvailableServices;
    ListView listViewNewAvailableServices;
    TextView noServicesMessage;
    ViewFlipper availableServicesViewFlipper;

    String branchName;
    ArrayList<String> currentAvailableServices = new ArrayList<>();
    ArrayList<String> allAvailableServices = new ArrayList<>();

    // branchAvailableServicesTextView
    AvailableServicesList availableServicesAdapter;
    NewAvailableServicesList allAvailableServicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services_manager);

        availableServicesViewFlipper = (ViewFlipper) findViewById(R.id.availableServicesViewFlipper); // get the reference of ViewFlipper

        Bundle extras = getIntent().getExtras();
        branchName = extras.getString("branch_name");

        currentAvailableServices = findServices(branchName);

        refreshAllAvailableServices();

        listViewAvailableServices = (ListView) findViewById(R.id.listViewAvailableServices);
        listViewNewAvailableServices = (ListView) findViewById(R.id.listViewNewAvailableServices);

        noServicesMessage = findViewById(R.id.noServicesMessage);
        setNoServicesMessage();

        availableServicesAdapter = new AvailableServicesList(AvailableServicesManager.this, new ArrayList<>(currentAvailableServices));
        allAvailableServicesAdapter = new NewAvailableServicesList(AvailableServicesManager.this, new ArrayList<>(allAvailableServices));

        listViewAvailableServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String service = currentAvailableServices.get(position);
                confirmDelete(service);
            }
        });

        listViewNewAvailableServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = allAvailableServices.get(position);
                confirmAdd(serviceName);
            }
        });

        listViewAvailableServices.setAdapter(availableServicesAdapter);
        listViewNewAvailableServices.setAdapter(allAvailableServicesAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setNoServicesMessage();
    }



    private void setNoServicesMessage() {
        if (currentAvailableServices.size() == 0) {
            noServicesMessage.setVisibility(View.VISIBLE);
        } else {
            noServicesMessage.setVisibility(View.GONE);
        }
    }


    public void addNewService(View view) {
        availableServicesViewFlipper.showNext();
    }

    public void cancelNewService(View view) {
        availableServicesViewFlipper.showPrevious();
    }


    // Sends request to database to update services
    public void save(View view) {
        String currentAccountsString = TextUtils.join("\t", currentAvailableServices);

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("available-services").child(branchName);

        AvailableService product = new AvailableService(branchName, currentAccountsString);
        dR.setValue(product);

        Toast.makeText(getApplicationContext(), "Available services updated!", Toast.LENGTH_LONG).show();
        finish();
    }



    private void confirmDelete(String serviceName) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(AvailableServicesManager.this).create();
        alertDialog1.setTitle("Confirm"); // Setting Dialog Title
        alertDialog1.setMessage("Are you sure you want to delete this service?"); // Setting Dialog Message
        alertDialog1.setCancelable(true);

        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Update shown current services
                currentAvailableServices.remove(serviceName);
                availableServicesAdapter = new AvailableServicesList(AvailableServicesManager.this, new ArrayList<>(currentAvailableServices));
                listViewAvailableServices.setAdapter(availableServicesAdapter);

                refreshAllAvailableServices();
                allAvailableServicesAdapter = new NewAvailableServicesList(AvailableServicesManager.this, new ArrayList<>(allAvailableServices));
                listViewNewAvailableServices.setAdapter(allAvailableServicesAdapter);

                setNoServicesMessage();

                // Executed after dialog closed
                Toast.makeText(getApplicationContext(), "Removed available service!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog1.show(); // Showing Alert Message
    }



    private void confirmAdd(String serviceName) {
        // Update shown current services
        currentAvailableServices.add(serviceName);
        availableServicesAdapter = new AvailableServicesList(AvailableServicesManager.this, new ArrayList<>(currentAvailableServices));
        listViewAvailableServices.setAdapter(availableServicesAdapter);

        // Update all available current services
        allAvailableServices.remove(serviceName);
        allAvailableServicesAdapter = new NewAvailableServicesList(AvailableServicesManager.this, new ArrayList<>(allAvailableServices));
        listViewNewAvailableServices.setAdapter(allAvailableServicesAdapter);

        availableServicesViewFlipper.showPrevious();
        setNoServicesMessage();
    }



    ArrayList<String> findServices(String employeeName) {
        for (AvailableService as : AccountGenerator.availableServices) {
            if (as.getUsername().equals(employeeName))
                return new ArrayList<>(Arrays.asList(as.getListOfServices().split("\t")));
        }
        return new ArrayList<>();
    }

    private void refreshAllAvailableServices() {
        this.allAvailableServices = new ArrayList<>();
        for (Service service : AccountGenerator.services) {
            if (!this.currentAvailableServices.contains(service.serviceName)) {
                allAvailableServices.add(service.serviceName);
            }
        }
        Log.d("remaining_available_s", allAvailableServices.toString());
    }

    private boolean existingAccount(String branchName) {
        for (AvailableService as : AccountGenerator.availableServices) {
            if (as.getUsername().equals(branchName)){
                return true;
            }
        }
        return false;
    }

}
