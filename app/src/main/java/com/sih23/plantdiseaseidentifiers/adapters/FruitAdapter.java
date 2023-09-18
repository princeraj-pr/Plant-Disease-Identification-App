package com.sih23.plantdiseaseidentifiers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sih23.plantdiseaseidentifiers.FruitEntry;
import com.sih23.plantdiseaseidentifiers.R;

public class FruitAdapter extends ListAdapter<FruitEntry, FruitAdapter.FruitViewHolder> {

    /*
     * DiffUtil is a utility class that helps optimize updates to a RecyclerView by efficiently
     * determining which items have changed, been added, or been removed in a list.
     */
    private static final DiffUtil.ItemCallback<FruitEntry> DIFF_CALLBACK = new DiffUtil
            .ItemCallback<FruitEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull FruitEntry oldItem, @NonNull FruitEntry newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FruitEntry oldItem, @NonNull FruitEntry newItem) {
            return oldItem.getIconId() == newItem.getIconId();
        }
    };

    public FruitAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fruit, parent, false);
        return new FruitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        FruitEntry currentFruit = getItem(position);
        holder.bind(currentFruit.getIconId());
    }

    public static class FruitViewHolder extends RecyclerView.ViewHolder {

        private final ImageView fruitIconView;

        public FruitViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitIconView = itemView.findViewById(R.id.fruit_icon_view);
        }

        void bind(int iconResId) {
            if (iconResId != 0) {
                fruitIconView.setImageResource(iconResId);
            } else {
                // Default icon set to view
                fruitIconView.setImageResource(R.drawable.ic_plant);
            }
        }
    }
}
