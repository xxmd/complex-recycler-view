package io.github.xxmd;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public interface ComplexRecyclerViewListener<T> {
    void initRecyclerView(RecyclerView recyclerView);
    void loadDataAsync(Consumer<List<T>> onSuccess);
    void renderData(List<T> itemList);
}
