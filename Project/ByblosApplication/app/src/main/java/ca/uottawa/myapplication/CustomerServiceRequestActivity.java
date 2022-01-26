package ca.uottawa.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class CustomerServiceRequestActivity extends AppCompatActivity {
    DatabaseReference databaseServiceRequests;
    Service service;
    ServiceRequest serviceSelected;
    ArrayList<EditText> textFields;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseServiceRequests = FirebaseDatabase.getInstance().getReference("service-requests");
        setContentView(R.layout.activity_customer_service_form);

        Bundle extras = getIntent().getExtras();
        String serviceName = extras.getString("service");
        String branchName  = extras.getString("branch");
        BranchAccount ba = retrieveBranchAccount(branchName);
        String customerUser  = extras.getString("customer");

        service = retrieveService(serviceName);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        serviceSelected = new ServiceRequest(ba.getBranchName(), customerUser, df.format(new Date()), service);

        btnSave = (Button) findViewById(R.id.btnAccept);

        TextView serviceTitle = findViewById(R.id.serviceTitle);
        serviceTitle.setText(serviceSelected.getServiceType().getServiceName() + " (Hourly Cost: $" + serviceSelected.getServiceType().getHourlyCost() + ")");

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendService();
            }
        });

        ArrayList<Field> fields = serviceSelected.getFields();
        textFields = new ArrayList<>();
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
        EditText fieldText = new EditText(this);

        fieldTitle.setLayoutParams(titleParams);
        fieldText.setLayoutParams(textParams);
        fieldText.setEms(20);
        textFields.add(fieldText);

        fieldTitle.setText(sanitizeFieldName(f.getName()));

        hl.addView(fieldTitle);
        hl.addView(fieldText);
        fieldsLayout.addView(hl);
    }

    private String sanitizeFieldName(String f) {
        String output = "";
        String[] titleSplit = f.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        String firstLetter = titleSplit[0].substring(0, 1);
        String remLetters = titleSplit[0].substring(1);
        titleSplit[0] = firstLetter.toUpperCase() + remLetters;
        for (String s : titleSplit) output += s + " ";
        return output;
    }

    private Service retrieveService(String key) {
        for (Service req : AccountGenerator.services) if (key.equals(req.getServiceName())) return req;
        return null;
    }

    private void sendService() {
        boolean valid = validateAllFields();
        if (valid) {
            setValues(serviceSelected);
            databaseServiceRequests.child(serviceSelected.getRequestID()).setValue(serviceSelected);
            setValues(serviceSelected);
            finish();
        }
    }

    private void setValues(ServiceRequest serviceSelected) {
        ArrayList<Field> fields = serviceSelected.getFields();
        for (int i = 0; i < fields.size() ; i++) {
            fields.get(i).setFieldValue(textFields.get(i).getText().toString());
        }
        serviceSelected.setFields(fields);
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        for (EditText et : textFields) if (TextUtils.isEmpty(et.getText().toString().trim())) {
            et.setError("Mandatory field!");
            isValid = false;
        }

        return isValid;
    }

    private int dpToPx(int dps) {
        return (int) (dps * this.getApplicationContext().getResources().getDisplayMetrics().density +0.5f);
    }

    private BranchAccount retrieveBranchAccount(String bn) {
        for (BranchAccount ba : AccountGenerator.branchAccounts) if (ba.getUsername().equals(bn)) return ba;
        return null;
    }
}