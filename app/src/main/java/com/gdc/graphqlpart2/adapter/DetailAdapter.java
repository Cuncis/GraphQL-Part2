package com.gdc.graphqlpart2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdc.graphql.FeedDetailQuery;
import com.gdc.graphqlpart2.R;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

    private Context context;
    private List<FeedDetailQuery.Episode> episodes = new ArrayList<>();

    public DetailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
        holder.tvNumber.setText(String.valueOf(position+1));
        holder.tvNameEpisode.setText(episodes.get(position).name());
        holder.tvEpisode.setText(episodes.get(position).episode());
        holder.tvAirDate.setText(episodes.get(position).air_date());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void setEpisode(List<FeedDetailQuery.Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    class DetailHolder extends RecyclerView.ViewHolder {

        private TextView tvNumber, tvNameEpisode, tvEpisode, tvAirDate;

        public DetailHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvEpisode = itemView.findViewById(R.id.tv_episode);
            tvNameEpisode = itemView.findViewById(R.id.tv_name);
            tvAirDate = itemView.findViewById(R.id.tv_airDate);
        }
    }

}
