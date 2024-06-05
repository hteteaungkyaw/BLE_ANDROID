package it.kyaw.bleapp;

//package com.example.bleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<String> deviceList;

    private Context context;

    public DeviceAdapter( Context context,List<String> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String deviceName = deviceList.get(position);
        holder.deviceName.setText(deviceName);
        holder.menu_btn.setOnClickListener(v -> {
            Log.i("Kyaw","name edit dialog..*********");
            
            showAlerDialgo();
           

        });



    }

    private void showAlerDialgo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.name_edit_box, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.nameText);
        EditText dialogMessage = dialogView.findViewById(R.id.nameChange);
        Button buttonOk = dialogView.findViewById(R.id.btn_Ok);
        Button buttonCancel = dialogView.findViewById(R.id.btn_Cancel);

        dialogTitle.setText("Name Edit...");
        dialogMessage.setText("Details about device: " + deviceList);

        AlertDialog alertDialog = builder.create();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Log.i("Kyaw","OK Button..*********");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
                Log.i("Kyaw","Cancel Button..*********");
            }
        });

        alertDialog.show();


//        builder.setTitle("Device Name Change");
//        builder.setMessage("Are you sure device name change??");
//
//        builder.setPositiveButton("OK", (dialog, which) -> {
//            Toast.makeText(context, "OK clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Device Information")
//                .setMessage("Details about the device: " )
//                .setPositiveButton("OK", null)
//                .setNegativeButton("Cancel", null);
//        builder.show();
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        ImageView menu_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.devicename);
            menu_btn = itemView.findViewById(R.id.menu_btn);


        }
    }
}
