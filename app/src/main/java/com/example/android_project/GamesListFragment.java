package com.example.android_project;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class GamesListFragment extends ListFragment {


    HashMap<Integer, String> games_map = new HashMap<>();

    List<String> games_list = new ArrayList<>();

    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.games_found_fragment, container, false);
        adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, games_list);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(requireActivity().getBaseContext(), games_list.get(position), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateList(HashMap<Integer, String> games_map, String searched_game) {
        this.games_map = games_map;
        games_list.clear();

        List<String> game_list = sortByRelevance(games_map.values(), searched_game);
        games_list.addAll(game_list);

        adapter.notifyDataSetChanged();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> sortByRelevance(Collection<String> names, String searched_game) {
        List<String> a_list = new ArrayList<>();
        List<String> b_list = new ArrayList<>();


        for (String name : names) {
            if (name.toLowerCase(Locale.ROOT).startsWith(searched_game.toLowerCase(Locale.ROOT)))
                a_list.add(name);
            else b_list.add(name);
        }

        a_list.sort(Comparator.comparing(String::length));
        b_list.sort(Comparator.comparing(String::length));

        a_list.addAll(b_list);
        return a_list;
    }
}