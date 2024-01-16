package com.example.bloodprojectapplication.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.model.Notification;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Viewholder> {
    Context context;
    List<Notification> notifications;

    public HistoryAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public HistoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_donation_item, parent, false);
        return new HistoryAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.Viewholder holder, int position) {

        Notification notification = notifications.get(position);
        String message;
        if (!notification.getRecipient().equals("empty")) {
            message = "In your last blood donation, you contacted to  " + notification.getRecipient()
                    + ".";
        } else {
            message = "You want to donate blood.";
        }


        holder.message.setText(message);
        holder.timeStamp.setText(notification.getTimeStamp());
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView timeStamp;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notification_message);
            timeStamp = itemView.findViewById(R.id.notification_timestamp);
        }
    }
}
