package com.example.civiladvocacyapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder>{
    private static final String TAG = "OfficialAdapter";
    private final List<Official> officialList;
    private final MainActivity mainAct;
    private long start;


    public OfficialAdapter(List<Official> officialList, MainActivity mAct) {
        this.officialList = officialList;
        mainAct = mAct;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW OfficialViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_row, parent, false);
        itemView.setOnClickListener(mainAct);
        //Long click is NOT needed
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Official " + position);
        Official official = officialList.get(position);

        String party = official.getParty();
        String name = official.getName();
        holder.name.setText(name + " (" + party + ")");
        holder.title.setText(official.getGovernmentTitle());
        //holder.picture.;
        if (official.getPhotoLink() != null){
            Glide.with(holder.picture.getContext())
                    .load(official.getPhotoLink())
                    .into(holder.picture);
        }
        else{ //glide error
            Glide.with(holder.picture.getContext())
                    .load(official.getPhotoLink())
                    .error(R.drawable.missing)
                    .into(holder.picture);
        }
        //if picture is just white then it is bad gateway 404 error

    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }

}
