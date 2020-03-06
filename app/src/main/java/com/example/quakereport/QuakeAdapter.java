package com.example.quakereport;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuakeAdapter extends ArrayAdapter<Quake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public QuakeAdapter(Activity context, ArrayList<Quake> quake){

        super(context, 0, quake);
    }

    private String formatDate(Date dateObj){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd,yyyy");
        return dateFormat.format(dateObj);
    }

    private String formatTime(Date timeObj){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(timeObj);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch(magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list, parent, false);
        }
        Quake currentPosition = getItem(position);




        TextView mag = (TextView) listItemView.findViewById(R.id.mag);
        String formattedMagnitude = formatMagnitude(currentPosition.getMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        mag.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentPosition.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);





        String primaryLocation;
        String locationOffset;
        String originalLocation = currentPosition.getLocation();

        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String loc_separated[] = originalLocation.split(LOCATION_SEPARATOR);
            primaryLocation = loc_separated[0]+LOCATION_SEPARATOR;
            locationOffset = loc_separated[1];
        }else{
            primaryLocation = getContext().getString(R.string.near_the);
            locationOffset = originalLocation;
        }

        TextView locOffSet = (TextView) listItemView.findViewById(R.id.location_offset);
        locOffSet.setText(locationOffset);


        TextView primaryLoc = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLoc.setText(primaryLocation);


        Date dateObject = new Date(currentPosition.getDate());

        String formatedDate = formatDate(dateObject);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(formatedDate);

        TextView time = (TextView) listItemView.findViewById(R.id.time);
        String formatedTime = formatTime(dateObject);
        time.setText(formatedTime);




        return listItemView;
    }
}
