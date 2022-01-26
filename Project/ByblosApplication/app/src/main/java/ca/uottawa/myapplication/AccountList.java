package ca.uottawa.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountList extends ArrayAdapter<Account> {
    private Activity context;
    ArrayList<Account> currentAccounts;

    public AccountList(Activity context, ArrayList<Account> accounts) {
        super(context, R.layout.item_account, accounts);
        this.context = context;
        this.currentAccounts = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item_account, null, true);

        TextView username = (TextView) listViewItem.findViewById(R.id.username);

        Account account = currentAccounts.get(position);
        username.setText(account.getUsername());

        return listViewItem;
    }
}