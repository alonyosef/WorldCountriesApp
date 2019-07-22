package com.alon.yosef.countries.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alon.yosef.countries.service.model.Country;
import com.alon.yosef.countries.R;
import com.alon.yosef.countries.ui.adapters.RecyclerViewAdapter;
import com.alon.yosef.countries.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecyclerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerFragment extends Fragment implements RecyclerViewAdapter.OnCountryListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String COUNTRIES_TAG = "countries";
    public static final String COUNTRY_TAG = "country";

    // TODO: Rename and change types of parameters
    private ArrayList<Country> countries;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private RecyclerViewAdapter adapter;

    private OnFragmentInteractionListener mListener;

    private @IdRes int lastSortRes = R.id.sortByName;

    private boolean sortAsc = true;

    private boolean active = true;

    public RecyclerFragment() { }

    public static RecyclerFragment newInstance(ArrayList<Country> countries, Country country) {
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args = new Bundle();
        args.putSerializable(COUNTRIES_TAG, countries);
        args.putSerializable(COUNTRY_TAG, country);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            countries = (ArrayList<Country>) getArguments().getSerializable(COUNTRIES_TAG);
    }

    private void setCountries() {
        if (getArguments() != null) {
            countries = (ArrayList<Country>) getArguments().getSerializable(COUNTRIES_TAG);
        }
    }

    public Country getCountry() {
        return (Country) getArguments().getSerializable(COUNTRY_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view_layout, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        adapter = new RecyclerViewAdapter(countries, this);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentResume(this);
        if (!active) {
            setCountries();
            active = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        active = false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onFragmentResume(RecyclerFragment fragment);
    }

    public boolean sortList(@IdRes int sortByRes) {
        if (lastSortRes == sortByRes) {
            Collections.reverse(countries);
            sortAsc = !sortAsc;
        } else {
            if (sortByRes == R.id.sortByName)
                Collections.sort(countries, COMPARE_NAMES);
            else if (sortByRes == R.id.sortByArea)
                Collections.sort(countries, COMPARE_AREAS);
            lastSortRes = sortByRes;
            sortAsc = true;
        }
        adapter.notifyDataSetChanged();
        return sortAsc;
    }

    public @IdRes int getLastSortRes() {
        return lastSortRes;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    private static final Comparator<Country> COMPARE_NAMES = new Comparator<Country>() {
        @Override
        public int compare(Country c1, Country c2) {
            return c1.getName().compareTo(c2.getName());
        }
    };

    private static final  Comparator<Country> COMPARE_AREAS = new Comparator<Country>() {
        @Override
        public int compare(Country c1, Country c2) {
            return (int) (c1.getArea() - c2.getArea());
        }
    };

    @Override
    public void onCountryClick(int position) {
        Country country = countries.get(position);
        String[] countryCodes = country.getBorders();
        if (countryCodes == null || countryCodes.length == 0)
            Toast.makeText(getActivity(), R.string.no_bordering_countries, Toast.LENGTH_LONG).show();
        else
            ((MainActivity)getActivity()).setNewFragment(countryCodes, country);
    }
}
