package com.example.seniorsync2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

// The main class for the activity, extending AppCompatActivity to handle the activity lifecycle
public class hData_lineChart extends AppCompatActivity {
    // Declaration of variables for two line charts
    LineChart lineChart, lineChart1;
    DBHelper myDB;
    SQLiteDatabase sqLiteDatabase;
    // Variables for managing the data sets and data visualization
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData lineData, lineData1;

    // The onCreate method is called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting the layout for the activity
        setContentView(R.layout.h_activity_data_line_chart);
        // Initializing the line charts from the layout
        lineChart = findViewById(R.id.lineChart);
        lineChart1 = findViewById(R.id.lineChart1);
        // Setup the database helper and writable database
        myDB = new DBHelper(this);
        sqLiteDatabase = myDB.getWritableDatabase();
        // Enabling interactions like dragging, scaling on the charts
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(2000);

        lineChart1.setDragEnabled(true);
        lineChart1.setScaleEnabled(true);
        lineChart1.setTouchEnabled(true);
        lineChart1.getDescription().setEnabled(false);
        lineChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart1.getAxisRight().setEnabled(false);
        lineChart1.animateX(2000);


        // Defining different data sets for various health metrics
        LineDataSet lineDataSet1 = new LineDataSet(getDataValues(),"Systolic Blood Pressure");
        LineDataSet lineDataSet2 = new LineDataSet(getDataValues1(),"Diastolic Blood Pressure");
        LineDataSet lineDataSet3 = new LineDataSet(getDataValues2(),"Heartbeat");
        LineDataSet lineDataSet4 = new LineDataSet(getDataValues3(),"Blood Sugar (before meals)");
        LineDataSet lineDataSet5 = new LineDataSet(getDataValues4(),"Blood Sugar (after meals)");

        lineDataSet1.setColor(Color.rgb(106,90,205));
        lineDataSet1.setValueTextSize(12f);
        lineDataSet1.setLineWidth(2);

        lineDataSet2.setColor(Color.rgb(156,102,31));
        lineDataSet2.setValueTextSize(12f);
        lineDataSet2.setLineWidth(2);

        lineDataSet3.setColor(Color.rgb(0,255,255));
        lineDataSet3.setValueTextSize(12f);
        lineDataSet3.setLineWidth(2);

        lineDataSet4.setColor(Color.rgb(0,201,87));
        lineDataSet4.setValueTextSize(12f);
        lineDataSet4.setLineWidth(2);

        lineDataSet5.setColor(Color.rgb(252,230,201));
        lineDataSet5.setValueTextSize(12f);
        lineDataSet5.setLineWidth(2);
        // Clearing previous data sets and setting new data on charts
        dataSets.clear();
        lineData = new LineData(lineDataSet1,lineDataSet2,lineDataSet3);
        lineData1 = new LineData(lineDataSet4, lineDataSet5);
        lineChart.clear();
        lineChart1.clear();
        lineChart.setData(lineData);
        lineChart1.setData(lineData1);
        lineChart.invalidate();
        lineChart1.invalidate();



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        // Configuring axis properties for both charts
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisMinimum(60f);
        //yAxis.setDrawLabels(false);
        LimitLine yLimitLine = new LimitLine(140f,"Critical value");
        yAxis.addLimitLine(yLimitLine);
        yLimitLine.setLineColor(Color.rgb(178,34,34));


        XAxis xAxis1 = lineChart1.getXAxis();
        xAxis1.setTextSize(11f);
        xAxis1.setGranularity(1f);

        YAxis yAxis1 = lineChart1.getAxisLeft();
        yAxis1.setAxisMaximum(180f);
        yAxis1.setAxisMinimum(40f);
        //yAxis.setDrawLabels(false);
        LimitLine yLimitLine1 = new LimitLine(140f,"Critical value");
        yAxis1.addLimitLine(yLimitLine1);
        yLimitLine.setLineColor(Color.rgb(178,34,34));

    }
    // Method to retrieve systolic blood pressure values from the database and prepare them for charting
    private ArrayList<Entry> getDataValues() {
        ArrayList<Entry> dataVales = new ArrayList<>();
        // Query the database for all entries in the healthdata table
        Cursor cursor = sqLiteDatabase.query("healthdata", null, null, null, null, null, null);
        // Loop through all the rows returned by the query
        for(int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToNext();  // Move to the next row
            // Create a new Entry for the chart where the X-axis is the entry index and the Y-axis is the blood pressure value
            dataVales.add(new Entry(i, cursor.getFloat(1)));
        }
        return dataVales;  // Return the list of Entries for the chart
    }
    // Method to retrieve diastolic blood pressure values from the database for charting
    private ArrayList<Entry> getDataValues1() {
        ArrayList<Entry> dataVales1 = new ArrayList<>();
        // Perform a database query similar to the first method.
        Cursor cursor = sqLiteDatabase.query("healthdata", null, null, null, null, null, null);
        // Extract data specific to diastolic blood pressure
        for(int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToNext();
            dataVales1.add(new Entry(i, cursor.getFloat(2))); // Get the second column float data
        }
        return dataVales1;
    }
    // Method to retrieve heartbeat values from the database for charting
    private ArrayList<Entry> getDataValues2() {
        ArrayList<Entry> dataVales2 = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("healthdata", null, null, null, null, null, null);
        // Iterate through each row to get heartbeat data
        for(int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToNext();
            dataVales2.add(new Entry(i, cursor.getFloat(3)));  // Third column for heartbeat

        }
        return dataVales2;
    }
    // Method to retrieve blood sugar levels before meals from the database for charting
    private ArrayList<Entry> getDataValues3() {
        ArrayList<Entry> dataVales3 = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("healthdata", null, null, null, null, null, null);
        // Extract data from each row corresponding to blood sugar levels before meals
        for(int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToNext();
            dataVales3.add(new Entry(i, cursor.getFloat(4))); // Fourth column for blood sugar before meals
        }
        return dataVales3;
    }
    // Method to retrieve blood sugar levels after meals from the database for charting
    private ArrayList<Entry> getDataValues4() {
        ArrayList<Entry> dataVales4 = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("healthdata", null, null, null, null, null, null);
        // Extract data from each row corresponding to blood sugar levels after meals
        for(int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToNext();
            dataVales4.add(new Entry(i, cursor.getFloat(5))); // Fifth column for blood sugar after meals
        }
        return dataVales4;
    }

}