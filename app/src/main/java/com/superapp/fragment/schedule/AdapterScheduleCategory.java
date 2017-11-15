package com.superapp.fragment.schedule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.beans.BeanScheduleCategory;

import java.util.ArrayList;

/**
 * Created by inte on 9/9/2016.
 */
public class AdapterScheduleCategory extends BaseAdapter {

    ArrayList<BeanScheduleCategory> selectCategories;
    Context context;

    public AdapterScheduleCategory(Context context, ArrayList<BeanScheduleCategory> selectCategories) {
        this.context = context;
        this.selectCategories = selectCategories;
    }

    private static class ViewHolder {
        TextView tv_categoryTitle;

        public ViewHolder(View view) {
            tv_categoryTitle = (TextView) view.findViewById(R.id.tv_categoryTitle);
        }
    }

    @Override
    public int getCount() {
        return selectCategories.size();
    }

    @Override
    public Object getItem(int position) {
        if (selectCategories == null) {
            return 0;
        } else {
            return selectCategories.get(position);
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
            convertView = Inflater.inflate(R.layout.spinner_schedulecategory_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BeanScheduleCategory category = selectCategories.get(position);
        holder.tv_categoryTitle.setText(category.getItem());
        convertView.setTag(holder);
        return convertView;
    }
}

