package in.projectmanas.hub.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.projectmanas.hub.Adapters.HomeRecyclerAdapter;
import in.projectmanas.hub.R;

/**
 * Created by knnat on 13-07-2017.
 */

public class WeeklyFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> s = new ArrayList<>();
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView recyclerView;

    public static WeeklyFragment getInstance() {
        return new WeeklyFragment();
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
        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(), s);
        recyclerView.setAdapter(homeRecyclerAdapter);
        return view;
    }

    public void setStringList(ArrayList<ArrayList<String>> output) {
        for (ArrayList<String> row : output) {
            s.add(row.get(0) + " : " + row.get(1));
        }
    }
}
