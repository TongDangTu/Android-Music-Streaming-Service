
package huce.fit.mvvmpattern.views.fragments.home.itemRandom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;

public class RandomAdapter extends RecyclerView.Adapter<RandomAdapter.RandomViewHolder>{

private List<Song> items;
private IClickSongOption iClickSongOption;
public void setItems(List<Song> list,IClickSongOption listener) {
    this.iClickSongOption = listener;
    this.items = list;
//    load và bind dữ liệu vào adapter
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public RandomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_random,parent,false);
        return new RandomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomViewHolder holder, int position) {
        Song item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(item.getImage()).centerCrop().into(holder.imageView);
        holder.tvTitle.setText(item.getTrackName());
        holder.itemRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickSongOption != null) {
                    iClickSongOption.onClickSong(item);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
    if (items != null){
        return items.size();
    }
        return 0;
    }

    public class RandomViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView tvTitle;
        private LinearLayout itemRandom;

    public RandomViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgRandom);
        tvTitle = itemView.findViewById(R.id.tvRandomTitle);
        itemRandom = itemView.findViewById(R.id.itemRandom);
        itemRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickSongOption != null) {
                    iClickSongOption.onClickSong(items.get(getAdapterPosition()));
                }
            }
        });

    }
}
}