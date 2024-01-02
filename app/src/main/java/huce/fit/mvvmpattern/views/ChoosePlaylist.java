package huce.fit.mvvmpattern.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.PlaylistService;
import huce.fit.mvvmpattern.api.SongPlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.SongPlaylist;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.adapter.ChoosePlaylistAdapter;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePlaylist extends AppCompatActivity {
    private List<Playlist> playlists;
    private RecyclerView rcvChoosePlaylist;
    private ChoosePlaylistAdapter choosePlaylistAdapter;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_playlist);

        btnBack= findViewById(R.id.btnBack);

        setRecyclerView();
        getPlaylist();
        processEvent();
    }

    private void processEvent() {
        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    public void setRecyclerView () {
        playlists = new ArrayList<>();
        playlists.add(new Playlist("", "", ""));
        rcvChoosePlaylist = findViewById(R.id.rcv_choose_playlist);
        choosePlaylistAdapter = new ChoosePlaylistAdapter(playlists, new ChoosePlaylistAdapter.IOnClickItemChoosePlaylist() {
            @Override
            public void onClickItemChoosePlaylist(int position) {
                addToPlaylist(position);
            }
        });
    }

    private void addToPlaylist(int position) {
        Bundle bundle = getIntent().getExtras();
        String id_song = bundle.getSerializable("id_song").toString();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_song", id_song);
        hashMap.put("id_playlist", playlists.get(position).getId());
        SongPlaylistService.songPlaylistService.addToPlaylist(hashMap)
                .enqueue(new Callback<DataJson<SongPlaylist>>() {
                    @Override
                    public void onResponse(Call<DataJson<SongPlaylist>> call, Response<DataJson<SongPlaylist>> response) {
                        DataJson<SongPlaylist> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus() == true) {
                                Toast.makeText(ChoosePlaylist.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ChoosePlaylist.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+ t.getMessage());
                    }
                });
    }

    private void getPlaylist() {
        PlaylistService.playlistservice.getPlayListList(Tmp.current_username)
                .enqueue(new Callback<DataJson<Playlist>>() {
                    @Override
                    public void onResponse(Call<DataJson<Playlist>> call, Response<DataJson<Playlist>> response) {
                        DataJson<Playlist> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus() == true) {
                                for (int i = 0; i < dataJson.getData().size(); i++) {
                                    playlists.add(new Playlist(dataJson.getData().get(i).getImageUrl(), dataJson.getData().get(i).getTitle(), dataJson.getData().get(i).getId()));
                                }
                                choosePlaylistAdapter.setItems(playlists);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<Playlist>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"getListSection()->onFailure: "+t.getMessage());
                        Toast.makeText(ChoosePlaylist.this, R.string.not_connection, Toast.LENGTH_SHORT).show();
                    }
                });
        rcvChoosePlaylist.setAdapter(choosePlaylistAdapter);
        rcvChoosePlaylist.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
    }
}