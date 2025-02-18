package com.example.gamevault.ui.home.favoritechallenges;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gamevault.R;
import com.example.gamevault.ui.home.HomeFragment.ChallengeCard;
import java.util.List;

public class CustomAdapterFavorites extends RecyclerView.Adapter<CustomAdapterFavorites.ViewHolder> {
    private List<ChallengeCard> data;

    public CustomAdapterFavorites(List<ChallengeCard> data){
        this.data = data;
    }

    @Override
    public CustomAdapterFavorites.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_challenge_layout, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(CustomAdapterFavorites.ViewHolder holder, int position) {
        ChallengeCard challengeCard = data.get(position);
        holder.imageView.setImageResource(R.mipmap.gun_placeholder);

        holder.textViewTitle.setText(challengeCard.getCamoName());
        holder.textViewDescription.setText(challengeCard.getCamoDescription());
        holder.progressText.setText(challengeCard.getCurrentNumAchieved() + "/" + challengeCard.getNumRequired() + " Completed");


        holder.progressBar.setMax(challengeCard.getNumRequired());
        holder.progressBar.setProgress(challengeCard.getCurrentNumAchieved());

        Glide.with(holder.imageView.getContext())
                .load(challengeCard.getImageName())
                .into(holder.imageView);


        if (challengeCard.isFavourite()) {
            holder.favoriteIcon.setColorFilter(0xFFFF0000); // Red for favorite
        } else {
            holder.favoriteIcon.setColorFilter(0xFFFFFFFF); // White for non-favorite
        }

        holder.subtractButton.setOnClickListener(v -> {
            int newProgress = Math.max(0, challengeCard.getCurrentNumAchieved() - 1);
            challengeCard.setCurrentNumAchieved(newProgress);
            holder.progressText.setText(newProgress + "/" + challengeCard.getNumRequired() + " Completed");
            holder.progressBar.setProgress(newProgress);
        });

        holder.addButton.setOnClickListener(v -> {
            int newProgress = Math.min(challengeCard.getNumRequired(), challengeCard.getCurrentNumAchieved() + 1);
            challengeCard.setCurrentNumAchieved(newProgress);
            holder.progressText.setText(newProgress + "/" + challengeCard.getNumRequired() + " Completed");
            holder.progressBar.setProgress(newProgress);
        });

        // Handle favorite icon toggle
        holder.favoriteIcon.setOnClickListener(v -> {
            challengeCard.setFavourite(!challengeCard.isFavourite());
            if (challengeCard.isFavourite()) {
                holder.favoriteIcon.setColorFilter(0xFFFF0000);
            } else {
                holder.favoriteIcon.setColorFilter(0xFFFFFFFF);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageView imageView;
        private ImageView subtractButton;
        private ImageView addButton;
        private ProgressBar progressBar;
        private ImageView favoriteIcon;
        private TextView progressText;
        private ImageView checkMarkIcon;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textViewTitle = view.findViewById(R.id.title);
            this.textViewDescription = view.findViewById(R.id.description);
            this.imageView = view.findViewById(R.id.weaponImage);
            this.subtractButton = view.findViewById(R.id.subtract);
            this.addButton = view.findViewById(R.id.add);
            this.progressBar = view.findViewById(R.id.progressBar);
            this.favoriteIcon = view.findViewById(R.id.favorite);
            this.progressText = view.findViewById(R.id.progressText);
            this.checkMarkIcon = view.findViewById(R.id.imageView6);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Tapped on challenge", Toast.LENGTH_SHORT).show();
        }
    }
}