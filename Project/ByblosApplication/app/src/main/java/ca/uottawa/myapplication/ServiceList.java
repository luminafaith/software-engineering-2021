package ca.uottawa.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ServiceList extends ArrayAdapter<Service> {
    private Activity context;
    ArrayList<Service> currentServices;

    public ServiceList(Activity context, ArrayList<Service> services) {
        super(context, R.layout.service_list_item, services);
        this.context = context;
        this.currentServices = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.service_list_item, null, true);

        TextView service_name = (TextView) listViewItem.findViewById(R.id.service_name);

        Service service = currentServices.get(position);
        service_name.setText(service.getServiceName());

        return listViewItem;
    }
}