package io.github.xxmd;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.function.Consumer;

import io.github.xxmd.databinding.ComplexRecyclerViewBinding;

public class ComplexRecyclerView<T> extends ConstraintLayout {
    private ComplexRecyclerViewBinding binding;
    private RecyclerViewState state;
    private ComplexRecyclerViewListener<T> listener;
    public static Consumer<ComplexRecyclerViewBinding> globalInit;

    public ComplexRecyclerViewListener getListener() {
        return listener;
    }

    public void setListener(ComplexRecyclerViewListener listener) {
        this.listener = listener;
        loadData();
    }

    public RecyclerViewState getState() {
        return state;
    }

    public void setState(RecyclerViewState state) {
        binding.viewLoading.getRoot().setVisibility(GONE);
        binding.viewEmpty.getRoot().setVisibility(GONE);
        binding.viewError.getRoot().setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);

        switch (state) {
            case IDLE:
                binding.recyclerView.setVisibility(VISIBLE);
                break;
            case LOADING:
                binding.viewLoading.getRoot().setVisibility(VISIBLE);
                break;
            case EMPTY:
                binding.viewEmpty.getRoot().setVisibility(VISIBLE);
                break;
            case ERROR:
                binding.viewError.getRoot().setVisibility(VISIBLE);
                break;
        }
        this.state = state;
    }

    public ComplexRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ComplexRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        binding = ComplexRecyclerViewBinding.inflate(LayoutInflater.from(getContext()), this, true);
        if (globalInit != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                globalInit.accept(binding);
            }
        }
        bindEvent();
    }

    private void bindEvent() {
        binding.viewError.btnRetry.setOnClickListener(v -> loadData());
    }


    private void loadData() {
        if (listener != null) {
            listener.initRecyclerView(binding.recyclerView);
            setState(RecyclerViewState.LOADING);
            try {
                listener.loadDataAsync(data -> {
                    if (data == null || data.size() == 0) {
                        setState(RecyclerViewState.EMPTY);
                    } else {
                        setState(RecyclerViewState.IDLE);
                    }
                    listener.renderData(data);
                });
            } catch (Exception e) {
                setState(RecyclerViewState.ERROR);
            }
        }
    }
}
