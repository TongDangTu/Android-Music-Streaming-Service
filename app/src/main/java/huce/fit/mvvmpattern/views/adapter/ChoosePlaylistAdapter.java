package huce.fit.mvvmpattern.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;

public class ChoosePlaylistAdapter extends RecyclerView.Adapter<ChoosePlaylistAdapter.ChoosePlaylistViewHolder> {

    private List<Playlist> items;
    private IOnClickItemChoosePlaylist iOnClickItemChoosePlaylist;

    public ChoosePlaylistAdapter(List<Playlist> items, IOnClickItemChoosePlaylist iOnClickItemChoosePlaylist) {
        this.items = items;
        this.iOnClickItemChoosePlaylist = iOnClickItemChoosePlaylist;
    }

    public void setItems(List<Playlist> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChoosePlaylistAdapter.ChoosePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ChoosePlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoosePlaylistAdapter.ChoosePlaylistViewHolder holder, int position) {
        Playlist playlist = items.get(position);
        Glide.with(holder.imageView.getContext()).load(playlist.getImageUrl()).into(holder.imageView);
        holder.tvTitle.setText(playlist.getTitle());
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ChoosePlaylistViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvTitle;

        public ChoosePlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPlaylist);
            tvTitle = itemView.findViewById(R.id.tvPlaylistTitle);

            itemView.setOnClickListener(view -> {
                iOnClickItemChoosePlaylist.onClickItemChoosePlaylist(getAdapterPosition());
            });
        }
    }

    public interface IOnClickItemChoosePlaylist {
        void onClickItemChoosePlaylist (int position);
    }
}