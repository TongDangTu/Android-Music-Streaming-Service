package huce.fit.mvvmpattern.views.fragments.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.PlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.FavoritesActivity;
import huce.fit.mvvmpattern.views.HistoryActivity;
import huce.fit.mvvmpattern.views.PlaylistActivity;

import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.home.itemHistory.ItemAdapter;

import huce.fit.mvvmpattern.views.fragments.home.section.Section;
import huce.fit.mvvmpattern.views.fragments.library.itemFavorite.Favorite;
import huce.fit.mvvmpattern.views.fragments.library.itemFavorite.FavoriteAdapter;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.PlaylistAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickItemPlaylist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HISTORY = 0;
    private static final int TYPE_FAVORITE = 1;
    private static final int TYPE_PLAYLIST = 2;

    private Context context;
    private List<Section> sections;
    private IClickSongOption iClickSongOption;
    private IClickItemPlaylist iClickItemPlaylist;
    private List<Playlist> items;

    public LibraryAdapter(Context context) {
        this.context = context;
    }

    public void setSections(List<Section> sections, IClickSongOption listener) {
        if (sections == null) {
            this.sections = new ArrayList<>();
        } else {
            this.sections = sections;
        }
        this.iClickSongOption = listener;
        notifyDataSetChanged();
    }
    public void setItems(List<Playlist> list, IClickItemPlaylist listener){
        this.items = list;
        this.iClickItemPlaylist = listener;
//    load và bind dữ liệu vào adapter
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HISTORY:
                View viewHistory = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new HistoryViewHolder(viewHistory);
            case TYPE_FAVORITE:
                View viewFavorite = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new FavoriteViewHolder(viewFavorite);
            case TYPE_PLAYLIST:
                View viewPlaylist = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new PlaylistViewHolder(viewPlaylist);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Section section = sections.get(position);
        if (section == null) {
            return;
        }
        switch (holder.getItemViewType()) {
            case TYPE_HISTORY:
                HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
                historyViewHolder.sectionName.setText(section.getSectionName());
                historyViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                ItemAdapter itemAdapter = new ItemAdapter();
                itemAdapter.setItems((List<Song>) section.getItems(), new IClickSongOption() {
                    @Override
                    public void onClickSongOption(Song song) {
                    }
                    @Override
                    public void onClickSong(Song song) {
                        if (iClickSongOption != null) {
                            iClickSongOption.onClickSong(song);
                        }
                    }
                });
                historyViewHolder.rcvItem.setAdapter(itemAdapter);
                break;
            case TYPE_FAVORITE:
                FavoriteViewHolder favoriteViewHolder = (FavoriteViewHolder) holder;
                favoriteViewHolder.sectionName.setText(section.getSectionName());
                favoriteViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter( );
                favoriteAdapter.setItems((List<Song>) section.getItems(), new IClickSongOption() {
                    @Override
                    public void onClickSongOption(Song song) {
                    }
                    @Override
                    public void onClickSong(Song song) {
                        if (iClickSongOption != null) {
                            iClickSongOption.onClickSong(song);
                        }
                    }
                });
                favoriteViewHolder.rcvItem.setAdapter(favoriteAdapter);
                break;
            case TYPE_PLAYLIST:
                PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
                playlistViewHolder.sectionName.setText(section.getSectionName());
                playlistViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                PlaylistAdapter playlistAdapter = new PlaylistAdapter((List<Playlist>) section.getItems(), new IClickItemPlaylist() {
                    @Override
                    public void onclickItemPlaylist(Playlist playlist) {
                        playlistViewHolder.onclickItemPlaylist(playlist);
                    }
                    @Override
                    public void onLongClickItemPlaylist(Playlist playlist) {
                        playlistViewHolder.onLongClickItemPlaylist(playlist);
                    }
                });
                playlistViewHolder.rcvItem.setAdapter(playlistAdapter);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 1:
                return TYPE_FAVORITE;
            case 2:
                return TYPE_PLAYLIST;
            default:
                return TYPE_HISTORY;
        }
    }

    @Override
    public int getItemCount() {
        if (sections != null) {
            return sections.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName;
        private RecyclerView rcvItem;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);

            sectionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HistoryActivity.class);
                    context.startActivity(intent);
                }
            });
        }

    }
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName;
        private RecyclerView rcvItem;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
            sectionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FavoritesActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName;
        private RecyclerView rcvItem;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
        }
        public void onclickItemPlaylist(Playlist playlist){
            Intent intent = new Intent(context, PlaylistActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("playlist", playlist);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

        public void onLongClickItemPlaylist(Playlist playlist) {
            View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_add_playlist, null);
            TextInputEditText edtPlaylistName = viewDialog.findViewById(R.id.edtPlaylistName);
            edtPlaylistName.setText(playlist.getTitle());
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                    .setTitle("Change name/Delete Playlist")
                    .setView(viewDialog)
                    .setPositiveButton("Edit", (dialog, which) -> {
                        String playlistName = edtPlaylistName.getText().toString().trim();
                        playlist.setTitle(playlistName);
                        if (!playlistName.isEmpty()) {
                            PlaylistService.playlistservice.PlaylistUpdate(playlist).enqueue(new Callback<DataJson<Playlist>>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onResponse(Call<DataJson<Playlist>> call, Response<DataJson<Playlist>> response) {
                                    DataJson<Playlist> djson = response.body();
                                    if(djson!=null){
                                        if(djson.isStatus()){
                                            Toast.makeText(context, "Playlist's name updated successfully!", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                        notifyDataSetChanged();
                                    }
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<DataJson<Playlist>> call, Throwable t) {
                                    Log.e("ERROR", this.getClass().getName()+"getListSection()->onFailure: "+t.getMessage());
                                    Toast.makeText(context, R.string.not_connection, Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        else {
                            Toast.makeText(context, "Empty playlist's name!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNeutralButton("Delete", (dialog, which) -> {
                        String playlistName = edtPlaylistName.getText().toString().trim();
                        playlist.setTitle(playlistName);
                        Log.e("ID",playlist.getId().toString());
                        if (!playlistName.isEmpty()) {
                            PlaylistService.playlistservice.PlaylistDel(playlist).enqueue(new Callback<DataJson<Playlist>>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onResponse(Call<DataJson<Playlist>> call, Response<DataJson<Playlist>> response) {
                                    Log.e("status","Success");
                                    DataJson<Playlist> djson = response.body();
                                    if(djson!=null){
                                        if(djson.isStatus()){
                                            Toast.makeText(context, "This playlist completely deleted!", Toast.LENGTH_SHORT).show();
                                            items.remove(playlist);
                                            notifyDataSetChanged();
                                        }
                                        notifyDataSetChanged();
                                    }
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<DataJson<Playlist>> call, Throwable t) {
                                    Log.e("ERROR", this.getClass().getName()+"getListSection()->onFailure: "+t.getMessage());
                                    Toast.makeText(context, R.string.not_connection, Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        else {
                            Toast.makeText(context, "Empty playlist's name!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();
            alertDialog.show();

        }
    }
}