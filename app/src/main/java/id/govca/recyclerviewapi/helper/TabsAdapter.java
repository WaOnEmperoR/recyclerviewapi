package id.govca.recyclerviewapi.helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import id.govca.recyclerviewapi.fragment.FavoriteMovieFragment;
import id.govca.recyclerviewapi.fragment.MovieFragment;
import id.govca.recyclerviewapi.fragment.TVShowFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MovieFragment movie = new MovieFragment();
                return movie;
            case 1:
                TVShowFragment tvShow = new TVShowFragment();
                return tvShow;
            case 2:
                FavoriteMovieFragment favoriteMovieFragment = new FavoriteMovieFragment();
                return favoriteMovieFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
