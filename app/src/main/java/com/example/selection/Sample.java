package com.example.selection;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.widget.NumberPicker;

public class Sample extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //Get the widgets reference from XML layout
        final TextView tv = (TextView) findViewById(R.id.tv);
        NumberPicker np = (NumberPicker) findViewById(R.id.np);

        //Set TextView text color
        tv.setTextColor(Color.parseColor("#FF2C834F"));

        //Initializing a new string array with elements
        final String[] values= {"English","Hindi", "Suomi"};

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        np.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(values.length-1); //to array last value

        //Specify the NumberPicker data source as array elements
        np.setDisplayedValues(values);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                tv.setText("Selected value : " + values[newVal]);
            }
        });
    }
}