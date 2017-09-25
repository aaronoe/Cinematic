package de.aaronoe.cinematic.ui.search;

import android.content.Context;
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
import de.aaronoe.cinematic.model.MultiSearch.SearchItem;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 14.04.17.
 */

class MultiSearchAdapter extends RecyclerView.Adapter<MultiSearchAdapter.MultiViewHolder> {

    private List<SearchItem> itemList;
    private Context mContext;
    private MultiSearchItemOnClickHandler onClickHandler;
    static final String MEDIA_TYPE_TV = "tv";
    static final String MEDIA_TYPE_MOVIE = "movie";
    static final String MEDIA_TYPE_PERSON = "person";


    MultiSearchAdapter(Context context, MultiSearchItemOnClickHandler clickHandler) {
        mContext = context;
        onClickHandler = clickHandler;
    }

    @Override
    public MultiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.multi_search_item, parent, false);
        return new MultiViewHolder(view);
    }

    interface MultiSearchItemOnClickHandler {
        void onClick(SearchItem searchItem);
    }

    @Override
    public void onBindViewHolder(MultiViewHolder holder, int position) {
        SearchItem searchItem = itemList.get(position);
        String type = searchItem.getMediaType();
        String picturePath;
        String pictureUrl;
        ImageView currentImageView = holder.searchImageView;

        switch (type) {
            case MEDIA_TYPE_PERSON:

                holder.searchItemTitle.setText(searchItem.getName());
                holder.searchTypeTv.setText(R.string.type_person);

                String knownFor = Utilities.getKnownFor(searchItem.getKnownFor());
                if (knownFor != null && knownFor != "null") {
                    holder.searchItemSubtitle.setText(knownFor);
                }

                picturePath =  searchItem.getProfilePath();
                pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;

                // use picasso to load the image into the view
                Picasso.with(mContext)
                        .load(pictureUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(currentImageView);

                break;

            case MEDIA_TYPE_MOVIE:

                holder.searchItemTitle.setText(searchItem.getTitle());
                holder.searchItemSubtitle.setText(Utilities.convertDate(searchItem.getReleaseDate()));
                holder.searchTypeTv.setText(R.string.type_movie);

                picturePath =  searchItem.getPosterPath();
                pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;
                // get a reference to this item's ImageView
                // use picasso to load the image into the view
                Picasso.with(mContext)
                        .load(pictureUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(currentImageView);

                break;

            case MEDIA_TYPE_TV:

                holder.searchItemTitle.setText(searchItem.getName());
                holder.searchItemSubtitle.setText(Utilities.convertDate(searchItem.getFirstAirDate()));
                holder.searchTypeTv.setText(R.string.type_tv);

                picturePath =  searchItem.getPosterPath();
                pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;
                // get a reference to this item's ImageView
                // use picasso to load the image into the view
                Picasso.with(mContext)
                        .load(pictureUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(currentImageView);

                break;
        }

    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.search_item_imageview)
        ImageView searchImageView;
        @BindView(R.id.search_item_title)
        TextView searchItemTitle;
        @BindView(R.id.search_item_subtitle)
        TextView searchItemSubtitle;
        @BindView(R.id.multi_search_type_tv)
        TextView searchTypeTv;

        MultiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            SearchItem searchItem = itemList.get(adapterPosition);
            onClickHandler.onClick(searchItem);
        }
    }

    public void setData(List<SearchItem> searchItemList) {
        itemList = searchItemList;
        notifyDataSetChanged();
    }

}
