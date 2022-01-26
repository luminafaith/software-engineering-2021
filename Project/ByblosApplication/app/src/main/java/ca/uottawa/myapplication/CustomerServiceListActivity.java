package ca.uottawa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerServiceListActivity extends AppCompatActivity {

    String branchName, customerUser;
    ArrayList<Service> availServices;
    BranchAccount ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_list);

        Bundle extras = getIntent().getExtras();
        branchName = extras.getString("branch");
        customerUser = extras.getString("customer");
        ba = retrieveBranchAccount(branchName);

        availServices = getAvailableServices(ba);

        TextView branchTitle = findViewById(R.id.branchNameTitle);
        branchTitle.setText(ba.getBranchName() + " - Services");

        LinearLayout servicesLayout = (LinearLayout) findViewById(R.id.services);

        LinearLayout.LayoutParams tiP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        tiP.setMargins(dpToPx(10), 0, 0, 0);

        for (Service s : availServices) {
            generateServiceLayoutElem(s, servicesLayout, tiP);
        }

    }

    private void generateServiceLayoutElem(Service s, LinearLayout servicesLayout, LinearLayout.LayoutParams tiP) {

        TextView serviceTitle = new TextView(this);

        serviceTitle.setLayoutParams(tiP);
        serviceTitle.setText(s.getServiceName());
        serviceTitle.setClickable(true);

        serviceTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestService(s);
            }
        });

        servicesLayout.addView(serviceTitle);

    }

    private void requestService(Service s) {
        Intent intent = new Intent(getApplicationContext(), CustomerServiceRequestActivity.class);
        intent.putExtra("branch", ba.getUsername());
        intent.putExtra("customer", customerUser);
        intent.putExtra("service", s.getServiceName());
        startActivity(intent);
        finish();
    }

    private BranchAccount retrieveBranchAccount(String bn) {
        for (BranchAccount ba : AccountGenerator.branchAccounts) if (ba.getUsername().equals(bn)) return ba;
        return null;
    }

    public ArrayList<Service> getAvailableServices(BranchAccount ba) {
        AvailableService availableService = null;

        for (AvailableService as : AccountGenerator.availableServices) if (as.getUsername().equals(ba.getUsername())) {
            availableService = as;
            break;
        }

        if (availableService != null) {
            ArrayList<String> allServices = new ArrayList<>(
                    Arrays.asList(availableService.getListOfServices().split("\t"))
            );
            ArrayList<Service> selectedServices = new ArrayList<>();
            for (Service s : AccountGenerator.services) if (allServices.contains(s.getServiceName())) selectedServices.add(s);
            return selectedServices;
        }

        return null;
    }

    private int dpToPx(int dps) {
        return (int) (dps * this.getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
