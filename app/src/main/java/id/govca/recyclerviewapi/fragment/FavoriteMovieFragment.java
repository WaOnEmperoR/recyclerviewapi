package id.govca.recyclerviewapi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.adapter.ListFavoriteMovieAdapter;
import id.govca.recyclerviewapi.adapter.ListMovieAdapter;
import id.govca.recyclerviewapi.entity.Favorite;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.viewmodel.FavoriteMovieListViewModel;
import id.govca.recyclerviewapi.viewmodel.MovieListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = this.getClass().getSimpleName();
    private View mProgressView;

    private ListFavoriteMovieAdapter listFavoriteMovieAdapter;
    private RecyclerView rvFavorites;

    private OnFragmentInteractionListener mListener;

    private FavoriteMovieListViewModel favoriteMovieListViewModel;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteMovieFragment newInstance(String param1, String param2) {
        FavoriteMovieFragment fragment = new FavoriteMovieFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        mProgressView = view.findViewById(R.id.progressBarFavoriteMovie);

        showLoading(true);

        rvFavorites = view.findViewById(R.id.recyclerView_favorite_movie);
        rvFavorites.setHasFixedSize(true);

        listFavoriteMovieAdapter = new ListFavoriteMovieAdapter();
        listFavoriteMovieAdapter.notifyDataSetChanged();

        favoriteMovieListViewModel = ViewModelProviders.of(this).get(FavoriteMovieListViewModel.class);
        favoriteMovieListViewModel.getListFavoriteMovies().observe(this, getFavoriteMovieList);

        favoriteMovieListViewModel.setListFavoriteMovies();

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setAdapter(listFavoriteMovieAdapter);

        return view;
    }

    private Observer<List<Favorite>> getFavoriteMovieList = new Observer<List<Favorite>>() {
        @Override
        public void onChanged(List<Favorite> favorites) {
            if (favorites!=null)
            {
                ArrayList<Favorite> theFavorites = new ArrayList<>(favorites);
                listFavoriteMovieAdapter.setData(theFavorites);
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
}
