package com.superapp.fragment.checklist.category;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.superapp.ApplicationContext;
import com.superapp.R;
import com.superapp.beans.BeanSelectCategory;

import java.util.ArrayList;

public class AdapterSelectCategory extends BaseAdapter implements Filterable {

    private ArrayList<BeanSelectCategory> selectCategories;
    private Context context;
    private ArrayList<BeanSelectCategory> mOriginalValues;

    public AdapterSelectCategory(Context context, ArrayList<BeanSelectCategory> selectCategories) {
        this.context = context;
        this.selectCategories = selectCategories;
        this.mOriginalValues = selectCategories;
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
            convertView = Inflater.inflate(R.layout.popupwindow_selectcategoryitem, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BeanSelectCategory category = selectCategories.get(position);
        holder.tv_categoryTitle.setText(category.getTitle());
        convertView.setTag(holder);
        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                selectCategories = (ArrayList<BeanSelectCategory>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<BeanSelectCategory> FilteredArrList = new ArrayList<BeanSelectCategory>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<BeanSelectCategory>(selectCategories); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new BeanSelectCategory(mOriginalValues.get(i).getTitle()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
