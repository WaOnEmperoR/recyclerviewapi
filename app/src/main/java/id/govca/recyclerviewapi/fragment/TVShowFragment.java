package id.govca.recyclerviewapi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import id.govca.recyclerviewapi.DetailActivity;
import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.adapter.ListMovieAdapter;
import id.govca.recyclerviewapi.adapter.ListTvShowAdapter;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.pojo.TVShow;
import id.govca.recyclerviewapi.pojo.TVShowList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import id.govca.recyclerviewapi.viewmodel.MovieListViewModel;
import id.govca.recyclerviewapi.viewmodel.TvShowListViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TVShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TVShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TVShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CompositeDisposable disposable = new CompositeDisposable();

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;

    private RecyclerView rvTvShow;

    private ListTvShowAdapter listTvShowAdapter;

    private OnFragmentInteractionListener mListener;
    private String param_lang;

    private TvShowListViewModel tvShowListViewModel;

    public TVShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TVShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TVShowFragment newInstance(String param1, String param2) {
        TVShowFragment fragment = new TVShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);
        mProgressView = view.findViewById(R.id.progressBarTvShow);

        setHasOptionsMenu(true);

        showLoading(true);

        rvTvShow = view.findViewById(R.id.recyclerView_tv_show);
        rvTvShow.setHasFixedSize(true);

        listTvShowAdapter = new ListTvShowAdapter();
        listTvShowAdapter.notifyDataSetChanged();

        tvShowListViewModel = ViewModelProviders.of(this).get(TvShowListViewModel.class);
        tvShowListViewModel.getListTvShows().observe(this, getTvShowList);

        Locale current = getResources().getConfiguration().locale;

        param_lang = current.getLanguage() + "-" + current.getCountry();
        if (param_lang.equals("in-ID"))
        {
            param_lang = "id-ID";
        }

        tvShowListViewModel.setListTvShows(param_lang);

        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvShow.setAdapter(listTvShowAdapter);

        listTvShowAdapter.setOnItemClickCallback(new ListTvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TVShow data) {
                Log.d(TAG, String.valueOf(data.getId()));

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("Movie_ID", data.getId());
                intent.putExtra("Category", 1);
                startActivity(intent);
            }
        });

        return view;
    }

    private Observer<TVShowList> getTvShowList = new Observer<TVShowList>() {
        @Override
        public void onChanged(TVShowList tvShowList) {
            if (tvShowList!=null)
            {
                Log.d(TAG, "Calling onChange");
                listTvShowAdapter.setData(tvShowList.getTvShowArrayList());
                showLoading(false);
            }
        }
    };

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showLoading(Boolean state) {
        if (state) {
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mProgressView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchItem);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.title_search_tv_shows));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty())
                    tvShowListViewModel.setSearchTVShows(newText, param_lang);
                else
                    tvShowListViewModel.setListTvShows(param_lang);

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
