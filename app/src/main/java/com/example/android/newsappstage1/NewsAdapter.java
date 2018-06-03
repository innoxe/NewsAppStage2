package com.example.android.newsappstage1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of the news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }


    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView with view ID headline
        TextView titleView = listItemView.findViewById(R.id.headline);
        // Display the healine of the current news in that TextView
        titleView.setText(currentNews.getHeadline());

        // Find the TextView with view ID lead
        TextView trailView = listItemView.findViewById(R.id.trailText);
        // Display the healine of the current news in that TextView
        trailView.setText(currentNews.getHeadline());

        // Find the TextView with view ID section_name
        TextView sectionNameView = listItemView.findViewById(R.id.section_name);
        // Display the section name of the current news in that TextView
        sectionNameView.setText(currentNews.getSectionName());


        // Find the TextView with view ID byline (News Author)
        TextView authorNameView = listItemView.findViewById(R.id.byline);
        // If the field byline has content display else not display the author name of the current news in that TextView
        if (currentNews.getByline() != "") {
            authorNameView.setText(currentNews.getByline());
            //Set byline view as visible
            authorNameView.setVisibility(View.VISIBLE);
        } else {
            //Set byline view as gone
            authorNameView.setVisibility(View.GONE);
        }

        // Find the ImageView with view ID image
        ImageView imageView = listItemView.findViewById(R.id.image);
        // Display the image for the current news in that ImageView
        if(currentNews.getImage() != "") {

            Bitmap bmp = getBitmapfromUrl(currentNews.getImage());

            imageView.setImageBitmap(bmp);
            //Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            //Otherwise hide the ImageView (set visibility to GONE)
            imageView.setVisibility(View.GONE);
        }


        // TextView date and time
        TextView dateView = null;
        TextView timeView = null;

        if (currentNews.getPublicationDate() != null) {

            // Find the date TextView
            dateView = listItemView.findViewById(R.id.date);

        }


        if (currentNews.getPublicationDate() != null) {
            dateView = listItemView.findViewById(R.id.date);
            // Format the date string (i.e. "Mar 3, 1984")
            String formattedDate = formatDate(currentNews.getPublicationDate()).concat(",");
            // Display the date of the current earthquake in that TextView
            dateView.setText(formattedDate);

            // Find the TextView with view ID time
            timeView = listItemView.findViewById(R.id.time);
            // Format the time string (i.e. "4:30PM")
            String formattedTime = formatTime(currentNews.getPublicationDate());
            // Display the time of the current earthquake in that TextView
            timeView.setText(formattedTime);

            //Set date & time views as visible
            dateView.setVisibility(View.VISIBLE);
            timeView.setVisibility(View.VISIBLE);
        } else {
            //Set date & time views as gone
            dateView.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

        /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }



    public Bitmap getBitmapfromUrl(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


}