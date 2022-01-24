package com.example.groupattendancerollcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventTypeListViewAdapter extends RecyclerView.Adapter<EventTypeListViewAdapter.ViewHolder> {

    Context context;
    List<EventTypeListViewModel> viewList;

    public EventTypeListViewAdapter(Context context, List<EventTypeListViewModel> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public EventTypeListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_type, parent, false);
        return new EventTypeListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeListViewAdapter.ViewHolder holder, int position) {
        if (viewList != null && viewList.size() > 0) {
            EventTypeListViewModel viewModel = viewList.get(position);
            holder.tvName.setText(viewModel.getName());
            holder.tvActive.setText(viewModel.getActive());
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvActive;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvActive = itemView.findViewById(R.id.tvActive);

        }
    }

}
