package ca.uottawa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerBranchRateActivity extends AppCompatActivity {

    Button button1, button2, button3, button4, button5;
    String branchUsername, customerUsername;
    DatabaseReference databaseRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_branch_rate);

        databaseRatings = FirebaseDatabase.getInstance().getReference("branch-ratings");

        //ServiceRequest sr = new ServiceRequest("Best Branch", "americanfriend", "2020-06-08", "Car rental", "america\tfirst\t2001-06-08\t"+new Address("1600 Pennsylvania Avenue", "", "Washington", "DC", "United States of America", "20500")+"\tamerica@first.usa\tG\tTruck\t2020-06-09\t2020-06-18");
        //databaseServiceRequests = FirebaseDatabase.getInstance().getReference("service-requests");
        //databaseServiceRequests.child(sr.getRequestID()).setValue(sr);

        Bundle extras = getIntent().getExtras();
        branchUsername = extras.getString("branch_username");
        customerUsername = extras.getString("customer_username");
        // String role = extras.getString("role");

        String branchName = "NULL";

        for (BranchAccount ba : AccountGenerator.branchAccounts) {
            if (ba.getUsername().equals(branchUsername)) branchName = ba.getBranchName();
        }

        TextView branch = findViewById(R.id.branchName);
        branch.setText(branchName);

        button1 = findViewById(R.id.rateBranch);
        button2 = findViewById(R.id.rateBranch2);
        button3 = findViewById(R.id.rateBranch3);
        button4 = findViewById(R.id.rateBranch4);
        button5 = findViewById(R.id.rateBranch5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchRating br = new BranchRating(branchUsername, customerUsername, 1);
                databaseRatings.child(br.generateKey()).setValue(br);
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchRating br = new BranchRating(branchUsername, customerUsername, 2);
                databaseRatings.child(br.generateKey()).setValue(br);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchRating br = new BranchRating(branchUsername, customerUsername, 3);
                databaseRatings.child(br.generateKey()).setValue(br);
                finish();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchRating br = new BranchRating(branchUsername, customerUsername, 4);
                databaseRatings.child(br.generateKey()).setValue(br);
                finish();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchRating br = new BranchRating(branchUsername, customerUsername, 5);
                databaseRatings.child(br.generateKey()).setValue(br);
                finish();
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