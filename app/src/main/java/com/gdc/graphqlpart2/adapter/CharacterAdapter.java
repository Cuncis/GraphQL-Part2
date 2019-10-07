package com.gdc.graphqlpart2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdc.graphql.FeedResultQuery;
import com.gdc.graphqlpart2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterHolder> {


    private Context context;
    private List<FeedResultQuery.Result> resultList = new ArrayList<>();
    private ClickListener clickListener;

    public CharacterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false);
        return new CharacterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        FeedResultQuery.Result result = resultList.get(position);
        holder.tvName.setText(result.name());
        holder.tvSpecies.setText(result.species());
        holder.tvGender.setText(result.gender());
        holder.tvType.setText(result.type());
        Picasso.get().load(result.image()).into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void setResult(List<FeedResultQuery.Result> results) {
        this.resultList = results;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class CharacterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvSpecies, tvGender, tvType;
        private ImageView imgPoster;

        public CharacterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSpecies = itemView.findViewById(R.id.tv_species);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvType = itemView.findViewById(R.id.tv_type);
            imgPoster = itemView.findViewById(R.id.img_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }

}
