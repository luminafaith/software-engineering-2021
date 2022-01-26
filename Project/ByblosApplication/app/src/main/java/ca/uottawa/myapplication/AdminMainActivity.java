package ca.uottawa.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {

    Button btnCustomerReg;
    Button btnBranchReg;
    Button btnRefresh;
    ImageButton btnAddService;

    FirebaseDatabase database;

    ListView listViewServices;
    ArrayList<Service> tmpServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnCustomerReg = (Button) findViewById(R.id.customerReg);
        btnBranchReg = (Button) findViewById(R.id.branchReg);
        btnRefresh = (Button) findViewById(R.id.refresh);
        btnAddService = (ImageButton) findViewById(R.id.addService);

        listViewServices = (ListView) findViewById(R.id.listViewServices);

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = AccountGenerator.services.get(i);
                showUpdateDeleteDialog(service.getServiceName());
                return true;
            }
        });

        // Add adapter
        ServiceList serviceAdapter = new ServiceList(AdminMainActivity.this, AccountGenerator.services);
        listViewServices.setAdapter(serviceAdapter);

        tmpServices = new ArrayList<>();

        // STUB: adding Service objects for testing, remove after FireBase is integrated
        // // Car Rental
        tmpServices.add(new Service("Car Rental", 30.0f, new String[]{
                "First Name",
                "Last Name",
                "Date of Birth",
                "Address",
                "E-mail",
                "License Type",
                "Preferred Car Type",
                "Pickup Date and Time",
                "Return Date and Time"
        }));

        // // Truck Rental
        tmpServices.add(new Service("Truck Rental", 40.0f, new String[]{
                "First Name",
                "Last Name",
                "Date of Birth",
                "Address",
                "E-mail",
                "License Type",
                "Pickup Date and Time",
                "Return Date and Time",
                "Maximum Distance (km)",
                "Area of Operation"
        }));

        // // Moving Assistance
        tmpServices.add(new Service("Moving Assistance", 25.0f, new String[]{
                "First Name",
                "Last Name",
                "Date of Birth",
                "Address",
                "E-mail",
                "Moving Start Location",
                "Moving End Location",
                "Number of Movers Required",
                "Expected Number of Boxes"
        }));


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: make sure a real refresh is not necessary
                // database.getReference(MainActivity.servicesNode);
                reloadPage();
            }
        });


        btnCustomerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new page for customer
                Intent intent = new Intent(getApplicationContext(), AdminAccountManagerActivity.class);
                intent.putExtra("account_type", "customer");
                startActivity(intent);
            }
        });

        btnBranchReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new page for branch
                Intent intent = new Intent(getApplicationContext(), AdminAccountManagerActivity.class);
                intent.putExtra("account_type", "branch");
                startActivity(intent);
            }
        });

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send to add service page
                Intent intent = new Intent(getApplicationContext(), AdminServiceAddActivity.class);
                startActivity(intent);
            }
        });
    }


    private void showUpdateDeleteDialog(final String productId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle(productId);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                Intent intent = new Intent(getApplicationContext(), AdminServiceEditActivity.class);
                intent.putExtra("service_key", productId);
                startActivity(intent);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(productId);
                b.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private boolean deleteService(String id) {
        try {
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference(MainActivity.servicesNode).child(id);
            dR.removeValue();
            // Success
            Toast.makeText(getApplicationContext(), "Service Deleted", Toast.LENGTH_LONG).show();
            reloadPage();
            return true;
        } catch (Exception e) {
            // Fail
            return false;
        }
    }

    private void reloadPage() {
        finish();
        startActivity(getIntent());
    }
}
