package io.github.xxmd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import io.github.xxmd.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements ComplexRecyclerViewListener<String> {
    private ActivityMainBinding binding;
    private String[] usernameArr = {};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        binding.complexRecycler.setListener(this);
    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UsernameAdapter usernameAdapter = new UsernameAdapter(usernameArr);
        recyclerView.setAdapter(usernameAdapter);
    }

    @Override
    public void loadDataAsync(Consumer<List<String>> onSuccess) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {

        }, 3000);
    }

    @Override
    public void renderData(List itemList) {

    }
}