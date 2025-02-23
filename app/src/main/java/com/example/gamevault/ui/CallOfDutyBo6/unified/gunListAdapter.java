package com.example.gamevault.ui.CallOfDutyBo6.unified;

import android.content.Intent;
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
import com.example.gamevault.ui.CallOfDutyBo6.unified.weaponsList.mainGunCard;
import android.content.Context;

import java.util.List;

public class gunListAdapter extends RecyclerView.Adapter<gunListAdapter.ViewHolder> {
    private List<mainGunCard> data;
    private Context context;
    private String weaponCategory;

    public gunListAdapter(Context context, List<mainGunCard> data, String weaponCategory) {
        this.context = context;
        this.data = data;
        this.weaponCategory = weaponCategory;
    }

    @Override
    public gunListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.gun_list_layout, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(gunListAdapter.ViewHolder holder, int position) {
        // Get mainGunCard at the current position
        mainGunCard gunCard = data.get(position);
        holder.textViewTitle.setText(gunCard.getMainGunName());

        // Load the image dynamically
        Glide.with(holder.imageView.getContext())
                .load(gunCard.getImageId())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            if (context != null) {
                Intent intent = new Intent(context, camoList.class);
                intent.putExtra("gun_name", gunCard.getMainGunName());
                intent.putExtra("gun_image", gunCard.getImageId());
                intent.putExtra("weapon_category", weaponCategory);
                context.startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "Context is null!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textViewTitle = view.findViewById(R.id.gun_name);
            this.imageView = view.findViewById(R.id.gunImage);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Tapped on challenge", Toast.LENGTH_SHORT).show();
        }
    }
}