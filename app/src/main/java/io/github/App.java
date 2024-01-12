package io.github;

import android.app.Application;
import android.graphics.Color;
import android.graphics.PorterDuff;

import java.util.function.Consumer;

import io.github.xxmd.ComplexRecyclerView;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initComplexRecyclerView();
    }

    private void initComplexRecyclerView() {
        ComplexRecyclerView.globalInit = binding -> {
            binding.viewLoading.progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            binding.viewLoading.tvLoading.setText("玩命加载中...");

            binding.viewEmpty.ivEmpty.setImageResource(R.drawable.box_empty);
            binding.viewEmpty.tvEmpty.setText("列表空空如也");
            binding.viewEmpty.tvEmpty.setTextColor(Color.RED);

            binding.viewError.ivError.setImageResource(R.drawable.ic_error);
            binding.viewError.btnRetry.setText("立即重试");
            binding.viewError.tvError.setText("出现了未知的错误，请重试");

        };
    }
}
