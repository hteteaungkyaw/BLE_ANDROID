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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    //    private List<Device> deviceList;
//
    private Context context;
//
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onItemClick(Device device);
//    }
//
//    public DeviceAdapter(List<Device>deviceList,OnItemClickListener listener ) {
////        this.context = context;
//        this.deviceList = deviceList;
//        this.listener = listener;
//
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices, parent, false);
//        return new ViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////        String deviceName = deviceList.get(position);
//        Device device = deviceList.get(position);
//        holder.deviceName.setText(device.getName() != null ? device.getName() : "Unknown Device");
//        holder.deviceAddress.setText(device.getAddress());
//        holder.itemView.setOnClickListener(v -> listener.onItemClick(device));
////        holder.deviceName.setText(device.getName());
//        holder.menu_btn.setOnClickListener(v -> {
//            Log.i("Kyaw","name edit dialog..*********");
//
//            showAlerDialgo();
//
//
//        });
//
//
//
//    }
//
//    private void showAlerDialgo() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View dialogView = inflater.inflate(R.layout.name_edit_box, null);
//        builder.setView(dialogView);
//
//        TextView dialogTitle = dialogView.findViewById(R.id.nameText);
//        EditText dialogMessage = dialogView.findViewById(R.id.nameChange);
//        Button buttonOk = dialogView.findViewById(R.id.btn_Ok);
//        Button buttonCancel = dialogView.findViewById(R.id.btn_Cancel);
//
//        dialogTitle.setText("Name Edit...");
//        dialogMessage.setText("Details about device: " + deviceList);
//
//        AlertDialog alertDialog = builder.create();
//
//        buttonOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                alertDialog.dismiss();
//                Log.i("Kyaw","OK Button..*********");
//            }
//        });
//
//        buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                alertDialog.dismiss();
//                Log.i("Kyaw","Cancel Button..*********");
//            }
//        });
//
//        alertDialog.show();
//    }

//    @Override
//    public int getItemCount() {
//        return deviceList.size();
//    }
//
//    public void addDevice(Device device) {
//        if (!deviceList.contains(device)) {
//            deviceList.add(device);
//            notifyDataSetChanged();
//        }
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView deviceName,deviceAddress;
//        ImageView menu_btn;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            deviceName = itemView.findViewById(R.id.devicename);
//            deviceAddress = itemView.findViewById(R.id.device_address);
//            menu_btn = itemView.findViewById(R.id.menu_btn);
//
//
//        }
//    }
//}

    private List<Device> deviceList;
    private OnItemClickListener listener;


    public DeviceAdapter(List<Device> deviceList, OnItemClickListener listener) {
        this.deviceList = deviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_devices, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.deviceName.setText(device.getName() != null ? device.getName() : "Unknown Device");
        holder.deviceAddress.setText(device.getAddress());
        holder.menu_btn.setOnClickListener(v -> {
            Log.i("Kyaw", "name edit dialog..*********");

            showAlerDialgo();


        });
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(device);

            Log.i("KYAW", " Connected device " + device);
        });

//
//
    }

    private void showAlerDialgo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
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
                Log.i("Kyaw", "OK Button..*********");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Log.i("Kyaw", "Cancel Button..*********");
            }
        });

        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void addDevice(Device device) {
        if (!deviceList.contains(device)) {
            deviceList.add(device);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Device device);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        TextView deviceAddress;

        ImageView menu_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.devicename);
            deviceAddress = itemView.findViewById(R.id.device_address);
            menu_btn = itemView.findViewById(R.id.menu_btn);
        }
    }


}
