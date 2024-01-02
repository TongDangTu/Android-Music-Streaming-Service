package huce.fit.mvvmpattern.views.fragments.library.itemFavorite;

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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private IClickSongOption iClickSongOption;

private List<Song> items;
public void setItems(List<Song> list, IClickSongOption listener){
    this.iClickSongOption = listener;
    this.items = list;
//    load và bind dữ liệu vào adapter
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Song item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.imageView.getContext()).load(item.getImage()).into(holder.imageView);
        holder.tvTitle.setText(item.getTrackName());
        holder.itemFavorite.setOnClickListener(new View.OnClickListener() {
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

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView tvTitle;
    private LinearLayout itemFavorite;
    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgFavorite);
        tvTitle = itemView.findViewById(R.id.tvFavoriteTitle);
        itemFavorite = itemView.findViewById(R.id.itemFavorite);
        itemFavorite.setOnClickListener(new View.OnClickListener() {
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