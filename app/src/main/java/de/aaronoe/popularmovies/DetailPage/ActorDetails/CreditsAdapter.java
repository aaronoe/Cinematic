package de.aaronoe.popularmovies.DetailPage.ActorDetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Data.ActorCredits.Cast;
import de.aaronoe.popularmovies.DetailPage.DetailActivity;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaron on 11.03.17.
 */

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditViewHolder> {

    private List<Cast> castList;
    private Context mContext;

    public CreditsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.actor_movie_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new CreditViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CreditViewHolder holder, int position) {

        final Cast castItem = castList.get(position);

        String characterName = castItem.getCharacter();
        String movieName = castItem.getTitle();
        String imagePath = castItem.getPosterPath();
        String pictureUrl = "http://image.tmdb.org/t/p/w342/" + imagePath;

        holder.characterNameTV.setText(characterName);
        holder.movieNameTV.setText(movieName);

        Glide.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.movieImageView);

        holder.movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStartActorDetailActivity = new Intent(mContext, DetailActivity.class);
                intentToStartActorDetailActivity.putExtra("MovieId", castItem.getId());
                mContext.startActivity(intentToStartActorDetailActivity);
            }
        });

    }

    public void setCastList(List<Cast> castData) {
        castList = castData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (castList == null) return 0;
        return castList.size();
    }

    class CreditViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.actor_movie_iv)
        RoundedImageView movieImageView;
        @BindView(R.id.actor_movie_name)
        TextView movieNameTV;
        @BindView(R.id.character_movie_name) TextView characterNameTV;


        public CreditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
