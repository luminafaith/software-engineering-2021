package ca.uottawa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeeServiceRequestManagementActivity extends AppCompatActivity {

    ArrayList<ServiceRequest> services;
    String branchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_service_request_management);

        Bundle extras = getIntent().getExtras();
        branchName = extras.getString("branch_name");

        services = getBranchRequests();

        LinearLayout servicesLayout = (LinearLayout) findViewById(R.id.requests);
        LinearLayout.LayoutParams tiP = new LinearLayout.LayoutParams(
                dpToPx(200),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams teP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        teP.gravity = Gravity.RIGHT;

        tiP.setMargins(dpToPx(10), 0, 0, 0);
        tiP.gravity = Gravity.CENTER;

        for (ServiceRequest s : services) {
            generateServiceLayoutElement(s, servicesLayout, tiP, teP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        services = getBranchRequests();

        LinearLayout servicesLayout = (LinearLayout) findViewById(R.id.requests);

        servicesLayout.removeAllViews();

        LinearLayout.LayoutParams tiP = new LinearLayout.LayoutParams(
                dpToPx(200),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams teP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        teP.gravity = Gravity.RIGHT;

        tiP.setMargins(dpToPx(10), 0, 0, 0);
        tiP.gravity = Gravity.CENTER;

        for (ServiceRequest s : services) {
            generateServiceLayoutElement(s, servicesLayout, tiP, teP);
        }
    }

    private ArrayList<ServiceRequest> getBranchRequests() {
        ArrayList<ServiceRequest> branchRequests = new ArrayList<>();
        for (ServiceRequest s : AccountGenerator.serviceRequests) {
            if (s.getBranch().getBranchName() != null && s.getBranch().getBranchName().equals(branchName)) { // && s.getStatus().equals("PENDING")
                branchRequests.add(s);
            }
        }
        return branchRequests;
    }

    private void generateServiceLayoutElement(ServiceRequest s, LinearLayout fieldsLayout, LinearLayout.LayoutParams titleParams, LinearLayout.LayoutParams textParams) {

        LinearLayout hl = new LinearLayout(this);
        hl.setOrientation(LinearLayout.HORIZONTAL);
        TextView fieldTitle = new TextView(this);
        Button examineBtn = new Button(this);

        fieldTitle.setLayoutParams(titleParams);
        examineBtn.setText(s.getStatus().equals("PENDING") ? "EXAMINE" : "VIEW");
        examineBtn.setLayoutParams(textParams);
        examineBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                examineService(s);
            }
        });
        fieldTitle.setText(s.getServiceType().getServiceName());

        hl.addView(fieldTitle);
        hl.addView(examineBtn);

        fieldsLayout.addView(hl);
    }

    private Service retrieveService(String key) {
        boolean serviceFound = false;

        if (!serviceFound) {
            for (Service currService : AccountGenerator.services) {
                if (key.equals(currService.getServiceName())) {
                    serviceFound = true;
                    Log.d("Service name", currService.getServiceName());
                    Log.d("Service fields", currService.getFieldNamesAsString());
                    return currService;
                }
            }
        }
        return (new Service());
    }

    private void examineService(ServiceRequest s) {
        Intent intent = new Intent(getApplicationContext(), EmployeeServiceExamineActivity.class);
        intent.putExtra("service_request", s.getRequestID());
        startActivity(intent);
    }

    private int dpToPx(int dps) {
        return (int) (dps * this.getApplicationContext().getResources().getDisplayMetrics().density +0.5f);
    }
}