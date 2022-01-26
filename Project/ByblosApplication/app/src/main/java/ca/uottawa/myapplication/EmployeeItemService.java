package ca.uottawa.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeItemService extends AppCompatActivity {
    private Activity context;
    ArrayList<Account> currentAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_item_service);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.item_account, null, true);
//
//        TextView username = (TextView) listViewItem.findViewById(R.id.username);
//
//        Account account = currentAccounts.get(position);
//        username.setText(account.getUsername());
//
//        return listViewItem;
//    }
}