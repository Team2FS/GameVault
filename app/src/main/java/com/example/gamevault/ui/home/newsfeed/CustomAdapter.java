package com.example.gamevault.ui.home.newsfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gamevault.ui.home.HomeFragment.Article;
import com.example.gamevault.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Article> data;
    public CustomAdapter (List<Article> data){
        this.data = data;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_newscard, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        Article article = data.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewDescription.setText(article.getDescription());

        // Load image using Glide
        Glide.with(holder.imageView.getContext())
                .load(article.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textViewTitle = view.findViewById(R.id.article_title);
            this.textViewDescription = view.findViewById(R.id.article_description);
            this.imageView = view.findViewById(R.id.article_image);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "tapped on link", Toast.LENGTH_SHORT).show();
        }
    }
}