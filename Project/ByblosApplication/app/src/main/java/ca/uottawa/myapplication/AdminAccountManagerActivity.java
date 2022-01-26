package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminAccountManagerActivity extends AppCompatActivity {

    String accountType;

    ListView listViewAccounts;

    // customerAccountsTextView
    AccountList accountsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account_manager);

        Bundle extras = getIntent().getExtras();
        accountType = extras.getString("account_type");

        TextView accountsTextView = findViewById(R.id.accountsTextView);
        if (accountType.equals("branch")) {
            accountsTextView.setText(R.string.branch_accounts);
            Log.d("omgidk", AccountGenerator.branchAccounts.toString());
            accountsAdapter = new AccountList(AdminAccountManagerActivity.this, new ArrayList<>(AccountGenerator.branchAccounts));
        } else {
            accountsTextView.setText(R.string.customer_accounts);
            accountsAdapter = new AccountList(AdminAccountManagerActivity.this, new ArrayList<>(AccountGenerator.customerAccounts));
        }

        listViewAccounts = (ListView) findViewById(R.id.listViewAccounts);

        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account product;
                if (accountType.equals("branch"))
                    product = AccountGenerator.branchAccounts.get(position);
                else
                    product = AccountGenerator.customerAccounts.get(position);

                confirmDelete(product.getUsername(), accountType);

            }
        });

        listViewAccounts.setAdapter(accountsAdapter);

    }

    private void confirmDelete(String username, String accountType) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(AdminAccountManagerActivity.this).create();
        alertDialog1.setTitle("Confirm"); // Setting Dialog Title
        alertDialog1.setMessage("Are you sure you want to delete this account?"); // Setting Dialog Message
        alertDialog1.setCancelable(true);

        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("test", username);

                DatabaseReference dR;

                if (accountType.equals("branch")) {
                    dR = FirebaseDatabase.getInstance().getReference("branch-accounts").child(username);
                } else {
                    dR = FirebaseDatabase.getInstance().getReference("customer-accounts").child(username);
                }

                dR.removeValue();

                Log.d("Branch Accounts", AccountGenerator.branchAccounts.toString());

                // Executed after dialog closed
                Toast.makeText(getApplicationContext(), "Deleted Account", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog1.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog1.show(); // Showing Alert Message
    }
}
