package com.example.groupattendancerollcall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventMainViewAdapter extends RecyclerView.Adapter<EventMainViewAdapter.ViewHolder> {
    Context context;
    List<EventMainViewModel> viewList;

    public EventMainViewAdapter(Context context, List<EventMainViewModel> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventMainViewAdapter.ViewHolder holder, int position) {
        if (viewList != null && viewList.size() > 0) {
            EventMainViewModel viewModel = viewList.get(position);
            holder.tvDateTime.setText(viewModel.getEventFromDate());
            holder.tvEventType.setText(viewModel.getEventTypeName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(v.getContext()) // problem over here
                            .setTitle("Event Detail")
                            .setMessage("Event Type : " + viewModel.getEventTypeName() + "\n" +
                                    "From Date : " + viewModel.getEventFromDate() + "\n" +
                                    "From Time : " + viewModel.getEventFromTime() + "\n" +
                                    "To Date : " + viewModel.eventToDate + "\n" +
                                    "To Time : " + viewModel.getEventToTime() + "\n" +
                                    "Event Description : " + viewModel.getEventDescription())
                            .show();
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvEventType;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvEventType = itemView.findViewById(R.id.tvEventType);

        }
    }
}
