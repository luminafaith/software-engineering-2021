package ca.uottawa.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.NumberUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;

public class AdminServiceAddActivity extends AppCompatActivity {

    Button btnAddField, btnAddService;
    Service service;
    private DatabaseReference databaseServices;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_add);

        btnAddField = (Button) findViewById(R.id.addField);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addField(v);
            }
        });

        btnAddService = (Button) findViewById(R.id.addServiceButton);
        btnAddService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleServiceCreation();
                finish();
            }
        });

        // establishing database reference on right path
        databaseServices = FirebaseDatabase.getInstance().getReference(MainActivity.servicesNode);
    }

    public void addField(View view){
        //add field text and field editText, insert it above button in layout
        LinearLayout ll = (LinearLayout) ((LinearLayout) view.getParent()).findViewById(R.id.serviceFieldsLL);
        LinearLayout hl = new LinearLayout(this);
        hl.setOrientation(LinearLayout.HORIZONTAL);
        EditText field = new EditText(this);
        ImageButton delete = new ImageButton(this);
        delete.setOnClickListener(fieldDelete);
        delete.setImageResource(R.drawable.trash_can_icon);
        delete.setMaxWidth(dpToPx(20));
        delete.setMaxHeight(dpToPx(20));
        delete.setAdjustViewBounds(true);
        field.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 9.0f));
        hl.addView(field);
        hl.addView(delete);
        ll.addView(hl);
    }

    private View.OnClickListener fieldDelete = new View.OnClickListener() {
        public void onClick(View v) {
            LinearLayout ll = (LinearLayout) (((LinearLayout)((LinearLayout)v.getParent()).getParent()).findViewById(R.id.serviceFieldsLL));
            LinearLayout target = (LinearLayout) (v.getParent());
            ll.removeView(target);
        }
    };

    public Service generateServiceFromFields() {
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.serviceFieldsLL);
        String name, costS;
        float cost;
        ArrayList<String> fieldNames = new ArrayList<>();

        int startingIndex = ll.indexOfChild(ll.findViewById(R.id.addField)) + 1;
        int endingIndex = ll.getChildCount();

        name = ((EditText) ll.findViewById(R.id.serviceNameField)).getText().toString();
        costS = ((EditText) ll.findViewById(R.id.serviceCostField)).getText().toString();

        // fields have already been validated prior to function being called, so no need to catch parse errors
        cost = (Float.parseFloat(costS));

        // Retrieves input from additional fields and adds them to a
        for (int i = startingIndex; i < endingIndex; i++) {
            LinearLayout fieldLayout = (LinearLayout) ll.getChildAt(i);
            EditText fieldView = (EditText) fieldLayout.getChildAt(0); // gets text field
            fieldNames.add(fieldView.getText().toString());
        }

        return new Service(name, cost, fieldNames.toArray(new String[0]));
    }

    private void handleServiceCreation() {
        // initialize variables
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.serviceFieldsLL);
        String name, costS;

        int startingIndex = ll.indexOfChild(ll.findViewById(R.id.addField)) + 1;
        int endingIndex = ll.getChildCount();

        name = ((EditText) ll.findViewById(R.id.serviceNameField)).getText().toString().trim();
        costS = ((EditText) ll.findViewById(R.id.serviceCostField)).getText().toString().trim();


        // Checks for valid input in fields
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            // Checks if serviceNameField is empty
            ((EditText) ll.findViewById(R.id.serviceNameField)).setError("Name is required.");
            isValid = false;
        }
        if (TextUtils.isEmpty(costS)) {
            // Checks if serviceCostField is empty
            ((EditText) ll.findViewById(R.id.serviceCostField)).setError("Cost is required.");
            isValid = false;
        } else if (!costS.matches("[0-9]*\\.[0-9]*")) {
            // Checks if inputted text is in float format
            ((EditText) ll.findViewById(R.id.serviceCostField)).setError("Enter the hourly cost as a decimal.");
            isValid = false;
        }
        // Checking additional fields for non-empty input
        for (int i = startingIndex; i < endingIndex; i++) {
            LinearLayout fieldLayout = (LinearLayout) ll.getChildAt(i);
            EditText fieldView = (EditText) fieldLayout.getChildAt(0); // gets text field

            // if the fieldview is empty, then we inform user
            if (TextUtils.isEmpty(fieldView.getText().toString().trim())) {
                // ((EditText) ll.findViewById(fieldView.getId())).setError("Field info is required."); // TODO: send error to empty additional fields (currently crashes with this attempt)
                Toast.makeText(this, "Fill in or remove additional empty fields", Toast.LENGTH_LONG).show(); // TODO: potentially replace with setError if we get it working
                isValid = false;
            }
        }

        // If any of the fields are invalid, return without generating service to force user to input valid information
        if (!isValid) {
            return;
        }

        // Assuming that all fields have valid input, generate the service
        service = generateServiceFromFields();

        // Push the service to the database
        databaseServices.child(service.getServiceName()).setValue(service);
    }

    public int dpToPx(int dps) {
        return (int) (dps * this.getApplicationContext().getResources().getDisplayMetrics().density +0.5f);
    }
}