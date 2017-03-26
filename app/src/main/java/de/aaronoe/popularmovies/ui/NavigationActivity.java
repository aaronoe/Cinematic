package de.aaronoe.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.R;

public class NavigationActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.frame)
    FrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_menu__activity);

        ButterKnife.bind(this);

        // Setting toolbar as the actionbar
        setSupportActionBar(toolbar);

        // set up navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Checking if the item is in checked state or not, if not make it in checked state
                item.setChecked(!item.isChecked());

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.drawer_menu_movies:
                        Toast.makeText(NavigationActivity.this, "Movies Seleted", Toast.LENGTH_SHORT).show();
                        MoviesFragment moviesFragment = new MoviesFragment();
                        FragmentTransaction moviesFragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        moviesFragmentTransaction.replace(R.id.frame, moviesFragment);
                        moviesFragmentTransaction.commit();
                        return true;

                    case R.id.drawer_menu_shows:
                        Toast.makeText(NavigationActivity.this, "Shows selected", Toast.LENGTH_SHORT).show();
                        TvShowsFragment tvShowsFragment = new TvShowsFragment();
                        FragmentTransaction showsFragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        showsFragmentTransaction.replace(R.id.frame, tvShowsFragment);
                        showsFragmentTransaction.commit();
                        return true;

                    case R.id.drawer_menu_actors:
                        Toast.makeText(NavigationActivity.this, "Actors selected", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        Toast.makeText(NavigationActivity.this, "Something went terribly wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }

            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


}
