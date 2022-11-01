package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView name;
    ImageView picture;

    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.position_tv);
        name = itemView.findViewById(R.id.personName_tv);
        picture = itemView.findViewById(R.id.personPic_IV);
    }
}
