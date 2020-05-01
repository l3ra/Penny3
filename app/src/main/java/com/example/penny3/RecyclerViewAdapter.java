package com.example.penny3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<Photo> mPhotosList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<Photo> photosList) {
        mContext = context;
        mPhotosList = photosList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Photo photoItem = mPhotosList.get(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getMessage() + " --> " + position);
//        Picasso.with(mContext).load(photoItem.getRoom())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(holder.thumbnail);

        holder.textView.setText(photoItem.getMessage());
        holder.senderView.setText(photoItem.getSender());
        holder.roomView.setText(photoItem.getRoom());

    }

    @Override
    public int getItemCount() {
        return ((mPhotosList != null) && (mPhotosList.size() !=0) ? mPhotosList.size() : 0);
    }

    void loadNewData (List<Photo> newPhotos) {
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto (int position) {
        return ((mPhotosList != null) && (mPhotosList.size() !=0) ? mPhotosList.get(position) : null);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ImageViewHolder";
        ImageView thumbnail = null;
        TextView textView = null;
        TextView senderView = null;
        TextView roomView = null;

        public ImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ImageViewHolder: starts");

            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.senderView = (TextView) itemView.findViewById(R.id.senderView);
            this.roomView = (TextView) itemView.findViewById(R.id.roomView);
        }
    }
}
