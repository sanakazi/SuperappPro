package com.superapp.activity.supersearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.beans.Region;

import java.util.ArrayList;

/**
 * Created by inte on 9/2/2016.
 */
public class SpinnerListAdapter extends BaseAdapter {

    ArrayList<Region> regions;
    Context context;

    public SpinnerListAdapter(Context context, ArrayList<Region> beanLocationDetails) {
        this.context = context;
        this.regions = beanLocationDetails;
    }

    private static class ViewHolder {
        TextView tv_countryListItem;

        public ViewHolder(View view) {
            tv_countryListItem = (TextView) view.findViewById(R.id.textViewItem);
        }
    }

    @Override
    public int getCount() {
        return regions.size();
    }

    @Override
    public Object getItem(int position) {
        if (regions == null) {
            return 0;
        } else {
            return regions.get(position);
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
            convertView = Inflater.inflate(R.layout.item_string_text, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Region countryList = regions.get(position);
        holder.tv_countryListItem.setText(countryList.getTitle());
        convertView.setTag(holder);
        return convertView;
    }
}
