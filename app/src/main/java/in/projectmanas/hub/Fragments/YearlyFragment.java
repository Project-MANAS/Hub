package in.projectmanas.hub.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.projectmanas.hub.Adapters.HomeRecyclerAdapter;
import in.projectmanas.hub.AsyncResponse;
import in.projectmanas.hub.R;

/**
 * Created by knnat on 13-07-2017.
 */

public class YearlyFragment extends Fragment implements AsyncResponse {

    SwipeRefreshLayout swipeRefreshLayout;
    List<String> s = new ArrayList<>();
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView recyclerView;

    public static YearlyFragment getInstance() {
        return new YearlyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity());
        recyclerView.setAdapter(homeRecyclerAdapter);
        return view;
    }

    @Override
    public void processFinish(ArrayList<ArrayList<String>> output) {
        //Just logging here for checking the fetched data
        Log.d("check", output.size() + " ");
        for (List<String> row : output) {
            homeRecyclerAdapter.addTolist(row.get(0)+row.get(3));
            homeRecyclerAdapter.notifyDataSetChanged();
        }

    }
}
