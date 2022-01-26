package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CustomerMain extends AppCompatActivity {

    Button searchBranches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Bundle extras = getIntent().getExtras();
        String customerName = extras.getString("username");
        // String role = extras.getString("role");

        TextView customer = findViewById(R.id.customerName);
        customer.setText(customerName);

        Spinner spinner = (Spinner) findViewById(R.id.searchTypes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_params, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        searchBranches = (Button) findViewById(R.id.searchBranches);
        searchBranches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedAttribute = spinner.getSelectedItem().toString();
                Log.d("search branches by", selectedAttribute);

                switch (selectedAttribute) {
                    case "address": {
                        Intent intent = new Intent(getApplicationContext(), SearchBySingleField.class);
                        intent.putExtra("customerName", customerName);
                        intent.putExtra("attributeType", "address");
                        startActivity(intent);
                        break;
                    }
                    case "working hours": {
                        Intent intent = new Intent(getApplicationContext(), SearchByDoubleFieldActivity.class);
                        intent.putExtra("customerName", customerName);
                        intent.putExtra("attributeType", "working hours");
                        startActivity(intent);
                        break;
                    }
                    case "type of service": {
                        Intent intent = new Intent(getApplicationContext(), SearchBySingleField.class);
                        intent.putExtra("customerName", customerName);
                        intent.putExtra("attributeType", "type of service");
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }
}
