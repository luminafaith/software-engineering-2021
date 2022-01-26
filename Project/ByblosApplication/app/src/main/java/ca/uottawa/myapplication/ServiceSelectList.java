package ca.uottawa.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ServiceSelectList extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<String> currentAvailableServices;

    public ServiceSelectList(Activity context, ArrayList<String> accounts) {
        super(context, R.layout.item_service_select, accounts);
        this.context = context;
        this.currentAvailableServices = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item_service_select, null, true);

        TextView serviceName = (TextView) listViewItem.findViewById(R.id.selectServiceName);

        String availableService = currentAvailableServices.get(position);
//        if (account.getRole().equals("Branch")) {
//            BranchAccount
//        }
        serviceName.setText(availableService);

        return listViewItem;
    }
}
