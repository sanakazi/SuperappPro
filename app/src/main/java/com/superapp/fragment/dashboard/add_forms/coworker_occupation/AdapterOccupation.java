package com.superapp.fragment.dashboard.add_forms.coworker_occupation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;

import java.util.ArrayList;

/**
 * Created by APPNWEB on 01-09-2016.
 */
public class AdapterOccupation extends BaseAdapter {

    ArrayList<BeanOccupation> beanOccupation;
    Context context;

    public AdapterOccupation(Context context, ArrayList<BeanOccupation> beanOccupation) {
        this.context = context;
        this.beanOccupation = beanOccupation;
    }

    private static class ViewHolder {
        TextView tv_coWorkerOccupationListItem;

        public ViewHolder(View view) {
            tv_coWorkerOccupationListItem = (TextView) view.findViewById(R.id.tv_coWorkerOccupationListItem);
        }
    }

    @Override
    public int getCount() {
        return beanOccupation.size();
    }

    @Override
    public Object getItem(int position) {
        if (beanOccupation == null) {
            return 0;
        } else {
            return beanOccupation.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater Inflater = (LayoutInflater) ApplicationContext.getInstance().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = Inflater.inflate(R.layout.popupwindow_occupation_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BeanOccupation occupation = beanOccupation.get(position);
        holder.tv_coWorkerOccupationListItem.setText(occupation.getTitle());
        convertView.setTag(holder);
        return convertView;
    }
}
