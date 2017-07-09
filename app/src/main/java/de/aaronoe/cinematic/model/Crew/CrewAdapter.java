package de.aaronoe.cinematic.model.Crew;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.ui.detailpage.ActorDetails.ActorDetailsActivity;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 11.03.17.
 */

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    private List<Cast> castList;
    private Context mContext;

    public CrewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.cast_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CrewViewHolder holder, int position) {

        final Cast castItem = castList.get(position);

        String characterName = castItem.getCharacter();
        String actorName = castItem.getName();
        String picPath = castItem.getProfilePath();
        String pictureUrl = "http://image.tmdb.org/t/p/w342/" + picPath;

        holder.actorNameTextView.setText(actorName);
        holder.characterNameTextView.setText(characterName);
        RoundedImageView actorIv = holder.actorImageView;

        Picasso.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(actorIv);

        actorIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStartActorDetailActivity = new Intent(mContext, ActorDetailsActivity.class);
                intentToStartActorDetailActivity.putExtra(mContext.getString(R.string.intent_key_cast_id), castItem.getId());
                mContext.startActivity(intentToStartActorDetailActivity);
            }
        });

    }

    public void setCastData(List<Cast> castData) {
        castList = castData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (castList == null) {
            return 0;
        }
        return castList.size();
    }

    class CrewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.actor_iv) RoundedImageView actorImageView;
        @BindView(R.id.actor_name) TextView actorNameTextView;
        @BindView(R.id.character_name) TextView characterNameTextView;

        public CrewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
