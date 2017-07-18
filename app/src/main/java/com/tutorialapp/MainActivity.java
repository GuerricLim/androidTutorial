package com.tutorialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.content.Intent;

import com.example.guerriclim.tutorialapp.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.content.ContentValues;
import android.content.CursorLoader;

import android.database.Cursor;

import android.view.Menu;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button decreaseFont, increaseFont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button increasing/decreasing font sizes
        decreaseFont = (Button)findViewById(R.id.buttonDecreaseFontSize);
        increaseFont = (Button)findViewById(R.id.buttonIncreaseFontSize);

        decreaseFont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);
                float sizeVar = txtView.getTextSize() - 10;
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizeVar);
            }
        });

        increaseFont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);
                float sizeVar = txtView.getTextSize() + 10;
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeVar);
            }
        });

        //Spinner Example
        Spinner fontSizeSpinner = (Spinner) findViewById(R.id.spinnerFontSize);

        // Spinner click listener
        fontSizeSpinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Please select font size.");
        categories.add("Small");
        categories.add("Medium");
        categories.add("Large");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            public boolean areAllItemsEnabled() {
                return false;
            }

        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fontSizeSpinner.setAdapter(dataAdapter);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        TextView txtView = (TextView) findViewById(R.id.sampleTextFontSize);

        if (item == "Small")
        {
            txtView.setTextSize(15);
        }
        else if (item == "Medium")
        {
            txtView.setTextSize(30);
        }
        else if (item == "Large")
        {
            txtView.setTextSize(50);
        }
        // Showing selected spinner item
        /*Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void broadcastIntent(View view){
        Intent intent = new Intent();
        intent.setAction("com.tutorialapp.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(StudentsProvider.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(StudentsProvider.GRADE,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                StudentsProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickDeleteAll(View view) {
        String URL = "content://com.tutorialapp.StudentsProvider";

        Uri students = Uri.parse(URL);

        getContentResolver().delete(StudentsProvider.CONTENT_URI, null, null);

        Toast.makeText(this, "Deleted All Students", Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.tutorialapp.StudentsProvider";

        Uri students = Uri.parse(URL);
        Cursor c = getContentResolver().query(students, null, null, null, "name");
        if (c.getCount() != 0)
        {
            if (c.moveToFirst()) {
                do{
                    Toast.makeText(this,
                            c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                    ", " +  c.getString(c.getColumnIndex( StudentsProvider.NAME)) +
                                    ", " + c.getString(c.getColumnIndex( StudentsProvider.GRADE)),
                            Toast.LENGTH_SHORT).show();
                } while (c.moveToNext());
            }
        }
        else
        {
            Toast.makeText(this, "No Content Yet", Toast.LENGTH_LONG).show();
        }

    }
}
