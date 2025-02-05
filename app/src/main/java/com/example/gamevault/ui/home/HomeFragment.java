package com.example.gamevault.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamevault.R;
import com.example.gamevault.databinding.FragmentHomeBinding;
import com.example.gamevault.ui.home.newsfeed.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_news);
        recyclerView.setAdapter(new CustomAdapter(generateData()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    private List<String> generateData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(String.valueOf(i) + "th Element");
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}