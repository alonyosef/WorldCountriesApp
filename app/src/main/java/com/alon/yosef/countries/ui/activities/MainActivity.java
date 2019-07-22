package com.alon.yosef.countries.ui.activities;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alon.yosef.countries.ui.interfaces.ResponseListener;
import com.alon.yosef.countries.R;
import com.alon.yosef.countries.ui.fragments.RecyclerFragment;
import com.alon.yosef.countries.service.VolleyController;
import com.alon.yosef.countries.service.model.Country;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener {

    public static final String BASE_FRAGMENT_TAG = "baseFragment";
    public static final char UP_ARROW = 0x2191;
    public static final char DOWN_ARROW = 0x2193;

    private VolleyController volleyController;
    private ActionBar actionBar;
    private ArrayList<Country> countries;
    private RecyclerFragment recyclerFragment;
    private FragmentManager fragmentManager;
    private ProgressBar loaderPB;
    private MenuItem sortByNameMI;
    private MenuItem sortByAreaMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        fragmentManager = getSupportFragmentManager();

        loaderPB = findViewById(R.id.loaderPB);

        volleyController = VolleyController.getInstance(this);
        volleyController.getAllCountries(new ResponseListener<ArrayList<Country>>() {
            @Override
            public void onSuccess(ArrayList<Country> response) {
                countries = response;
                recyclerFragment = RecyclerFragment.newInstance(countries, null);
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, recyclerFragment, BASE_FRAGMENT_TAG).commit();
                findViewById(R.id.listViewTitles).setVisibility(View.VISIBLE);
                loaderPB.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception ex) {
                loaderPB.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentResume(RecyclerFragment fragment) {
        recyclerFragment = fragment;
        String tag = fragment.getTag();
        if (tag != null && tag.equals(BASE_FRAGMENT_TAG))
            actionBar.setDisplayHomeAsUpEnabled(false);
        Country country = fragment.getCountry();
        if (country == null)
            actionBar.setTitle(R.string.app_name);
        else
            actionBar.setTitle(getString(R.string.custom_title, country.getName()));
        boolean sortAsc = recyclerFragment.isSortAsc();
        MenuItem mi = recyclerFragment.getLastSortRes() == R.id.sortByName ? sortByNameMI : sortByAreaMI;
        resetMenuItemSortSign(mi, sortAsc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        sortByNameMI = menu.findItem(R.id.sortByName);
        sortByAreaMI = menu.findItem(R.id.sortByArea);

        resetMenuItemSortSign(sortByNameMI);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sortByName:
                boolean sortAsc = recyclerFragment.sortList(R.id.sortByName);
                resetMenuItemSortSign(sortByNameMI, sortAsc);
                break;
            case R.id.sortByArea:
                sortAsc = recyclerFragment.sortList(R.id.sortByArea);
                resetMenuItemSortSign(sortByAreaMI, sortAsc);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void resetMenuItemSortSign(MenuItem mi) {
        String title = (String) mi.getTitle();
        if (mi.equals(sortByNameMI)) {
            mi.setTitle(getString(R.string.menu_sort_name, title.indexOf(DOWN_ARROW) == -1 ? DOWN_ARROW : UP_ARROW));
            sortByAreaMI.setTitle(getString(R.string.menu_sort_area, ' '));
        } else if (mi.equals(sortByAreaMI)) {
            mi.setTitle(getString(R.string.menu_sort_area, title.indexOf(DOWN_ARROW) == -1 ? DOWN_ARROW : UP_ARROW));
            sortByNameMI.setTitle(getString(R.string.menu_sort_name, ' '));
        }
    }

    public void resetMenuItemSortSign(MenuItem mi, boolean sortAsc) {
        if (mi.equals(sortByNameMI)) {
            mi.setTitle(getString(R.string.menu_sort_name, sortAsc ? DOWN_ARROW : UP_ARROW));
            sortByAreaMI.setTitle(getString(R.string.menu_sort_area, ' '));
        } else if (mi.equals(sortByAreaMI)) {
            mi.setTitle(getString(R.string.menu_sort_area, sortAsc ? DOWN_ARROW : UP_ARROW));
            sortByNameMI.setTitle(getString(R.string.menu_sort_name, ' '));
        }
    }

    public void setNewFragment(String[] countryCodes, Country country) {
        ArrayList<Country> borderingCountries = new ArrayList<>();
        for (String countryCode : countryCodes) {
            Country c = Country.getCountryByCode(countries, countryCode);
            if (c != null)
                borderingCountries.add(c);
        }
        if (borderingCountries.size() == 0)
            Toast.makeText(this, R.string.no_bordering_countries, Toast.LENGTH_LONG).show();
        else {
            recyclerFragment = RecyclerFragment.newInstance(borderingCountries, country);
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, recyclerFragment)
                .addToBackStack(null)
                .commit();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.custom_title, country.getName()));
        }
    }
}
