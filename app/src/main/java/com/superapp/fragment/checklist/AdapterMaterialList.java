package com.superapp.fragment.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.OnViewClickListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abhijeet-PC on 10-Feb-17.
 */

public class AdapterMaterialList extends BaseAdapter {

    private ArrayList<JSONObject> materialArrayList;
    private Context context;
    ViewHolder holder;
    OnViewClickListener onClickListener;


    public AdapterMaterialList(ArrayList<JSONObject> materialArrayList, Context context, OnViewClickListener onClickListener) {
        this.materialArrayList = materialArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return materialArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.materia_llist_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.tv_materialItem);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.iv_delete.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject materials = materialArrayList.get(position);
        try {
//            String unit = "";
//            if (materials.has("unit")) {
//                unit = " " + materials.getJSONObject("unit").getString("title");
//            }
            String titleStr = materials.getJSONObject("material").getString("title") + " | " +
                    materials.getJSONObject("subMaterial").getString("title") + " | " +
                    materials.getJSONObject("brand").getString("title") + " | " +
                    materials.getJSONObject("color").getString("title") + " | " +
                    materials.getJSONObject("size").getString("title") + " " +
                    materials.getJSONObject("measurement").getString("title") + " | " +
//                    materials.getJSONObject("measurement").getString("title") + " | " +
//                    materials.getJSONObject("size").getString("title")  + " | " +
                    materials.getString("quantity");
            if (materials.has("note")) {
                titleStr = titleStr + " | " + materials.getString("note");
            }
            holder.title.setText(titleStr);
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onClickListener != null) {
                        onClickListener.onViewItemClick(position);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        private TextView title;
        private ImageView iv_delete;
    }
}
