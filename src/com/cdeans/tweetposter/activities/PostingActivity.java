package com.cdeans.tweetposter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cdeans.tweetposter.OTweetApplication;
import com.cdeans.tweetposter.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostingActivity extends Activity {

    private OTweetApplication app;
    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (OTweetApplication)getApplication();
        twitter = app.getTwitter();
        setContentView(R.layout.posting);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!app.isAuthorized()) {
            beginAuthorization();
        } else {
            loadHomeTimeline();
            post();
        }
    }
    private void beginAuthorization() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }
    public void loadHomeTimeline() {
        try{
        Log.e("CAMSH", twitter.getHomeTimeline().get(0).getUser().getScreenName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void post() {
        Button postButton = (Button)findViewById(R.id.tweetBtn);
        Button closeButton = (Button)findViewById(R.id.closeBtn);
        final TextView charCounterText = (TextView)findViewById(R.id.charCountText);
        final EditText postEntry = (EditText)findViewById(R.id.textBox);

        final int charsRemaining = 140;

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    twitter.updateStatus(postEntry.getText().toString());
                    Toast confirmation = Toast.makeText(getApplicationContext(),"Posted", Toast.LENGTH_SHORT);
                    confirmation.show();
                    postEntry.setText("");
                }
                catch (TwitterException e)
                {
                    e.printStackTrace();
                    Toast failure = Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT);
                    failure.show();
                }
            }
        });
        
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        postEntry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                String text = postEntry.getText().toString();
                int remaining = 140 - text.length();
                if (text.length() < 140)
                {
                    Log.e("CAMSH", "Changing Text");
                    ((TextView) charCounterText).setText(Integer.toString(remaining));
                }


                // if enter is pressed start calculating
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {

                    // get EditText text
                    //String text = ((EditText) postEntry).getText().toString();

                    // find how many rows it cointains
                    int editTextRowCount = text.split("\\n").length;

                    // user has input more than limited - lets do something
                    // about that
                    if (editTextRowCount >= 4) {

                        // find the last break
                        int lastBreakIndex = text.lastIndexOf("\n");

                        // compose new text
                        String newText = text.substring(0, lastBreakIndex);

                        // add new text - delete old one and append new one
                        // (append because I want the cursor to be at the end)
                        ((EditText) view).setText("");
                        ((EditText) view).append(newText);

                    }
                }
                return false;
            }
        });
    }
}
