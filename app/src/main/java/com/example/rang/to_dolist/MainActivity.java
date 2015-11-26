package com.example.rang.to_dolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(arrayAdapterToDo);

        registerForContextMenu(listViewToDo);

        try {
            Log.i("ON CREATE", "Hi, the on create has occured.");

            Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

            while(scanner.hasNextLine()) {
                String toDo = scanner.nextLine();
                arrayAdapterToDo.add(toDo);
            }

            scanner.close();
        } catch(Exception e) {
            Log.i("ON CREATE", e.getMessage());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listViewToDo) {
            return;
        }

        menu.setHeaderTitle("What would you like to do?");

        String[] options = { "Delete Task", "Return"};

        for(String option: options) {
            menu.add(option);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;

        if(item.getTitle().equals("Delete Task")){
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onBackPressed(){
        try {
            Log.i("ON BACK PRESSED", "Hi, the back on event has occured.");

            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));

            for(String toDo : arrayListToDo){
                pw.println(toDo);
            }

            pw.close();
        } catch(Exception e){
            Log.i("ON BACK PRESSED", e.getMessage());
        }

    }

    public void buttonAddClick(View v){
        EditText editTextToDo = (EditText) findViewById(R.id.editTextToDo);
        String toDo = editTextToDo.getText().toString().trim();

        if(toDo.isEmpty()){
            return;
        }


        arrayAdapterToDo.add(toDo);
        editTextToDo.setText("");
    }
}
