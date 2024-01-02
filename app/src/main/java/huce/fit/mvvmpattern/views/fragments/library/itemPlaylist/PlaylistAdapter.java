package huce.fit.mvvmpattern.views.fragments.library.itemPlaylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.views.appInterface.IClickItemPlaylist;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>{
private IClickItemPlaylist iClickItemPlaylist;
private List<Playlist> items;
public PlaylistAdapter(List<Playlist> list,IClickItemPlaylist iClickItemPlaylist){
    this.iClickItemPlaylist = iClickItemPlaylist;
    this.items = list;
}
public void setItems(List<Playlist> list ){
    this.items = list;
//    load và bind dữ liệu vào adapter
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.imageView.getContext()).load(item.getImageUrl()).into(holder.imageView);
        holder.tvTitle.setText(item.getTitle());
        holder.layoutItemPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemPlaylist.onclickItemPlaylist(item);
            }
        });
        holder.layoutItemPlaylist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iClickItemPlaylist.onLongClickItemPlaylist(item);
                return true;
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

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout layoutItemPlaylist;
    private ImageView imageView;
    private TextView tvTitle;
    public PlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        layoutItemPlaylist = itemView.findViewById(R.id.layoutPlaylist);
        imageView = itemView.findViewById(R.id.imgPlaylist);
        tvTitle = itemView.findViewById(R.id.tvPlaylistTitle);

    }
}
}