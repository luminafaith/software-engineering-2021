package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CustomerChooseAction extends AppCompatActivity {

    Button createServiceRequest, rateBranch;
    String branchUsername, customerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_choose_action);

        Bundle extras = getIntent().getExtras();
        branchUsername = extras.getString("branch_username");
        customerUsername = extras.getString("customer_username");

        createServiceRequest = (Button) findViewById(R.id.createServiceRequest);
        createServiceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceRequestServiceSelectActivity.class);
                intent.putExtra("branch_username", branchUsername);
                intent.putExtra("customer_username", customerUsername);
                startActivity(intent);
                finish();
            }
        });

        rateBranch = (Button) findViewById(R.id.rateBranch);
        rateBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerBranchRateActivity.class);
                Log.d("Rate branch", branchUsername + " - " + customerUsername);
                intent.putExtra("branch_username", branchUsername);
                intent.putExtra("customer_username", customerUsername);
                startActivity(intent);
                finish();
            }
        });
    }
}