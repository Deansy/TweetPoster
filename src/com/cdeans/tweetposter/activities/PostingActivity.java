package com.cdeans.tweetposter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
        final EditText postEntry = (EditText)findViewById(R.id.textBox);
        
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
    }
}
