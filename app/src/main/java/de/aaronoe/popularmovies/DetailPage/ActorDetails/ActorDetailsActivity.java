package de.aaronoe.popularmovies.DetailPage.ActorDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ActorCredits.Actor;
import de.aaronoe.popularmovies.Data.ActorCredits.ActorCredits;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailsActivity extends AppCompatActivity {

    CreditsAdapter creditsAdapter;
    Actor thisActor;
    int actorId;
    ApiInterface apiService;
    List<de.aaronoe.popularmovies.Data.ActorCredits.Cast> castList;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    @BindView(R.id.actor_screen_profile)
    CircleImageView actorScreenProfile;
    @BindView(R.id.actor_biography_h1)
    TextView actorBiographyH1;
    @BindView(R.id.expandable_text)
    TextView expandableText;
    @BindView(R.id.expand_collapse)
    ImageButton expandCollapse;
    @BindView(R.id.expand_text_view)
    ExpandableTextView expandTextView;
    @BindView(R.id.actor_details_name)
    TextView actorDetailsName;
    @BindView(R.id.actor_credits_rv)
    RecyclerView actorCreditsRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_cast_id))) {
                actorId = intentThatStartedThisActivity.getIntExtra(getString(R.string.intent_key_cast_id), -1);

            }
        }
        apiService = ApiClient.getClient().create(ApiInterface.class);

        downloadActorData(actorId);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        actorCreditsRv.setLayoutManager(linearLayoutManager);
        actorCreditsRv.setNestedScrollingEnabled(false);
        creditsAdapter = new CreditsAdapter(this);
        actorCreditsRv.setAdapter(creditsAdapter);

        downloadCredits(actorId);

    }


    public void downloadCredits(int id) {
        Call<ActorCredits> call = apiService.getActorCredits(id, API_KEY);

        call.enqueue(new Callback<ActorCredits>() {
            @Override
            public void onResponse(Call<ActorCredits> call, Response<ActorCredits> response) {
                castList = response.body().getCast();

                if (castList != null) {
                    actorCreditsRv.setVisibility(View.VISIBLE);
                    creditsAdapter.setCastList(castList);
                } else {
                    actorCreditsRv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ActorCredits> call, Throwable t) {

            }
        });
    }


    public void downloadActorData(int id) {

        Call<Actor> call = apiService.getActorDetails(id, API_KEY);

        call.enqueue(new Callback<Actor>() {
            @Override
            public void onResponse(Call<Actor> call, Response<Actor> response) {
                thisActor = response.body();

                populateWithData();
            }

            @Override
            public void onFailure(Call<Actor> call, Throwable t) {
                Toast.makeText(ActorDetailsActivity.this, "Couldn't download data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateWithData() {

        String actorName = thisActor.getName();
        String actorBio = thisActor.getBiography();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actorName);
        }

        actorDetailsName.setText(actorName);
        expandTextView.setText(actorBio);

        String picturePath = thisActor.getProfilePath();
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;

        Picasso.with(ActorDetailsActivity.this)
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(actorScreenProfile);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
