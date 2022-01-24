package com.example.groupattendancerollcall;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    Context context;
    List<ViewModel> viewList;

    private FirebaseDatabase database;
    private DatabaseReference attendeeRef;



    public ViewAdapter(Context context, List<ViewModel> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_attendee_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        attendeeRef = database.getReference().child("AttendeeData");

        if (viewList != null && viewList.size() > 0) {
            ViewModel viewModel = viewList.get(position);
            holder.tvId.setText(viewModel.getId());
            holder.tvFirstName.setText(viewModel.getFirstName());
            holder.tvLastName.setText(viewModel.getLastName());

            if(viewModel.getActive().equals("No") && !holder.checkBoxActive.isChecked()){
                holder.checkBoxActive.setChecked(false);
            }
            else if (viewModel.getActive().equals("Yes")){
                holder.checkBoxActive.setChecked(true);
            }

            holder.checkBoxActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {

                    if ( isChecked )
                    {
                        attendeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if ((viewModel.getId().equals(ds.child("AttendeeId").getValue().toString())) && (viewModel.getContactPersonName().equals(ds.child("AttendeeContactPersonName").getValue().toString()))) {
                                        DatabaseReference activeReference = ds.getRef().child("Active");
                                        activeReference.setValue("Yes");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(context, "Attendance Marked as Present", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        attendeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if ((viewModel.getId().equals(ds.child("AttendeeId").getValue().toString())) && (viewModel.getContactPersonName().equals(ds.child("AttendeeContactPersonName").getValue().toString()))) {
                                        DatabaseReference activeReference = ds.getRef().child("Active");
                                        activeReference.setValue("No");

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(context, "Attendance Marked as not present", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater layoutinflater = LayoutInflater.from(context);
                    // below line is for inflating a custom pop up layout.
                    View dialoglayout = layoutinflater.inflate(R.layout.cutomized_dialogue, null);

                    ImageView tvImage = dialoglayout.findViewById(R.id.tvImage);

                    Glide.with(context)
                            .load(viewModel.getImageUrl())
                            .into(tvImage);

                    new AlertDialog.Builder(v.getContext()) // problem over here
                            .setTitle("Attendee Detail")
                            .setView(dialoglayout)
                            .setMessage("id : " + viewModel.getId() + "\n" +
                                    "First Name: " + viewModel.getFirstName() + "\n" +
                                    "Last Name : " + viewModel.getLastName() + "\n" +
                                    "Contact Person Name : " + viewModel.getContactPersonName() + "\n" +
                                    "Contact Person Email : " + viewModel.getContactPersonEmail() + "\n" +
                                    "Contact Person Mobile : " + viewModel.getContactPersonMobile() + "\n" +
                                    "Note: " + viewModel.getNote()
                            )
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
        TextView tvId, tvFirstName, tvLastName;
        CheckBox checkBoxActive;

        public ViewHolder(View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            checkBoxActive = itemView.findViewById(R.id.checkBoxActive);

        }
    }
}
