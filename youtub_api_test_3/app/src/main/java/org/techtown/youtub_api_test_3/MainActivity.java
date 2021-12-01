package org.techtown.youtub_api_test_3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;


import java.io.IOException;
import java.util.List;

public class MainActivity extends YouTubeBaseActivity {

    String API_KEY = "AIzaSyBePgpf4EUGMQtFXaxxanBEHAIhX07_E2E";
    String result = "없음";
    YouTubePlayerView youtubeView;
    Button button;
    YouTubePlayer.OnInitializedListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        button = (Button) findViewById(R.id.youtubeBtn);
        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(result);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_SHORT).show();
            }
        };

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                YoutubeAsyncTask youtubeAsyncTask = new YoutubeAsyncTask();
                youtubeAsyncTask.doInBackground();
                youtubeView.initialize(API_KEY,listener);

            }
        });
    }

    public class YoutubeAsyncTask extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.youtube.YouTube mService = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                HttpTransport TRANSPORT = new NetHttpTransport();
                final JsonFactory JSON_FACTORY = new JacksonFactory();

                mService = new YouTube.Builder(TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }

                }).setApplicationName("youtube_api_test_3").build();


                SearchListResponse searchResponse = mService.search().list("id")
                        .setKey(API_KEY)
                        .setQ("fix you coldplay")
                        .setType("video")
                        .setMaxResults(Long.valueOf(1))
                        .execute();



                //SearchListResponse searchResponse = search.execute();



                List<SearchResult> searchResult = searchResponse.getItems();


                if (searchResult != null) {

                    result = searchResult.get(0).getId().getVideoId();
                    Toast.makeText(getApplicationContext(), "result: "+result, Toast.LENGTH_SHORT).show();
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
                System.err.println("There was a service error 2: " + e.getLocalizedMessage() + " , " + e.toString());
            } catch (IOException e) {
                System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {

                t.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public String JsonParsing(List<SearchResult> searchResults) {

            String returnResult;
            returnResult = searchResults.get(0).getId().getVideoId();
            return returnResult;
        }
    }
}