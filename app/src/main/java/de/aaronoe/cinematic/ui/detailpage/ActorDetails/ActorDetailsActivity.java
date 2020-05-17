package de.aaronoe.cinematic.ui.detailpage.ActorDetails;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.ui.detailpage.DetailActivity;
import de.aaronoe.cinematic.movies.MovieItem;
import de.aaronoe.cinematic.movies.MovieResponse;
import de.aaronoe.cinematic.CinematicApp;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.model.ActorCredits.Actor;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import de.aaronoe.cinematic.model.MovieAdapter;
import de.aaronoe.cinematic.util.BackStackManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailsActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    MovieAdapter movieAdapter;
    Actor thisActor;
    int actorId;
    List<MovieItem> movieItemList;
    private static final String SORT_ORDER = "popularity.desc";

    @Inject ApiInterface apiService;

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
    protected void onDestroy() {
        super.onDestroy();
        BackStackManager.getInstance().popActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BackStackManager.getInstance().pushActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);

        ButterKnife.bind(this);

        ((CinematicApp) getApplication()).getNetComponent().inject(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_cast_id))) {
                actorId = intentThatStartedThisActivity.getIntExtra(getString(R.string.intent_key_cast_id), -1);

            }
        }

        downloadActorData(actorId);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        actorCreditsRv.setLayoutManager(linearLayoutManager);
        actorCreditsRv.setNestedScrollingEnabled(false);

        movieAdapter = new MovieAdapter(this, this);
        actorCreditsRv.setAdapter(movieAdapter);

        downloadMovies(actorId);
    }

    private void downloadMovies(int id) {

        Call<MovieResponse> movieResponseCall =
                apiService.discoverMoviesForActor(API_KEY, SORT_ORDER, String.valueOf(id));

        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response == null || response.body() == null || response.body().getResults() == null) {
                    return;
                }

                movieItemList = response.body().getResults();

                if (movieItemList != null) {
                    actorCreditsRv.setVisibility(View.VISIBLE);
                    movieAdapter.setMovieData(movieItemList);
                } else {
                    actorCreditsRv.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

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

    @Override
    public void onClick(MovieItem movieItem) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieId", movieItem.getId());
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_movie_name), movieItem.getTitle());
        startActivity(intentToStartDetailActivity);
    }

}
