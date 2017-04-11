package de.aaronoe.popularmovies.ui.Favorites;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 *
 * Created by aaronoe on 11.04.17.
 */

public class FavoritesAdapter extends FragmentPagerAdapter {

    public FavoritesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Movies
                return null;
            case 1:
                // Shows
                return null;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "Movies";
            case 1: return "Shows";
        }
        return "";
    }

}
