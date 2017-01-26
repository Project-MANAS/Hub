package in.projectmanas.android.hub.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.projectmanas.android.hub.R;

import java.util.List;

import in.projectmanas.android.hub.backend.PMUserWrapper;
import in.projectmanas.android.hub.user.UserListFragment.OnListFragmentInteractionListener;

class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

	private final List<PMUserWrapper> mValues;
	private final OnListFragmentInteractionListener mListener;

	UserRecyclerViewAdapter(List<PMUserWrapper> items, OnListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_user, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.mItem = mValues.get(position);
		holder.nameView.setText(holder.mItem.getFullName());
		holder.divisionView.setText(holder.mItem.getDivision());
		holder.levelView.setText(holder.mItem.getLevel());
		//holder.ratingView.setProgress(mValues.get(position).rating);

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					mListener.onListItemSelected(holder.mItem);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		final View mView;
		final TextView nameView;
		final TextView levelView;
		final TextView divisionView;
		final ProgressBar ratingView;
		//public final TextView tag1View, tag2View, tag3View, tag4View;
		PMUserWrapper mItem;

		ViewHolder(View view) {
			super(view);
			mView = view;
			nameView = (TextView) view.findViewById(R.id.tv_item_user_name);
			divisionView = (TextView) view.findViewById(R.id.tv_item_user_division);
			levelView = (TextView) view.findViewById(R.id.tv_item_user_level);
			ratingView = (ProgressBar) view.findViewById(R.id.pm_item_user_rating);
		}

	}
}
