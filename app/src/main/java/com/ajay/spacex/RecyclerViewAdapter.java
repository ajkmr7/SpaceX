package com.ajay.spacex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<JSONObject> crewMembers;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private  Context mContext;

    RecyclerViewAdapter(Context context, ArrayList<JSONObject> jsonObjects) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.crewMembers = jsonObjects;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.crew_member, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String name = null, agency = null, status = null, wikiLink = null, id = null, image= null;
        try {
            name = crewMembers.get(position).getString("name");
            agency = crewMembers.get(position).getString("agency");
            status = crewMembers.get(position).getString("status");
            wikiLink = crewMembers.get(position).getString("wikipedia");
            id = crewMembers.get(position).getString("id");
            image = crewMembers.get(position).getString("image");

            holder.name.setText("Name:\n"+name);
            holder.agency.setText("Agency: "+agency);
            holder.status.setText("Status: "+status);
            holder.wikipediaLink.setText("WIKIPEDIA: "+wikiLink);


            holder.id.setText("id: "+id);
            Glide.with(mContext)
                    .load(image)
                    .error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.cardBackground);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return crewMembers.toArray().length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,agency,status,id,wikipediaLink;
        ImageView cardBackground;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            agency = itemView.findViewById(R.id.agency);
            status = itemView.findViewById(R.id.status);
            id = itemView.findViewById(R.id.id);
            wikipediaLink = itemView.findViewById(R.id.wikipediaLink);
            cardBackground = itemView.findViewById(R.id.imageView);

            wikipediaLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaLink.getText().toString().substring(11)));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browserIntent);
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return null;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}