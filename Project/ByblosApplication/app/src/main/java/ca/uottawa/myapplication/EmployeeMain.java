package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeMain extends AppCompatActivity {

    Button manageServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        //ServiceRequest sr = new ServiceRequest("Best Branch", "americanfriend", "2020-06-08", "Car rental", "america\tfirst\t2001-06-08\t"+new Address("1600 Pennsylvania Avenue", "", "Washington", "DC", "United States of America", "20500")+"\tamerica@first.usa\tG\tTruck\t2020-06-09\t2020-06-18");
        //databaseServiceRequests = FirebaseDatabase.getInstance().getReference("service-requests");
        //databaseServiceRequests.child(sr.getRequestID()).setValue(sr);

        Bundle extras = getIntent().getExtras();
        String branchName = extras.getString("branch_name");
        // String role = extras.getString("role");

        TextView branch = findViewById(R.id.branchName);
        branch.setText(branchName);

        Button btnConfirmEdit = (Button) findViewById(R.id.editProfile);
        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BranchProfileEditor.class);
                intent.putExtra("branch_name", branchName);
                startActivity(intent);
            }
        });

        Button btnManageServiceRequests = (Button) findViewById(R.id.serviceRequests);
        btnManageServiceRequests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeServiceRequestManagementActivity.class);
                intent.putExtra("branch_name", branchName);
                startActivity(intent);
            }
        });

        Button btnHours = (Button) findViewById(R.id.setWorkingHours);
        btnHours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeWorkingHoursActivity.class);
                intent.putExtra("branch_name", branchName);
                startActivity(intent);
            }
        });

        manageServices = (Button) findViewById(R.id.manageServices);

        manageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new page for customer
                Intent intent = new Intent(getApplicationContext(), AvailableServicesManager.class);
                intent.putExtra("branch_name", getEmployeeUsername(branchName));
                startActivity(intent);
            }
        });

    }

    public String getEmployeeUsername (String branchName){
        for (BranchAccount ba : AccountGenerator.branchAccounts){
            if ((ba.getBranchName() != null && ba.getBranchName().equals(branchName))) return ba.getUsername();
        }
        return branchName;
    }

}