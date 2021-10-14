package com.example.android_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class GamesListFragment extends ListFragment {


    HashMap<Integer,String> games_map = new HashMap<>();
    List<String> games_list = new ArrayList<>();

    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.games_found_fragment, container, false);
        adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, games_list);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(requireActivity().getBaseContext(), games_list.get(position), Toast.LENGTH_SHORT).show();
    }

   public void updateList(HashMap<Integer,String> games_map){
        this.games_map = games_map;

        games_list.clear();
        games_list.addAll(games_map.values());

        adapter.notifyDataSetChanged();
   }

}