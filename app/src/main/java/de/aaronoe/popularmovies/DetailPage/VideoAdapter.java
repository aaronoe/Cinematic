package de.aaronoe.popularmovies.DetailPage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Movies.VideoItem;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaron on 21.02.17.
 */

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {


    private List<VideoItem> videoItemList;
    private Context mContext;

    VideoAdapter(Context callerContext){
        mContext = callerContext;
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {

        // Set the video name to the TextView
        VideoItem thisItem = videoItemList.get(position);
        String videoTitle = thisItem.getName();
        holder.mMovieTitleTextView.setText(videoTitle);

        // Set thumbnail video
        final String videoKey = thisItem.getKey();
        String thumbnailUrl = "http://img.youtube.com/vi/"+ videoKey +"/0.jpg";
        Picasso.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.mThumbnailImageView);

        holder.mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent
                        (Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey)));

            }
        });

    }

    @Override
    public int getItemCount() {
        if (videoItemList == null) return 0;
        return videoItemList.size();
    }


    void setVideoData(List<VideoItem> videoData) {
        videoItemList = videoData;
        notifyDataSetChanged();
    }


    class VideoAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.full_movie_title) TextView mMovieTitleTextView;
        @BindView(R.id.thumbnail_picture_detail) ImageView mThumbnailImageView;

        VideoAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }




}
