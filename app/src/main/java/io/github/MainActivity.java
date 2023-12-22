package io.github;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import io.github.xxmd.ComplexRecyclerViewListener;
import io.github.R;
import io.github.UsernameAdapter;
import io.github.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements ComplexRecyclerViewListener<String> {
    private ActivityMainBinding binding;
    private String[] usernameArr = {};
    private UsernameAdapter usernameAdapter;
    int retryTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        bindEvent();
    }

    private void bindEvent() {
        binding.btnRender.setOnClickListener(v -> binding.complexRecycler.reRender());
    }

    private void initView() {
        binding.complexRecycler.setListener(this);
        binding.complexRecycler.getBinding().viewEmpty.tvEmpty.setText("四大皆空");
    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usernameAdapter = new UsernameAdapter(Arrays.asList(usernameArr));
        recyclerView.setAdapter(usernameAdapter);
    }

    @Override
    public void loadDataAsync(Consumer<List<String>> onSuccess) {
        Handler handler = new Handler();
        if (retryTimes % 3 == 0) {
            retryTimes = (retryTimes + 1) % 3;
            throw new RuntimeException();
        }
        handler.postDelayed(() -> {
            usernameArr = new String[]{"James", "John", "Hans", "Tom", "Tony"};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                switch (retryTimes) {
                    case 1:
                        onSuccess.accept(new ArrayList<>());
                        break;
                    case 2:
                        onSuccess.accept(Arrays.asList(usernameArr));
                        break;
                }
                retryTimes = (retryTimes + 1) % 3;
            }
        }, 1000 * 3);
    }

    @Override
    public void renderData(List itemList) {
        usernameAdapter.setLocalDataSet(itemList);
        usernameAdapter.notifyDataSetChanged();
    }
}