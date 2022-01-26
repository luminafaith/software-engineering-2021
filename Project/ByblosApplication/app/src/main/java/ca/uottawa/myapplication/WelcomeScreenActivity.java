package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeScreenActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        String role = extras.getString("role");

        TextView nameFirst = findViewById(R.id.nameFirst);
        nameFirst.setText(username);

        TextView roleName = findViewById(R.id.roleName);
        roleName.setText("You are logged in as: " + role + " account");

        Button debug = (Button) findViewById(R.id.debugButtonCustomer);

        debug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerServiceListActivity.class);
                intent.putExtra("branch", "branchchurchill");
                intent.putExtra("customer", username);
                startActivity(intent);
            }
        });
    }
}