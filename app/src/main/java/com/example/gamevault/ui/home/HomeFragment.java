package com.example.gamevault.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamevault.databinding.FragmentHomeBinding;
import com.example.gamevault.ui.home.newsfeed.CustomAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Use ViewBinding properly
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView using binding
        recyclerView = binding.recyclerViewNews;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // async call to fetch the data before returning vieww
        fetchDataInBackground();

        // must return root in order to display view.
        return root;
    }

    private void fetchDataInBackground() { // welp have to do this becauase it was running on main thread.
        new Thread(() -> {
            // list of article components each article has elements we can use to populate rv
            List<Article> articles = generateData(); // Fetch api and parse data json only include articles elements

            new Handler(Looper.getMainLooper()).post(() -> {
                // Ensure recyclerView is properly set before setting the adapter
                if (recyclerView != null) {
                    adapter = new CustomAdapter(articles);
                    recyclerView.setAdapter(adapter);
                }
            });
        }).start();
    }

    private List<Article> generateData() {
        List<Article> articlesList = new ArrayList<>();
        // hard code api key for now. wont be public
        String url = "https://newsapi.org/v2/everything?q=Call Of Duty Black Ops 6&language=en&apiKey=3af1fefa16da450c9a9f1178ad35f498";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // filter out trash and only get articles from json
                JSONArray articles = jsonObject.getJSONArray("articles");

                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.optString("title", "No Title");
                    String description = article.optString("description", "No Description");
                    String imageUrl = article.optString("urlToImage", "");

                    articlesList.add(new Article(title, description, imageUrl));
                }
            } else {
                System.out.println("Failed to fetch data. Response code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articlesList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class Article { // use a model to define one single article the use the object properties to fill the card
        private final String title;
        private final String description;
        private final String imageUrl;


        public Article(String title, String description, String imageUrl) {
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;


        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }


    }
}