package de.aaronoe.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.R;
import de.aaronoe.popularmovies.ui.Favorites.FavoritesAdapter;

public class FavoritesActivity extends AppCompatActivity {

    @BindView(R.id.favorites_tabs)
    TabLayout tabs;
    @BindView(R.id.favorites_appbar)
    AppBarLayout appbar;
    @BindView(R.id.detailpage_viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        setUpViewPager();
    }

    private void setUpViewPager(){
        viewpager.setAdapter(new FavoritesAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewpager);
    }

}
