package com.gdc.graphqlpart2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.gdc.graphql.FeedDetailQuery;
import com.gdc.graphqlpart2.adapter.DetailAdapter;
import com.gdc.graphqlpart2.data.ApiClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "_DetailActivity";

    private TextView tvName, tvStatus, tvSpecies, tvType, tvGender;
    private ImageView imgPoster;
    private RecyclerView rvEpisodes;

    private DetailAdapter adapter;
    private FeedDetailQuery.Character detail = null;
    private String charId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getIntent().getStringExtra("NAME"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        if (getIntent().hasExtra("CHAR_ID")) {
            charId = getIntent().getStringExtra("CHAR_ID");
        }

        initRecyclerView();
        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initRecyclerView() {
        adapter = new DetailAdapter(this);
        rvEpisodes.setLayoutManager(new LinearLayoutManager(this));
        rvEpisodes.setHasFixedSize(true);
        rvEpisodes.setAdapter(adapter);
    }

    private void getData() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        ApiClient.getClient().query(FeedDetailQuery.builder()
                .id(charId)
                .build())
                .enqueue(new ApolloCall.Callback<FeedDetailQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<FeedDetailQuery.Data> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                detail = response.data().character();
                                adapter.setEpisode(detail.episode());
                                getDetail();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getDetail() {
        tvName.setText("Name: \t\t" + detail.name());
        tvStatus.setText("Status: \t\t" + detail.status());
        if (detail.species().equals("")) {
            tvSpecies.setText("Species: \t\t - ");
        } else {
            tvSpecies.setText("Species: \t\t" + detail.species());
        }

        if (detail.type().equals("")) {
            tvType.setText("Type: \t\t - ");
        } else {
            tvType.setText("Type: \t\t" + detail.type());
        }

        if (detail.gender().equals("")) {
            tvGender.setText("Gender: \t\t - ");
        } else {
            tvGender.setText("Gender: \t\t" + detail.gender());
        }
        Picasso.get().load(detail.image()).placeholder(R.drawable.ic_launcher_background).into(imgPoster);
    }

    private void initViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        tvName = findViewById(R.id.tv_name);
        tvStatus = findViewById(R.id.tv_status);
        tvSpecies = findViewById(R.id.tv_species);
        tvType = findViewById(R.id.tv_type);
        tvGender = findViewById(R.id.tv_gender);
        imgPoster = findViewById(R.id.img_poster);
        rvEpisodes = findViewById(R.id.rv_episodes);
    }
}
