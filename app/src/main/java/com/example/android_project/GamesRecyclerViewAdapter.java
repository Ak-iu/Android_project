package com.example.android_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GamesRecyclerViewAdapter extends RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder> {

    private final List<Game> mValues;
    private final GamesListFragment.OnListFragmentInteractionListener mListener;

    public GamesRecyclerViewAdapter(List<Game> _games, GamesListFragment.OnListFragmentInteractionListener listener) {
        this.mValues = _games;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener)
                mListener.onListFragmentInteraction(mValues.get(position).getAppiid(), mValues.get(position).getName());
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Game mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getName() + "'";
        }
    }

}
