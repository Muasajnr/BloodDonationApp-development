package com.example.bloodprojectapplication.ui.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.model.Notification;

import java.util.List;

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.Viewholder> {

    Context context;
    List<Notification> notifications;

    public DonationsAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public DonationsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_donation_item, parent, false);
        return new DonationsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationsAdapter.Viewholder holder, int position) {

        Notification notification = notifications.get(position);
        String message;
        if (!notification.getRecipient().equals("empty")) {
            message = notification.getUserName() + ", of blood group "
                    + notification.getBloodGroup() + " donated to " + notification.getRecipient()
                    + ".";
        } else {
            message = notification.getUserName() + ", of blood group "
                    + notification.getBloodGroup() + " wants to donate blood.";
        }


        holder.message.setText(message);
        holder.timeStamp.setText(notification.getTimeStamp());
        holder.contact.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new
                        String[]{android.Manifest.permission.CALL_PHONE}, 0);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + notification.getPhoneNumber()));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView timeStamp;
        private final Button contact;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notification_message);
            timeStamp = itemView.findViewById(R.id.notification_timestamp);
            contact = itemView.findViewById(R.id.contact);
        }
    }
}