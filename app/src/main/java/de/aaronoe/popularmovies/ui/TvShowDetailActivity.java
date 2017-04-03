package de.aaronoe.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.TvShow.FullShow.Genre;
import de.aaronoe.popularmovies.Data.TvShow.FullShow.TvShowFull;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailActivity extends AppCompatActivity {

    private static final String TAG = "TvShowDetailActivity";

    @BindView(R.id.tv_detail_backdrop)
    ImageView tvDetailBackdrop;
    @BindView(R.id.tv_detail_profile)
    ImageView tvDetailProfile;
    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.tv_detail_year)
    TextView tvDetailYear;
    @BindView(R.id.tv_detail_dividerdot)
    TextView tvDetailDividerdot;
    @BindView(R.id.tv_detail_age_rating)
    TextView tvDetailAgeRating;
    @BindView(R.id.tv_detail_user_score_tv)
    TextView tvDetailUserScoreTv;
    @BindView(R.id.tv_detail_play_trailer)
    TextView tvDetailPlayTrailer;
    @BindView(R.id.show_runtime_status)
    TextView showRuntimeStatus;
    @BindView(R.id.show_runtime_last_air_date)
    TextView showRuntimeLastAirDate;
    @BindView(R.id.show_runtime_first_air_date)
    TextView showRuntimeFirstAirDate;
    @BindView(R.id.show_runtime_minutes)
    TextView showRuntimeMinutes;
    @BindView(R.id.show_runtime_genres)
    TextView showRuntimeGenres;
    @BindView(R.id.show_runtime_nr_seasons)
    TextView showRuntimeNrSeasons;
    @BindView(R.id.show_runtime_nr_episodes)
    TextView showRuntimeNrEpisodes;
    @BindView(R.id.tv_detail_overview)
    TextView tvDetailOverview;

    int movieId;
    ApiInterface apiInterface;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    TvShowFull thisShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_show_details);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            if (startIntent.hasExtra(getString(R.string.intent_key_tv_show))) {
                movieId = startIntent.getIntExtra(getString(R.string.intent_key_tv_show), -1);
            }
        }
        downloadShowDetails();
    }


    public void downloadShowDetails() {

        Call<TvShowFull> call = apiInterface.getTvShowDetails(movieId, API_KEY);

        call.enqueue(new Callback<TvShowFull>() {
            @Override
            public void onResponse(Call<TvShowFull> call, Response<TvShowFull> response) {
                thisShow = response.body();
                if (thisShow != null) {
                    Log.d(TAG, "onResponse: " + thisShow.getOverview());
                    populateViewsWithData();
                }
            }

            @Override
            public void onFailure(Call<TvShowFull> call, Throwable t) {
                Toast.makeText(TvShowDetailActivity.this, "Downloading Data has failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateViewsWithData() {

        // Backdrop
        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + thisShow.getBackdropPath();
        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(tvDetailBackdrop);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + thisShow.getPosterPath();
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(tvDetailProfile);

        tvDetailTitle.setText(thisShow.getName());
        tvDetailYear.setText(thisShow.getFirstAirDate());
        tvDetailOverview.setText(thisShow.getOverview());
        showRuntimeStatus.setText(thisShow.getStatus());
        showRuntimeFirstAirDate.setText(Utilities.convertDate(thisShow.getFirstAirDate()));
        showRuntimeNrEpisodes.setText(String.valueOf(thisShow.getNumberOfEpisodes()));
        showRuntimeNrSeasons.setText(String.valueOf(thisShow.getNumberOfSeasons()));

        int diff = (int) Utilities.computeDifferenceInDays(thisShow.getLastAirDate());
        String lastRuntime = Utilities.convertDate(thisShow.getLastAirDate());


        if (diff <= 0) {
            String daysDifference = getResources()
                    .getQuantityString(R.plurals.x_days_ago_plurals, Math.abs(diff), Math.abs(diff));
            lastRuntime += " " + daysDifference;
        }

        showRuntimeLastAirDate.setText(lastRuntime);
        Log.d(TAG, "populateViewsWithData: "+ diff);

        List<Integer> runtime = thisShow.getEpisodeRunTime();
        String runtimeString = "";
        for (int i = 0; i < runtime.size(); i++) {
            if (i != 0) {
                runtimeString += ", ";
            }
            runtimeString += runtime.get(i);
        }
        runtimeString += " " + getString(R.string.runtime_minutes);

        showRuntimeMinutes.setText(runtimeString);

        List<Genre> genreList = thisShow.getGenres();
        String genreString = "";
        for (int i = 0; i < genreList.size(); i++) {
            if (i != 0) {
                genreString += ", ";
            }
            genreString += genreList.get(i).getName();
        }
        showRuntimeGenres.setText(genreString);

    }

}
