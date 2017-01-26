package in.projectmanas.android.hub.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspirephile.shared.debug.Logger;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import in.projectmanas.android.hub.R;
import in.projectmanas.android.hub.backend.PMUserWrapper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UserListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

	// TODO: Customize parameter argument names
	private static final String ARG_COLUMN_COUNT = "column-count";
	private static final String ARG_PID = "_id";
	private static final String ARG_STANDING = "STANDING";
	private Logger l = new Logger(UserListFragment.class);

	private int mColumnCount = 1;
	private OnListFragmentInteractionListener mListener;
	private List<PMUserWrapper> pointItems = new ArrayList<>();
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private String PID;
	private char standing = '\0';

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public UserListFragment() {
	}

	public static UserListFragment newInstance(int columnCount) {
		UserListFragment fragment = new UserListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	public static UserListFragment newInstance(int columnCount, @IntRange(from = 0) int PID, char standing) {
		UserListFragment fragment = new UserListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		args.putInt(ARG_PID, PID);
		args.putChar(ARG_STANDING, standing);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		l.onCreate();
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
			PID = getArguments().getString(ARG_PID);
			standing = getArguments().getChar(ARG_STANDING);
		}

		onRefresh();

	}

	@Override
	public void onResume() {
		super.onResume();
		onRefresh();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_list, container, false);

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_point_list);
		swipeRefreshLayout.setOnRefreshListener(this);
		// Set the adapter
		recyclerView = (RecyclerView) view.findViewById(R.id.rv_point_list);
		Context context = view.getContext();
		if (mColumnCount <= 1) {
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
		} else {
			recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
		}
		recyclerView.setAdapter(new UserRecyclerViewAdapter(pointItems, mListener));

		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onRefresh() {
		l.d("onRefresh");
		if (swipeRefreshLayout != null)
			swipeRefreshLayout.setRefreshing(true);

		ParseUser.getQuery().orderByAscending(PMUserWrapper.division)
				.findInBackground(new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> objects, ParseException e) {
						if (e != null) {
							e.printStackTrace();
							mListener.onListLoadFailed(e);
						} else {
							List<PMUserWrapper> list = PMUserWrapper.fromList(objects);
							if (isVisible()) {
								recyclerView.setAdapter(new UserRecyclerViewAdapter(list, mListener));
								swipeRefreshLayout.setRefreshing(false);
							}
						}
					}
				});
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnListFragmentInteractionListener {
		void onListItemSelected(PMUserWrapper item);

		void onListLoadFailed(ParseException e);
	}
}
