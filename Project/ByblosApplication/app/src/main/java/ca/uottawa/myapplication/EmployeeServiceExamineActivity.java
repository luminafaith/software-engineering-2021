package ca.uottawa.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeeServiceExamineActivity extends AppCompatActivity {
    DatabaseReference databaseServiceRequests;
    ServiceRequest serviceSelected;
    Button btnAccept, btnRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseServiceRequests = FirebaseDatabase.getInstance().getReference("service-requests");
        setContentView(R.layout.activity_employee_service_examine);

        Bundle extras = getIntent().getExtras();
        String requestId = extras.getString("service_request");

        serviceSelected = retrieveServiceRequest(requestId);

        btnAccept = (Button) findViewById(R.id.btnAccept);

        btnRefuse = (Button) findViewById(R.id.btnRefuse);



        TextView serviceTitle = findViewById(R.id.serviceTitle);
        serviceTitle.setText(serviceSelected.getServiceType().getServiceName());

        if (serviceSelected.getStatus().equals("PENDING")) {
            btnAccept.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setStatus(true);
                    finish();
                }
            });


            btnRefuse.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setStatus(false);
                    finish();
                }
            });
        } else {
            serviceTitle.setText(serviceSelected.getServiceType().getServiceName() + " (" + serviceSelected.getStatus() + ")");
            ((LinearLayout) (serviceTitle.getParent())).removeViewAt(2);
            ((LinearLayout) (serviceTitle.getParent())).removeViewAt(1);
        }

        ArrayList<Field> fields = serviceSelected.getFields();
        LinearLayout fieldsLayout = (LinearLayout) findViewById(R.id.fields);
        LinearLayout.LayoutParams tiP = new LinearLayout.LayoutParams(
                dpToPx(200),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams teP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        tiP.setMargins(dpToPx(10), 0, 0, 0);

        for (Field f : fields) {
            generateFieldLayoutElement(f, fieldsLayout, tiP, teP);
        }
    }

    private void generateFieldLayoutElement(Field f, LinearLayout fieldsLayout, LinearLayout.LayoutParams titleParams, LinearLayout.LayoutParams textParams) {

        LinearLayout hl = new LinearLayout(this);
        hl.setOrientation(LinearLayout.HORIZONTAL);
        TextView fieldTitle = new TextView(this);
        TextView fieldText = new TextView(this);

        fieldTitle.setLayoutParams(titleParams);
        fieldText.setLayoutParams(textParams);
        fieldText.setEms(15);

        fieldTitle.setText(f.getName());
        fieldText.setText(f.getFieldValue());

        hl.addView(fieldTitle);
        hl.addView(fieldText);
        fieldsLayout.addView(hl);
    }

    private ServiceRequest retrieveServiceRequest(String key) {
        for (ServiceRequest req : AccountGenerator.serviceRequests) {
            if (key.equals(req.getRequestID())) {
                return req;
            }
        }
        return null;
    }

    void setStatus (boolean status) {
        String s = (status) ? "ACCEPTED" : "DENIED";
        serviceSelected.setStatus(s);
        databaseServiceRequests.child(serviceSelected.getRequestID()).setValue(serviceSelected);
        finish();
    }

    private int dpToPx(int dps) {
        return (int) (dps * this.getApplicationContext().getResources().getDisplayMetrics().density +0.5f);
    }
}