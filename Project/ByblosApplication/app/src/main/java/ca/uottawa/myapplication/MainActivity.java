package ca.uottawa.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.app.AlertDialog;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseBranchAccounts;
    DatabaseReference databaseCustomerAccounts;
    DatabaseReference databaseServices;
    DatabaseReference databaseServiceRequests;
    DatabaseReference databaseAvailableServices;

    static boolean testingServices = false;
    static String servicesNode = testingServices ? "extra-services" : "services";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Always initialize the application with an admin account
        // This allows the admin to log in without registering
        AdminAccount adminAccount = new AdminAccount();
        AccountGenerator.localAccounts.add(adminAccount);

        // Always initialize the application with an employee account
        // This allows an arbitrary employee to log in without registering
        /*
        BranchAccount employeeAccount = new BranchAccount("employee", "employee", "awesome_employee@gmail.com", new WorkingHours("09:00", "17:00"), "Best Branch");
        AccountGenerator.localAccounts.add(employeeAccount);
        */

        CustomerAccount ca = new CustomerAccount("test", "test2", "test@tester.test", "1970-01-01", "Test", "Tester");
        Service s = new Service("Test", 5.2f, new String[] {"Test"});
        AccountGenerator.services.add(s);
        /* (test)
        BranchAccount branchAccountTemp = (BranchAccount) AccountGenerator.generate("test", "test", "test", AccountGenerator.AccountType.BRANCH);
        System.out.println(branchAccountTemp.getRole());
        */

        //// Firebase stuff
        // Alternative was addListenerForSingleValueEvent, revert if issues

        // Allocate listeners for branch accounts
        try {
            databaseBranchAccounts = FirebaseDatabase.getInstance().getReference("branch-accounts"); // This can use client
            databaseBranchAccounts.addValueEventListener(branchValueEventListener);
        } catch (Exception ignored) {
        }


        // Allocate listeners for customer accounts
        try {
            databaseCustomerAccounts = FirebaseDatabase.getInstance().getReference("customer-accounts"); // This can use client
            databaseCustomerAccounts.addValueEventListener(customerValueEventListener);
        } catch (Exception ignored) {
        }

        // Get services
        try {
            databaseServices = FirebaseDatabase.getInstance().getReference(servicesNode); // This can use client
            databaseServices.addValueEventListener(servicesValueEventListener);
        } catch (Exception ignored) {
        }

        // Get services
        try {
            databaseServiceRequests = FirebaseDatabase.getInstance().getReference("service-requests"); // This can use client
            databaseServiceRequests.addValueEventListener(serviceRequestsValueEventListener);

        } catch (Exception ignored) {
        }

        // Get services
        try {
            databaseAvailableServices = FirebaseDatabase.getInstance().getReference("available-services"); // This can use client
            databaseAvailableServices.addValueEventListener(availableServicesEventListener);

        } catch (Exception ignored) {
        }

        //databaseBranchAccounts.child(employeeAccount.getBranchName()).setValue(employeeAccount);

        ////

    }

    ValueEventListener branchValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                AccountGenerator.branchAccounts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BranchAccount account = dataSnapshot.getValue(BranchAccount.class);
                    if (account != null && account.getUsername() != null && account.getBranchName() != null)
                        AccountGenerator.branchAccounts.add(account);
                }
            }
            // if (AccountGenerator.accountsAdapter != null) AccountGenerator.accountsAdapter.notifyDataSetChanged();
            Log.d("Branch Accounts", AccountGenerator.branchAccounts.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    ValueEventListener customerValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            AccountGenerator.customerAccounts.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CustomerAccount account = dataSnapshot.getValue(CustomerAccount.class);
                    AccountGenerator.customerAccounts.add(account);
                }
            }
            // if (AccountGenerator.accountsAdapter != null) AccountGenerator.accountsAdapter.notifyDataSetChanged();
            // Log.d("Customer Accounts", AccountGenerator.customerAccounts.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    ValueEventListener serviceRequestsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            AccountGenerator.serviceRequests.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ServiceRequest request = dataSnapshot.getValue(ServiceRequest.class);
                    AccountGenerator.serviceRequests.add(request);
                }
            }
            // if (AccountGenerator.accountsAdapter != null) AccountGenerator.accountsAdapter.notifyDataSetChanged();
            // Log.d("Customer Accounts", customerAccounts.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    ValueEventListener servicesValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            AccountGenerator.services.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Service service = dataSnapshot.getValue(Service.class);
                        AccountGenerator.services.add(service);
                    } catch (Exception ignored) {
                    }
                }
            }
            Log.d("Services", AccountGenerator.services.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    ValueEventListener availableServicesEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            AccountGenerator.availableServices.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        AvailableService service = dataSnapshot.getValue(AvailableService.class);
                        AccountGenerator.availableServices.add(service);
                    } catch (Exception ignored) {
                    }
                }
            }
            Log.d("main_available_services", AccountGenerator.availableServices.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };



    public void validateLogin(View view) throws NoSuchAlgorithmException {

        EditText usernameRaw = findViewById(R.id.loginNameField);
        String username = usernameRaw.getText().toString().trim();

        EditText passwordRaw = findViewById(R.id.loginPassField);
        String password = passwordRaw.getText().toString().trim();

        boolean accountFound = false;

        for (Account account : AccountGenerator.localAccounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                accountFound = true;
                flushPassword();
                Intent intent = null;
                if (account instanceof AdminAccount) {
                    intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                } else if (account instanceof BranchAccount) {
                    intent = new Intent(getApplicationContext(), EmployeeMain.class);
                    intent.putExtra("branch_name", ((BranchAccount) account).getBranchName());
                    intent.putExtra("role", account.getRole());
                }
                startActivity(intent);
            }
        }

        if (!accountFound) {
            for (BranchAccount branchAccount : AccountGenerator.branchAccounts) {
                if (username.equals(branchAccount.getUsername()) &&
                        AccountGenerator.hashPassword(password).equals(branchAccount.getPassword())) {
                    accountFound = true;
                    flushPassword();
                    // Intent intent = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
                    // intent.putExtra("username", username);
                    // intent.putExtra("role", branchAccount.getRole());
                    Intent intent = new Intent(getApplicationContext(), EmployeeMain.class);
                    intent.putExtra("branch_name", ((BranchAccount) branchAccount).getBranchName());
                    intent.putExtra("role", branchAccount.getRole());
                    startActivity(intent);
                }
            }
        }

        if (!accountFound) {
            for (CustomerAccount customerAccount : AccountGenerator.customerAccounts) {
                if (username.equals(customerAccount.getUsername()) &&
                        AccountGenerator.hashPassword(password).equals(customerAccount.getPassword())) {
                    accountFound = true;
                    flushPassword();
                    Intent intent = new Intent(getApplicationContext(), CustomerMain.class);
                    intent.putExtra("username", username);
                    intent.putExtra("role", customerAccount.getRole());
                    startActivity(intent);
                }
            }
        }

        if (!accountFound) {
            // Source: https://www.protechtraining.com/blog/post/example-show-alert-dialog-in-android-618
            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog1.setTitle("Error"); // Setting Dialog Title
            alertDialog1.setMessage("Invalid login, please try again!"); // Setting Dialog Message

            // Setting OK Button
            alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Executed after dialog closed
                    // Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog1.show(); // Showing Alert Message
        }
    }

    private void flushPassword() {
        EditText passwordField = findViewById(R.id.loginPassField);
        passwordField.getText().clear();
    }

    public void switchToRegisterBranch(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterBranchActivity.class);
        startActivity(intent);
    }

    public void switchToRegisterUser(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterCustomerActivity.class);
        startActivity(intent);
    }

//    public void switchToBranchEditor(View view) {
//        Intent intent = new Intent(getApplicationContext(), BranchProfileEditor.class);
//        startActivity(intent);
//    }

    // maybe remove
    public ArrayList<Service> getAvailableServices(BranchAccount ba) {
        AvailableService availableService = new AvailableService();
        for (AvailableService as : AccountGenerator.availableServices) {
            if (as.getUsername().equals(ba.getUsername())) {
                availableService = as;
                break;
            }
        }
        ArrayList<String> allServices = new ArrayList<>(Arrays.asList(availableService.getListOfServices().split("\t")));
        ArrayList<Service> selectedServices = new ArrayList<>();
        for (Service s : AccountGenerator.services) {
            if (allServices.contains(s.getServiceName())) {
                selectedServices.add(s);
            }
        }
        return selectedServices;
    }

}
