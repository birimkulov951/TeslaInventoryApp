package com.example.teslainventory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teslainventory.room.Tesla;

import java.io.File;

public class TeslaAdapter extends ListAdapter<Tesla, TeslaAdapter.TeslaHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Tesla> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tesla>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tesla oldItem, @NonNull Tesla newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tesla oldItem, @NonNull Tesla newItem) {
            return oldItem.getId() == newItem.getId() &&
                    oldItem.getModel().equals(newItem.getModel()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getAvailableQuantity().equals(newItem.getAvailableQuantity()) &&
                    oldItem.getInventoryType().equals(newItem.getInventoryType()) &&
                    oldItem.getExteriorPaint().equals(newItem.getExteriorPaint()) &&
                    oldItem.getTeslaImage().equals(newItem.getTeslaImage()) &&
                    oldItem.getPrice().equals(newItem.getPrice()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    public TeslaAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TeslaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tesla_item, parent, false);
        return new TeslaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeslaHolder holder, int position) {
        Tesla currentTesla = getItem(position);
        holder.mModel.setText(currentTesla.getModel());
        holder.mPrice.setText(currentTesla.getPrice());
        holder.mAvailableQuantity.setText(currentTesla.getAvailableQuantity());
        holder.mDescription.setText(currentTesla.getDescription());
        holder.mInventoryType.setText(currentTesla.getInventoryType());
        holder.mExteriorPaint.setText(currentTesla.getExteriorPaint());

        File imgFile = new File(currentTesla.getTeslaImage());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            holder.mTeslaImage.setImageBitmap(myBitmap);

        }

    }

    public Tesla getTeslaAt(int position) {
        return getItem(position);
    }


    //**********************************************************************************************


    class TeslaHolder extends RecyclerView.ViewHolder {

        private TextView mModel;
        private TextView mPrice;
        private TextView mAvailableQuantity;
        private TextView mDescription;
        private TextView mInventoryType;
        private TextView mExteriorPaint;
        private ImageView mTeslaImage;


        public TeslaHolder(@NonNull View itemView) {
            super(itemView);
            mModel = itemView.findViewById(R.id.model);
            mPrice = itemView.findViewById(R.id.price);
            mAvailableQuantity = itemView.findViewById(R.id.available_quantity);
            mDescription = itemView.findViewById(R.id.description);
            mInventoryType = itemView.findViewById(R.id.inventory_type);
            mExteriorPaint = itemView.findViewById(R.id.exterior_paint);
            mTeslaImage = itemView.findViewById(R.id.image_view_tesla);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Tesla note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}