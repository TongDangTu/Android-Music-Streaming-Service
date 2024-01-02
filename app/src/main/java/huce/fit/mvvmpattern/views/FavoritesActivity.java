package huce.fit.mvvmpattern.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.SongInfoService;
import huce.fit.mvvmpattern.api.SongPlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongInfo;
import huce.fit.mvvmpattern.model.SongPlaylist;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.adapter.FavoritesAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private ImageButton btnGoBack;

    private RecyclerView recyclerView;
    private FloatingActionButton fabPlay;
    private FavoritesAdapter adapter;
    private List<Song> favoriteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        btnGoBack = findViewById(R.id.btnBack);

        recyclerView = findViewById(R.id.rcvPlaylistItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FavoritesAdapter();
        adapter.setItems(getPlaylistSong(), new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = favoriteList;
            }
        });
        recyclerView.setAdapter(adapter);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }public void openMusicPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openSongBottomSheet(Song song) {
        View viewBottom = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_favorite, null);
        ImageView ivSong = viewBottom.findViewById(R.id.iv_song);
        TextView tvSongName = viewBottom.findViewById(R.id.textView6);
        TextView tvArtist = viewBottom.findViewById(R.id.textView5);
        Glide.with(FavoritesActivity.this).load(song.getImage()).centerCrop().into(ivSong);
        tvSongName.setText(song.getTrackName());
        tvArtist.setText(song.getArtistName());

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(viewBottom);
        dialog.setCanceledOnTouchOutside(true);

        ConstraintLayout btnAddToPlaylist = dialog.findViewById(R.id.bottom_sheet_options_Playlist);
        ConstraintLayout btnDelete = dialog.findViewById(R.id.bottom_sheet_options_Favorite);
        btnAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FavoritesActivity.this, "Add to playlist", Toast.LENGTH_SHORT).show();
                dialog.cancel();

                Intent intent = new Intent(FavoritesActivity.this, ChoosePlaylist.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id_song", song.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", Tmp.current_username);
                hashMap.put("id_song", song.getId());
                SongPlaylistService.songPlaylistService.deleteSongFromFavorite(hashMap)
                        .enqueue(new Callback<DataJson<SongPlaylist>>() {
                            @Override
                            public void onResponse(Call<DataJson<SongPlaylist>> call, Response<DataJson<SongPlaylist>> response) {
                                DataJson<SongPlaylist> dataJson = response.body();
                                if (dataJson != null) {
                                    if (dataJson.isStatus() == true) {
                                        getPlaylistSong();
                                        Toast.makeText(FavoritesActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(FavoritesActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {
                                Log.e("ERROR", this.getClass().getName()+"openSongBottomSheet()->onFailure: "+t.getMessage());
                            }
                        });
            }
        });
        dialog.show();
    }
    private List<Song> getPlaylistSong(){
        favoriteList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("username", Tmp.current_username);
        SongInfoService.songInfoService.getFavorite(hashMap)
                .enqueue(new Callback<DataJson<SongInfo>>() {
                    @Override
                    public void onResponse(Call<DataJson<SongInfo>> call, Response<DataJson<SongInfo>> response) {
                        DataJson<SongInfo> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus() == true) {
                                List<SongInfo> songInfoList = dataJson.getData();
                                for (int i = 0; i < songInfoList.size(); i++) {
                                    favoriteList.add(new Song(songInfoList.get(i).getId_song(), songInfoList.get(i).getLinkPicture(), songInfoList.get(i).getName_song(), songInfoList.get(i).getName_artist(), songInfoList.get(i).getLinkSong(), songInfoList.get(i).getName_category(), songInfoList.get(i).getPlayedTime()));
                                    updateFavoriteAdapter();
                                }
                            }
                            else {
                                Toast.makeText(FavoritesActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<SongInfo>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"getPlaylistSong()->onFailure: "+t.getMessage());
                    }
                });
        return favoriteList;
    }

    private void updateFavoriteAdapter() {
        adapter.setItems(favoriteList, new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = favoriteList;
            }
        });
    }
}
