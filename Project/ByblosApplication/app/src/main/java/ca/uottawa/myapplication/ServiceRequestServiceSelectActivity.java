package ca.uottawa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ServiceRequestServiceSelectActivity extends AppCompatActivity {
    ListView listViewAvailableServices;
    String branchUsername, customerUsername;
    ArrayList<String> currentAvailableServices = new ArrayList<>();

//    AvailableServicesList servicesAdapter;
    NewAvailableServicesList servicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_select_service);

        listViewAvailableServices = (ListView) findViewById(R.id.listViewBranchSelect);

        Bundle extras = getIntent().getExtras();
        branchUsername = extras.getString("branch_username");
        customerUsername = extras.getString("customer_username");
//        branchUsername = "branchchurchill";

        currentAvailableServices = findServices(branchUsername);
        // add adapter
//        servicesAdapter = new AvailableServicesList(ServiceRequestServiceSelectActivity.this, new ArrayList<>(currentAvailableServices));

//        listViewAvailableServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String service = currentAvailableServices.get(position);
//                confirmSelect(service, branchUsername, customerUsername);
//                return true;
//            }
//        });

        listViewAvailableServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = currentAvailableServices.get(position);
                Intent intent = new Intent(getApplicationContext(), CustomerServiceRequestActivity.class);
                intent.putExtra("branch", branchUsername);
                intent.putExtra("customer", customerUsername);
                intent.putExtra("service", serviceName);
                startActivity(intent);
                finish();
            }
        });

        servicesAdapter = new NewAvailableServicesList(ServiceRequestServiceSelectActivity.this, new ArrayList<>(currentAvailableServices));

        listViewAvailableServices.setAdapter(servicesAdapter);
    }

    private void confirmSelect(final String serviceSelectedName, final String branchSelected, final String customerRequesting) {
        AlertDialog.Builder alert1 = new AlertDialog.Builder(ServiceRequestServiceSelectActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.service_select_dialog, null);
        alert1.setView(dialogView);

        final Button buttonSelect = (Button) dialogView.findViewById(R.id.btnSelectSelect);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btnSelectCancel);

        alert1.setTitle("Request " + serviceSelectedName.toLowerCase(Locale.ROOT) + "?");
        final AlertDialog b = alert1.create();
        b.show();

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                Intent intent = new Intent(getApplicationContext(), CustomerServiceRequestActivity.class);
                intent.putExtra("branch", branchUsername);
                intent.putExtra("customer", customerUsername);
                intent.putExtra("service", serviceSelectedName);
                startActivity(intent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    ArrayList<String> findServices(String employeeName) {
        for (AvailableService as : AccountGenerator.availableServices) {
            if (as.getUsername().equals(employeeName))
                return new ArrayList<>(Arrays.asList(as.getListOfServices().split("\t")));
        }
        return new ArrayList<>();
    }
}
