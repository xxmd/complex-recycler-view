package io.github.xxmd;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.xxmd.databinding.CrvComplexRecyclerViewBinding;


public class ComplexRecyclerView<T> extends ConstraintLayout {
    private CrvComplexRecyclerViewBinding binding;
    private TypedArray typedArray;

    public CrvComplexRecyclerViewBinding getBinding() {
        return binding;
    }

    private RecyclerViewState state;
    private ComplexRecyclerViewListener<T> listener;
    public static Consumer<CrvComplexRecyclerViewBinding> globalInit;

    public ComplexRecyclerViewListener getListener() {
        return listener;
    }

    public void setListener(ComplexRecyclerViewListener listener) {
        this.listener = listener;

        if (listener != null) {
            listener.initRecyclerView(binding.recyclerView);
            loadData();
        }
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

    public void reRender() {
        if (listener != null) {
            loadData();
        }
    }

    public ComplexRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ComplexRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ComplexRecyclerView);
        init();
    }

    private void init() {
        binding = CrvComplexRecyclerViewBinding.inflate(LayoutInflater.from(getContext()), this, true);
        if (globalInit != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                globalInit.accept(binding);
            }
        }
        if (typedArray != null) {
            initByTypeArray();
        }
        bindEvent();
    }

    private void initByTypeArray() {
        boolean loadingIconVisible = typedArray.getBoolean(R.styleable.ComplexRecyclerView_loadingIconVisible, true);
        binding.viewLoading.progressBar.setVisibility(loadingIconVisible ? VISIBLE : GONE);

        Drawable drawable = typedArray.getDrawable(R.styleable.ComplexRecyclerView_loadingIcon);
        if (drawable != null) {
            binding.viewLoading.progressBar.setIndeterminateDrawable(drawable);
        }

        int loadingIconTint = typedArray.getColor(R.styleable.ComplexRecyclerView_loadingIconTint, Integer.MIN_VALUE);
        if (loadingIconTint != Integer.MIN_VALUE) {
            binding.viewLoading.progressBar.getIndeterminateDrawable().setColorFilter(loadingIconTint, PorterDuff.Mode.SRC_IN);
        }

        setText(binding.viewLoading.tvLoading, R.styleable.ComplexRecyclerView_loadingTextVisible, R.styleable.ComplexRecyclerView_loadingText,  R.styleable.ComplexRecyclerView_loadingTextColor);

        setIcon(binding.viewEmpty.ivEmpty, R.styleable.ComplexRecyclerView_emptyIconVisible, R.styleable.ComplexRecyclerView_emptyIcon, R.styleable.ComplexRecyclerView_emptyIconTint);
        setText(binding.viewEmpty.tvEmpty, R.styleable.ComplexRecyclerView_emptyTextVisible, R.styleable.ComplexRecyclerView_emptyText,  R.styleable.ComplexRecyclerView_emptyTextColor);

        setIcon(binding.viewError.ivError, R.styleable.ComplexRecyclerView_errorIconVisible, R.styleable.ComplexRecyclerView_errorIcon, R.styleable.ComplexRecyclerView_errorIconTintColor);
        setText(binding.viewError.tvError, R.styleable.ComplexRecyclerView_errorTextVisible, R.styleable.ComplexRecyclerView_errorText,  R.styleable.ComplexRecyclerView_errorTextColor);

        boolean retryBtnVisible = typedArray.getBoolean(R.styleable.ComplexRecyclerView_retryBtnVisible, true);
        binding.viewEmpty.btnRetry.setVisibility(retryBtnVisible ? VISIBLE : GONE);
        binding.viewError.btnRetry.setVisibility(retryBtnVisible ? VISIBLE : GONE);

        float topMargin = typedArray.getDimension(R.styleable.ComplexRecyclerView_emptyTextMarginTop, 5);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.viewEmpty.tvEmpty.getLayoutParams();
        layoutParams.topMargin = (int) topMargin;
        binding.viewEmpty.tvEmpty.setLayoutParams(layoutParams);
    }

    private void setIcon(ImageView imageView, @StyleableRes int visibleIndex, @StyleableRes int iconIndex, @StyleableRes int iconTintIndex) {
        boolean visible = typedArray.getBoolean(visibleIndex, true);
        imageView.setVisibility(visible ? VISIBLE : GONE);

        Drawable drawable = typedArray.getDrawable(iconIndex);
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }

        int tintColor = typedArray.getColor(iconTintIndex, Integer.MIN_VALUE);
        if (tintColor != Integer.MIN_VALUE) {
            imageView.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setText(TextView textView, @StyleableRes int visibleIndex, @StyleableRes int textIndex, @StyleableRes int colorIndex) {
        boolean visible = typedArray.getBoolean(visibleIndex, true);
        textView.setVisibility(visible ? VISIBLE : GONE);

        String text = typedArray.getString(textIndex);
        if (text != null) {
            textView.setText(text);
        }

        int color = typedArray.getColor(colorIndex, Integer.MIN_VALUE);
        if (color != Integer.MIN_VALUE) {
            textView.setTextColor(color);
        }
    }

    private void bindEvent() {
        binding.viewError.btnRetry.setOnClickListener(v -> loadData());
        binding.viewEmpty.btnRetry.setOnClickListener(v -> loadData());
    }


    private void loadData() {
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
