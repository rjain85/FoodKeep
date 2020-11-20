package com.example.foodkeepproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements GroceryFragment.GroceryListener, PantryFragment.PantryListener, SettingsFragment.SettingsListener {

    final PantryFragment pantryFragment = new PantryFragment();
    final GroceryFragment groceryFragment = new GroceryFragment();
    final SettingsFragment settingsFragment = new SettingsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = pantryFragment;

    private TextView headerText;
    private ImageButton pantryTrash;
    private ImageButton pantryTrashDone;
    private boolean deleteMode = false;

    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.contentFrame, settingsFragment, "settings").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, groceryFragment, "grocery").hide(groceryFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, pantryFragment, "pantry").commit();

        headerText = (TextView) findViewById(R.id.headerTitle);
        pantryTrash = (ImageButton) findViewById(R.id.pantryTrash);
        pantryTrashDone = (ImageButton) findViewById(R.id.pantryTrashDone);

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigationBar);

        nav.setOnNavigationItemSelectedListener(
                item -> {
                    if (active == pantryFragment) {
                        if (deleteMode) {
                            pantryTrashDone.setVisibility(View.GONE);
                        } else {
                            pantryTrash.setVisibility(View.GONE);
                        }
                    }
                    switch (item.getItemId()) {
                        case R.id.pantry:
                            fm.beginTransaction().hide(active).show(pantryFragment).commit();
                            active = pantryFragment;
                            headerText.setText("Pantry");

                            if (deleteMode) {
                                pantryTrashDone.setVisibility(View.VISIBLE);
                            } else {
                                pantryTrash.setVisibility(View.VISIBLE);
                            }

                            break;
                        case R.id.grocery:
                            fm.beginTransaction().hide(active).show(groceryFragment).commit();
                            active = groceryFragment;
                            headerText.setText("Grocery List");
                            break;
                        case R.id.settings:
                            fm.beginTransaction().hide(active).show(settingsFragment).commit();
                            active = settingsFragment;
                            headerText.setText("Settings");
                            break;
                    }
                    return true;
                }
        );
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof GroceryFragment) {
            GroceryFragment groceryFragment = (GroceryFragment) fragment;
            groceryFragment.setGroceryListener(this);
        }

        if (fragment instanceof PantryFragment) {
            PantryFragment pantryFragment = (PantryFragment) fragment;
            pantryFragment.setPantryListener(this);
        }

        if (fragment instanceof SettingsFragment) {
            SettingsFragment settingsFragment = (SettingsFragment) fragment;
            settingsFragment.setSettingsListener(this);
        }
    }

    public void onPantryTrashClick(View view) {
        pantryTrash.setVisibility(View.GONE);
        deleteMode = true;
        pantryFragment.enterDeleteMode();
        pantryTrashDone.setVisibility(View.VISIBLE);
    }

    public void onPantryTrashDoneClick(View view) {
        pantryTrash.setVisibility(View.VISIBLE);
        deleteMode = false;
        pantryFragment.exitDeleteMode();
        pantryTrashDone.setVisibility(View.GONE);
    }

    @Override
    public void onButtonClick() {
        count++;
        groceryFragment.updateCount(count);
        pantryFragment.updateCount(count);
        settingsFragment.updateCount(count);
    }
}