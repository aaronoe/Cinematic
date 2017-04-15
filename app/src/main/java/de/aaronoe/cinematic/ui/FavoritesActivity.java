package de.aaronoe.cinematic.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.ui.Favorites.FavoritesAdapter;

public class FavoritesActivity extends AppCompatActivity {

    private static final String TAG = "FavoritesActivity";
    @BindView(R.id.favorites_tabs)
    TabLayout favoritesTabs;
    @BindView(R.id.favorites_viewpager)
    ViewPager favoritesViewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favorites_h1);
        }

        setUpViewPager();
    }

    private void setUpViewPager() {
        favoritesViewpager.setAdapter(new FavoritesAdapter(getSupportFragmentManager()));
        favoritesTabs.setupWithViewPager(favoritesViewpager);
    }

}
