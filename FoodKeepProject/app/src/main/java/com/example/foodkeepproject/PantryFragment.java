package com.example.foodkeepproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PantryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PantryFragment extends Fragment {

    final int ADD_PANTRY_ITEM = 1;
    ArrayList<PantryItem> pantryList;
    PantryListAdapter adapter;
    PantryListener callback;
    FloatingActionButton fab;

    public PantryFragment() {
        // Required empty public constructor
    }

    public static PantryFragment newInstance(String param1, String param2) {
        PantryFragment fragment = new PantryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        pantryList = new ArrayList<>();
        pantryList.add(new PantryItem("Apple", 5));
        pantryList.add(new PantryItem("Banana", 4));

        PantryItemClickListener listener = (name) -> {
            deleteItemPantryList(name);
        };

        RecyclerView pantryItemsList = (RecyclerView) view.findViewById(R.id.pantryList);
        adapter = new PantryListAdapter(pantryList, listener);
        pantryItemsList.setAdapter(adapter);
        pantryItemsList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        fab = (FloatingActionButton) view.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PantryItemAddActivity.class);
                startActivityForResult(intent, ADD_PANTRY_ITEM);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PANTRY_ITEM) {
            if (resultCode == RESULT_OK) {
                String itemName = data.getStringExtra("name");
                int itemCount = data.getIntExtra("count", 0);

                int index = indexPantryList(itemName);

                RecyclerView pantryListView = (RecyclerView) getView().findViewById(R.id.pantryList);

                if (index != -1) {
                    pantryList.get(index).addCount(itemCount);
                    adapter.notifyItemChanged(index);
                } else {
                    pantryList.add(0, new PantryItem(itemName, itemCount));
                    adapter.notifyItemInserted(0);
                    pantryListView.scrollToPosition(0);
                }
            }
        }
    }

    private int indexPantryList(String name) {
        for (int i = 0; i < pantryList.size(); i++) {
            if (pantryList.get(i).getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    private void deleteItemPantryList(String name) {
        int index = indexPantryList(name);
        pantryList.remove(index);
        adapter.notifyItemChanged(index);
        adapter.notifyItemRangeRemoved(index, 1);
    }

    public interface PantryListener {
        public void onButtonClick();
    }

    public void setPantryListener(PantryListener callback) {
        this.callback = callback;
    }

    public void enterDeleteMode() {
        fab.setVisibility(View.GONE);
        for (PantryItem item : pantryList) {
            item.flipVisibility();
        }
        adapter.notifyDataSetChanged();
    }

    public void exitDeleteMode() {
        fab.setVisibility(View.VISIBLE);
        for (PantryItem item : pantryList) {
            item.flipVisibility();
        }
        adapter.notifyDataSetChanged();
    }

    public void updateCount(int count) {
        View view = getView();
        //((TextView) view.findViewById(R.id.pantry_count)).setText(Integer.toString(count));
    }
}