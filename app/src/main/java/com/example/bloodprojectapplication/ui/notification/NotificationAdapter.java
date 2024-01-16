package com.example.bloodprojectapplication.ui.notification;

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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.domain.SharePreference;
import com.example.bloodprojectapplication.model.Notification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder> {
    private static final ArrayList<CardView> cardViewArrayList = new ArrayList<>();
    Context context;
    List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Notification notification = notifications.get(position);
        String message = "Hello, I am " + notification.getUserName() + " and I would like to donate blood.\nMy blood group is "
                + notification.getBloodGroup() + "\nYou can also connect with me through email " + notification.getUserEmail();
        holder.message.setText(message);
        holder.timeStamp.setText(notification.getTimeStamp());
        holder.connect.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new
                        String[]{android.Manifest.permission.CALL_PHONE}, 0);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + notification.getPhoneNumber()));
                context.startActivity(intent);
            }
            updateNotification(notification);
        });

    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView timeStamp;
        private final Button connect;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.notification_message);
            timeStamp = itemView.findViewById(R.id.notication_timestamp);
            connect = itemView.findViewById(R.id.connect);
        }
    }

    private void updateNotification(Notification notification) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifications")
                .child(SharePreference.getINSTANCE(context).getBLOODGROUP());
        reference.child(notification.getUserUID()).child("recipient").setValue(SharePreference.getINSTANCE(context).getName());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(notification.getUserUID()).child("history").child(notification.getTimeStamp()).child("recipient").setValue(SharePreference.getINSTANCE(context).getName());
    }
}