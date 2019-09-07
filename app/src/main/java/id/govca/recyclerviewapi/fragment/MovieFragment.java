package id.govca.recyclerviewapi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.govca.recyclerviewapi.DetailActivity;
import id.govca.recyclerviewapi.R;
import id.govca.recyclerviewapi.adapter.ListMovieAdapter;
import id.govca.recyclerviewapi.helper.Constants;
import id.govca.recyclerviewapi.pojo.Movie;
import id.govca.recyclerviewapi.pojo.MovieList;
import id.govca.recyclerviewapi.rest.ApiClient;
import id.govca.recyclerviewapi.rest.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
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

    private RecyclerView rvMovies;

    private OnFragmentInteractionListener mListener;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mProgressView = view.findViewById(R.id.progressBarMovie);

        rvMovies = view.findViewById(R.id.recyclerView_movie);
        rvMovies.setHasFixedSize(true);

        ObserveMovie();

        return view;
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

    private Observable<MovieList> getMovieListObs(){
        final ApiInterface mApiService = ApiClient.getClient().create(ApiInterface.class);

        return mApiService.RxGetMovieList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void ObserveMovie()
    {
        mProgressView.setVisibility(View.VISIBLE);

        Observable<MovieList> movieListObservable = getMovieListObs();

        disposable.add(
                movieListObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<MovieList>() {
                        @Override
                        public void onNext(MovieList movieList) {
//                            DividerItemDecoration divider = new DividerItemDecoration(rvMovies.getContext(), DividerItemDecoration.VERTICAL);
//                            divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
//                            rvMovies.addItemDecoration(divider);

                            for (int i=0; i<movieList.getMovieArrayList().size(); i++)
                            {
                                Log.d(TAG, movieList.getMovieArrayList().get(i).getTitle());
                            }

                            rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
                            ListMovieAdapter listMovieAdapter = new ListMovieAdapter(movieList.getMovieArrayList());
                            rvMovies.setAdapter(listMovieAdapter);

                            listMovieAdapter.setOnItemClickCallback(new ListMovieAdapter.OnItemClickCallback() {
                                @Override
                                public void onItemClicked(Movie data) {
                                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                                    intent.putExtra("Movie_ID", data.getId());
                                    intent.putExtra("Category", "Movie");
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Observable error : " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            mProgressView.setVisibility(View.GONE);

                            Log.d(TAG, "onComplete from Test Observable");

                            this.dispose();
                        }
                    })
        );
    }
}
