package com.superapp.fragment.survey;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superapp.R;
import com.superapp.activity.base.BaseAppCompatActivity;
import com.superapp.activity.base.OnItemClickListener;
import com.superapp.beans.Survey;

import java.util.ArrayList;

public class AdapterSurvey extends RecyclerView.Adapter<AdapterSurvey.ViewHolder> {

    BaseAppCompatActivity activity;
    ArrayList<Survey> surveyList;
    OnItemClickListener onItemClickListener;

    public AdapterSurvey(BaseAppCompatActivity activity, ArrayList<Survey> surveyList, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.surveyList = surveyList;
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_surveyName;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_surveyName = (TextView) itemView.findViewById(R.id.tv_surveyName);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // TODO user holder directly
        final Survey surveyList1 = surveyList.get(position);
        holder.tv_surveyName.setText(surveyList1.getName());

        holder.tv_surveyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(surveyList1, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

}
