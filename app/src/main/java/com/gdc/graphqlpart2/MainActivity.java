package com.gdc.graphqlpart2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.gdc.graphql.FeedResultQuery;
import com.gdc.graphqlpart2.adapter.CharacterAdapter;
import com.gdc.graphqlpart2.data.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements CharacterAdapter.ClickListener {
    private static final String TAG = "_MainActivity";

    private FloatingActionButton fabNext;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private CharacterAdapter adapter;
    private FeedResultQuery.Characters data = null;

    private int number = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        number++;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        fabNext = findViewById(R.id.fab_next);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new CharacterAdapter(this);


        initRecyclerview();
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                getData(number);
                setTitle("Page " + (number-1));
            }
        });

        getData(number);

    }

    private void initRecyclerview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void getData(int numberOfPage) {
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        ApiClient.getClient().query(FeedResultQuery.builder()
                .page(numberOfPage)
                .build())
                .enqueue(new ApolloCall.Callback<FeedResultQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<FeedResultQuery.Data> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                data = response.data().characters();
                                adapter.setResult(data.results());
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

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "Click " + data.results().get(position).id(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("CHAR_ID", data.results().get(position).id());
        i.putExtra("NAME", data.results().get(position).name());
        startActivity(i);
    }
}
