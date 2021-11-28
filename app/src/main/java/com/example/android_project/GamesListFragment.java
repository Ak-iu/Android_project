package com.example.android_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class GamesListFragment extends Fragment {

    Map<Integer, String> games_map = new HashMap<>();
    List<Game> games_list = new ArrayList<>();
    private GamesRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (adapter == null) adapter = new GamesRecyclerViewAdapter(games_list,mListener);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateList(Map<Integer, String> games_map, String searched_game) {
        this.games_map = games_map;
        List<Game> gameList = mapToList(games_map);
        games_list.clear();

        List<Game> r_gameList = sortByRelevance(gameList, searched_game);
        games_list.addAll(r_gameList);

        adapter.notifyDataSetChanged();
    }

    public List<Game> mapToList(Map<Integer, String> games_map) {
        List<Game> gameList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : games_map.entrySet()) {
            gameList.add(new Game(entry.getKey(), entry.getValue()));
        }
        return gameList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Game> sortByRelevance(List<Game> gameList, String searched_game) {
        List<Game> a_list = new LinkedList<>();
        List<Game> b_list = new LinkedList<>();

        for (Game game : gameList) {
            if (game.getName().toLowerCase(Locale.ROOT).startsWith(searched_game.toLowerCase(Locale.ROOT)))
                a_list.add(game);
            else b_list.add(game);
        }

        Collections.sort(a_list, new SortByName());
        Collections.sort(b_list, new SortByName());

        a_list.addAll(b_list);

        return a_list;
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(int appid, String name);
    }

}