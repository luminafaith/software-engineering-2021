package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AvailableServices extends AppCompatActivity {
    Button addServiceButton;
    ListView listViewNewAvailableServices;

    // branchAvailableServicesTextView
    AvailableServicesList availableServicesAdapter;

    ArrayList<String> availableServices = new ArrayList<>(Arrays.asList(new String[] {"service1", "service2"}));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_services_manager);

        listViewNewAvailableServices = (ListView) findViewById(R.id.listViewAccounts);

        listViewNewAvailableServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String service = availableServices.get(position);
                confirmDelete(service);

            }
        });

        listViewNewAvailableServices.setAdapter(availableServicesAdapter);
    }

    // Sends request to database to update services
    private void save() {

    }

    private void confirmDelete(String serviceName) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(AvailableServices.this).create();
        alertDialog1.setTitle("Confirm"); // Setting Dialog Title
        alertDialog1.setMessage("Are you sure you want to delete this service?"); // Setting Dialog Message
        alertDialog1.setCancelable(true);

        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("test", serviceName);

                Log.d("Branch Accounts", availableServices.toString());

                // Executed after dialog closed
                Toast.makeText(getApplicationContext(), "Deleted Account", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog1.show(); // Showing Alert Message

    }

}