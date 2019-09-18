package id.govca.recyclerviewapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import id.govca.recyclerviewapi.alarm.AlarmReceiver;
import id.govca.recyclerviewapi.fragment.FavoriteMovieFragment;
import id.govca.recyclerviewapi.fragment.FavoriteTVShowFragment;
import id.govca.recyclerviewapi.fragment.MovieFragment;
import id.govca.recyclerviewapi.fragment.TVShowFragment;
import id.govca.recyclerviewapi.helper.TabsAdapter;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener, TVShowFragment.OnFragmentInteractionListener, FavoriteMovieFragment.OnFragmentInteractionListener, FavoriteTVShowFragment.OnFragmentInteractionListener {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout tabLayout = findViewById(R.id.tabLayout_movies);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(tabsAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        else if (item.getItemId() == R.id.action_alarm_settings)
        {
            Intent intent = new Intent(this, SetAlarmActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
