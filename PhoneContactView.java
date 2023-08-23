package com.tuition.createcontactform;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PhoneContactView extends AppCompatActivity {
    AdapterClassForDataShow adapterClassForDataShow;
    ArrayList<DataModelClass> dataList;
    ArrayList<String> list= new ArrayList<>();
    TextView noContact;
    ImageView errorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact_view);

        FloatingActionButton addDataText = findViewById(R.id.add_data);
        getSupportActionBar().hide();
        noContact= findViewById(R.id.noContact);
        errorImage=findViewById(R.id.errorImage);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DataStoreSqlite db = new DataStoreSqlite(this);
        dataList = db.FetchData();

        adapterClassForDataShow = new AdapterClassForDataShow(this, dataList);
        recyclerView.setAdapter(adapterClassForDataShow);

        addDataText.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ContactFormActivity.class);
            startActivity(intent);
            finishAffinity();

        });
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                errorImage.setVisibility(View.GONE);
                noContact.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals(""))
                {
                    adapterClassForDataShow.getFilter().filter(newText);
                    if (adapterClassForDataShow.getItemCount() == 0) {
                        noContact.setVisibility(View.VISIBLE);
                        errorImage.setVisibility(View.VISIBLE);
                    } else {
                        errorImage.setVisibility(View.GONE);
                        noContact.setVisibility(View.GONE);
                    }
                }
                else
                {
                    errorImage.setVisibility(View.GONE);
                    noContact.setVisibility(View.GONE);
                    adapterClassForDataShow.getFilter().filter(newText);

                }

                return true;
            }
        });

        //for custom view
        ImageView customButton = findViewById(R.id.custom_button);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PhoneContactView.this, customButton);
                popupMenu.getMenuInflater().inflate(R.menu.mymenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int value= menuItem.getItemId();
                        switch (value)
                        {
                            case R.id.select:
                            {
                                adapterClassForDataShow.setSelectEnable(true);
                                adapterClassForDataShow.notifyDataSetChanged();
                                break;
                            }
                            case R.id.selectAll:
                            {
                                adapterClassForDataShow.setSelectEnable(true);
                                for (DataModelClass modelClass : dataList) {
                                    modelClass.setSelected(true);
                                }
                                adapterClassForDataShow.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "All selected", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case R.id.Unselect:
                            {
                                adapterClassForDataShow.setSelectEnable(false);
                                adapterClassForDataShow.notifyDataSetChanged();
                                break;
                            }
                            case R.id.Delete:
                            {
                                adapterClassForDataShow.deleteSelectedItem();
                                break;
                            }
                            default:
                            {

                            }
                        }


                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

   /* public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        item= list.get(position);
        if (position==1)
        {
            adapterClassForDataShow.setSelectEnable(true);
            adapterClassForDataShow.notifyDataSetChanged();
        }
        else if(position==2)
        {
            adapterClassForDataShow.setSelectEnable(true);
            for (DataModelClass modelClass : dataList) {
                modelClass.setSelected(true);
            }
            adapterClassForDataShow.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "All selected", Toast.LENGTH_SHORT).show();

        }
        else if(position==3)
        {
            adapterClassForDataShow.setSelectEnable(false);
            adapterClassForDataShow.notifyDataSetChanged();
        }
        else if(position==4)
        {
            adapterClassForDataShow.deleteSelectedItem();}
        else
        {

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}