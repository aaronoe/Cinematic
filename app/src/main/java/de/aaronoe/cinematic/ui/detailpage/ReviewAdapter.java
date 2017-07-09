package de.aaronoe.cinematic.ui.detailpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.movies.ReviewItem;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 22.02.17.
 */

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<ReviewItem> reviewItemList;
    private Context mContext;

    ReviewAdapter(Context context){
        mContext = context;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        ReviewItem thisReview = reviewItemList.get(position);


        String author = thisReview.getAuthor();
        String reviewText = thisReview.getContent();
        final String url = thisReview.getUrl();

        holder.reviewAuthorTv.setText(author);
        //holder.reviewTextTv.setText(reviewText);
        holder.expandableTextView.setText(reviewText);


        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }

            }
        });


    }

    void setReviewData(List<ReviewItem> reviewData) {
        reviewItemList = reviewData;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (reviewItemList == null) return 0;
        return reviewItemList.size();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_author)
        TextView reviewAuthorTv;
        @BindView(R.id.expandable_text)
        TextView reviewTextTv;
        @BindView(R.id.button_review_link)
        Button reviewButton;
        @BindView(R.id.expand_text_view)
        ExpandableTextView expandableTextView;


        ReviewAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
