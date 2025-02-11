package com.example.gamevault.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamevault.R;
import com.example.gamevault.databinding.FragmentHomeBinding;
import com.example.gamevault.ui.home.newsfeed.CustomAdapter;
import com.example.gamevault.ui.settings.manageprofile.ManageProfileActivity;
import okhttp3.Call;
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
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Use ViewBinding for home
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // set up the buttons for quick navigation
        binding.btnManageProfile.setOnClickListener(v -> {
                    Log.d("Home", "Manage Profile Clicked");
                    Intent intent = new Intent(getActivity(), ManageProfileActivity.class);
                    startActivity(intent);
            });

        // Init RecyclerView using binding
        recyclerView = binding.recyclerViewNews;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView = binding.apiUpdates;




        // async call to fetch the data before returning vieww
        fetchDataInBackground();
        updateListView();


        // must return root in order to display view.
        return root;
    }

    private void fetchDataInBackground() { // well have to do this because it was running on main thread.
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

    private List<String> generateUpdates() {
        List<String> list = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String url = "https://6793e4675eae7e5c4d9037f6.mockapi.io/api/v1/test1";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.has("updates")) {
                        JSONArray updatesArray = jsonObject.getJSONArray("updates");

                        for (int j = 0; j < updatesArray.length(); j++) {
                            list.add(updatesArray.getString(j)); // Extract and add updates
                        }
                    }
                }
            } else {
                Log.e("API Error", "Failed to fetch data. Response code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private void updateListView() {
        new Thread(() -> {
            List<String> updates = generateUpdates(); // Fetch API data

            new Handler(Looper.getMainLooper()).post(() -> {
                if (listView != null && getContext() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            requireContext(), android.R.layout.simple_list_item_1, updates) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = view.findViewById(android.R.id.text1);
                            textView.setGravity(android.view.Gravity.CENTER);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
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