package com.back4app.parseobjectsimplecrud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadObjects extends AppCompatActivity {

    public ArrayList<String> dataList = new ArrayList<String>();
    public String[] myArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_objects);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadObjects.this, CreateObject.class);
                startActivity(intent);
            }
        });

        findObjects();
    }

    private void findObjects(){
        myArray = new String[]{};
        final ListView listView = (ListView) findViewById(R.id.listviewA);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("reminderList");
        // Query Parameters
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.orderByAscending("itemName");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, final ParseException e) {
                if (e == null){
                    // Adding objects into the Array
                    for(int i= 0 ; i < objects.size(); i++){
                        String element = objects.get(i).getString("itemName");
                        dataList.add(element.toString());
                    }
                } else {

                }
                myArray = dataList.toArray(new String[dataList.size()]);

                final ArrayList<String> list  = new ArrayList<String>(Arrays.asList(myArray));

                ArrayAdapter<String> adapterList
                        = new ArrayAdapter<String>(ReadObjects.this, android.R.layout.simple_list_item_single_choice, myArray);

                listView.setAdapter(adapterList);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapter, View v, final int position,
                                            long id) {

                        final String value = (String) adapter.getItemAtPosition(position);

                        //Alert showing the options related with the object (Update or Delete)
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReadObjects.this)
                                .setTitle(value + " movie" )
                                .setMessage("What do you want to do?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataList.remove(position);
                                        deleteObject(value);
                                        myArray = dataList.toArray(new String[dataList.size()]);

                                        ArrayAdapter<String> adapterList
                                                = new ArrayAdapter<String>(ReadObjects.this, android.R.layout.simple_list_item_single_choice, myArray);

                                        listView.setAdapter(adapterList);
                                    }
                                })
                                .setNeutralButton("Update",  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ReadObjects.this, UpdateObject.class);
                                        intent.putExtra("objectName", value.toString());
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog ok = builder.create();
                        ok.show();
                    }
                });
            }
        });
    }

    // Delete object
    private void deleteObject(final String value) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("reminderList");

        // Query parameters based on the item name
        query.whereEqualTo("itemName", value.toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> object, ParseException e) {
                if (e == null) {
                    object.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                            } else {

                            }
                        }
                    });
                } else {

                }
            };
        });
    }
}