package ca.uottawa.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AvailableServicesList extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<String> currentAvailableServices;

    public AvailableServicesList(Activity context, ArrayList<String> availableServices) {
        super(context, R.layout.employee_item_service, availableServices);
        this.context = context;
        this.currentAvailableServices = availableServices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.employee_item_service, null, true);

        TextView serviceName = (TextView) listViewItem.findViewById(R.id.serviceName);

        String availableService = currentAvailableServices.get(position);
        serviceName.setText(availableService);

        return listViewItem;
    }
}