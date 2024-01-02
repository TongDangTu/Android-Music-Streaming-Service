package huce.fit.mvvmpattern.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.PlaylistService;
import huce.fit.mvvmpattern.api.SongInfoService;
import huce.fit.mvvmpattern.api.SongPlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongInfo;
import huce.fit.mvvmpattern.model.SongPlaylist;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.adapter.ChoosePlaylistAdapter;
import huce.fit.mvvmpattern.views.adapter.PlaylistAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {
    private ImageButton btnGoBack;
    private FloatingActionButton fabAddSongs;

    private RecyclerView recyclerView;
    private FloatingActionButton fabPlay;
    private PlaylistAdapter adapter;
    private List<Song> list;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Playlist playlist = (Playlist) bundle.get("playlist");
        fabAddSongs = findViewById(R.id.fabAddSongs);
        TextView textView = findViewById(R.id.tvPlaylist);
        textView.setText(playlist.getTitle());
        constraintLayout = findViewById(R.id.clPlaylistInfo);
//        constraintLayout
        Glide.with(this).load(playlist.getImageUrl()).into(new CustomTarget<Drawable>() {

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    constraintLayout.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        btnGoBack = findViewById(R.id.btnBack);

        fabAddSongs.setOnClickListener(view -> {
            openMusicPlayer();
            MainActivity.song = null;
            MainActivity.songList = list;
        });

        recyclerView = findViewById(R.id.rcvPlaylistSongs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PlaylistAdapter();
        adapter.setItems(getPlaylistSong(), new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = list;
            }
        });
        recyclerView.setAdapter(adapter);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
    public void openMusicPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openSongBottomSheet(Song song) {
        View viewBottom = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_song_playlist, null);
        ImageView ivSong = viewBottom.findViewById(R.id.iv_song);
        TextView tvSongName = viewBottom.findViewById(R.id.textView6);
        TextView tvArtist = viewBottom.findViewById(R.id.textView5);
        Glide.with(PlaylistActivity.this).load(song.getImage()).centerCrop().into(ivSong);
        tvSongName.setText(song.getTrackName());
        tvArtist.setText(song.getArtistName());

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(viewBottom);
        dialog.setCanceledOnTouchOutside(true);

        ConstraintLayout btnFavorite = dialog.findViewById(R.id.bottom_sheet_options_Favorite);
        ConstraintLayout btnAddToPlaylist = dialog.findViewById(R.id.bottom_sheet_options_Playlist);
        ConstraintLayout btnDelete = dialog.findViewById(R.id.bottom_sheet_options_Remove);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", Tmp.current_username);
                hashMap.put("id_song", song.getId());
                SongPlaylistService.songPlaylistService.addToFavorite(hashMap)
                        .enqueue(new Callback<DataJson<SongPlaylist>>() {
                            @Override
                            public void onResponse(Call<DataJson<SongPlaylist>> call, Response<DataJson<SongPlaylist>> response) {
                                DataJson<SongPlaylist> dataJson = response.body();
                                if (dataJson != null) {
                                    if (dataJson.isStatus() == true) {
                                        Toast.makeText(PlaylistActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(PlaylistActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {
                                Log.e("ERROR", this.getClass().getName()+": openSongBottomSheet() -> onFailure(): "+ t.getMessage());
                            }
                        });
            }
        });
        btnAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlaylistActivity.this, "Add to playlist", Toast.LENGTH_SHORT).show();
                dialog.cancel();

                Intent intent = new Intent(PlaylistActivity.this, ChoosePlaylist.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id_song", song.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Playlist playlist = (Playlist) bundle.getSerializable("playlist");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_song", song.getId());
                hashMap.put("id_playlist", playlist.getId());
                SongPlaylistService.songPlaylistService.deleteSongFromPlaylist(hashMap)
                        .enqueue(new Callback<DataJson<SongPlaylist>>() {
                            @Override
                            public void onResponse(Call<DataJson<SongPlaylist>> call, Response<DataJson<SongPlaylist>> response) {
                                DataJson<SongPlaylist> dataJson = response.body();
                                if (dataJson != null) {
                                    if (dataJson.isStatus() == true) {
                                        getPlaylistSong();
                                        Toast.makeText(PlaylistActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(PlaylistActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {

                            }
                        });
            }
        });
        dialog.show();
    }

    private List<Song> getPlaylistSong(){
        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        Playlist playlist = (Playlist) bundle.getSerializable("playlist");
        SongInfoService.songInfoService.getListSongByPlayListID(playlist.getId())
                .enqueue(new Callback<DataJson<SongInfo>>() {
                    @Override
                    public void onResponse(Call<DataJson<SongInfo>> call, Response<DataJson<SongInfo>> response) {
                        DataJson<SongInfo> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus()) {
                                List<SongInfo> songInfoList = dataJson.getData();
                                for (int i = 0; i < songInfoList.size(); i++) {
                                    list.add(new Song(songInfoList.get(i).getId_song(), songInfoList.get(i).getLinkPicture(), songInfoList.get(i).getName_song(), songInfoList.get(i).getName_artist(), songInfoList.get(i).getLinkSong(), songInfoList.get(i).getName_category(), songInfoList.get(i).getPlayedTime()));
                                }
                            }
                        }

                        updateAdapter();
                    }

                    @Override
                    public void onFailure(Call<DataJson<SongInfo>> call, Throwable t) {

                    }
                });
        return list;
    }

    private void updateAdapter() {
        adapter.setItems(list, new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = list;
            }
        });
    }
}