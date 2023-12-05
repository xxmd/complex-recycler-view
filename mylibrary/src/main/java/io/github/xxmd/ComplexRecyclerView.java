package io.github.xxmd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.github.xxmd.databinding.ComplexRecyclerViewBinding;

public class ComplexRecyclerView<T> extends ConstraintLayout {
    private ComplexRecyclerViewBinding binding;
    private RecyclerViewState state;
    private ComplexRecyclerViewListener<T> listener;

    public ComplexRecyclerViewListener getListener() {
        return listener;
    }

    public void setListener(ComplexRecyclerViewListener listener) {
        this.listener = listener;
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
    }

    public ComplexRecyclerView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        binding = ComplexRecyclerViewBinding.inflate(LayoutInflater.from(getContext()), this);
        initView();
    }

    private void initView() {
        if (listener != null) {
            listener.initRecyclerView(binding.recyclerView);
            setState(RecyclerViewState.LOADING);
            listener.loadDataAsync(data -> {
                setState(RecyclerViewState.IDLE);
                listener.renderData(data);
            });
        }
    }
}
