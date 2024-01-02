package huce.fit.mvvmpattern.views.fragments.library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.PlaylistService;
import huce.fit.mvvmpattern.api.SongInfoService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongInfo;
import huce.fit.mvvmpattern.services.MyService;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.MainActivity;
import huce.fit.mvvmpattern.views.appInterface.IClickItemPlaylist;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.home.section.Section;
import huce.fit.mvvmpattern.views.fragments.library.itemFavorite.Favorite;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryFragment extends Fragment {
    private RecyclerView recyclerViewLibrary;
    private LibraryAdapter section_adapter;

    private MainActivity mainActivity;
    private List<Song> favorite;
    private List<Playlist> playList;
    private List<Section> sections;
    private boolean flag = true;
    private FloatingActionButton btnAddPlaylist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        mainActivity = (MainActivity) getActivity();
        recyclerViewLibrary = view.findViewById(R.id.rcvLibrary);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        recyclerViewLibrary.setLayoutManager(linearLayoutManager);
        section_adapter = new LibraryAdapter(mainActivity);
        section_adapter.setSections(getListSection(), new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {

            }

            @Override
            public void onClickSong(Song song) {

                mainActivity.openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = favorite;
            }
        });
        recyclerViewLibrary.setAdapter(section_adapter);

        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist);
        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialog = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_add_playlist, null);
                TextInputEditText edtPlaylistName = viewDialog.findViewById(R.id.edtPlaylistName);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(mainActivity)
                        .setTitle("Add Playlist")
                        .setView(viewDialog)
                        .setPositiveButton("Add", (dialog, which) -> {

                            String playlistName = edtPlaylistName.getText().toString();
                            Playlist plz = new Playlist(playlistName);
                            plz.setUsername(Tmp.current_username);
                            plz.setImageUrl("https://nhomhungtu.000webhostapp.com/img/img_cd.png");
                            PlaylistService.playlistservice.PlaylistAdd(plz).enqueue(new Callback<DataJson<Playlist>>() {
                                @Override
                                public void onResponse(Call<DataJson<Playlist>> call, Response<DataJson<Playlist>> response) {
                                    DataJson<Playlist> djson = response.body();
                                    if(djson!=null){
                                        if(djson.isStatus()){
                                            if(flag) {
                                                Toast.makeText(mainActivity, "Thêm playlist thành công", Toast.LENGTH_SHORT).show();
                                                flag = false;
                                            }
                                            Log.e("updateplaylist","success");
                                            playList.add(new Playlist(plz.getImageUrl(),plz.getTitle(),plz.getId()));
                                            updatePlayListAdapter();
                                        }
                                        if(flag) {
                                            Toast.makeText(mainActivity, "Thêm playlist không thành công", Toast.LENGTH_SHORT).show();
                                            flag = false;
                                        }
                                    }
                                    if(flag) {
                                        Toast.makeText(mainActivity, "Null", Toast.LENGTH_SHORT).show();
                                        flag = false;
                                    }
                                }

                                @Override
                                public void onFailure(Call<DataJson<Playlist>> call, Throwable t) {
                                    Log.e("ERROR", this.getClass().getName()+"onClickLogin()->onFailure: "+t.getMessage());
                                    Toast.makeText(mainActivity, "Network Error!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(mainActivity, playlistName, Toast.LENGTH_SHORT).show();
                            flag = true;
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
        });
        btnAddPlaylist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        MainActivity.getIsComingLibrary().observe(mainActivity, isComingLibrary -> {
            getListSection();
        });
        return view;
    }
    public void holdStartService() {
        Intent serviceIntent = new Intent(mainActivity, MyService.class);

        mainActivity.startService(serviceIntent);
    }

    private List<Section> getListSection() {
        sections = new ArrayList<>();

        List<Song> history = new ArrayList<>();


        favorite = new ArrayList<>();
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
                                    favorite.add(new Song(songInfoList.get(i).getId_song(), songInfoList.get(i).getLinkPicture(), songInfoList.get(i).getName_song(), songInfoList.get(i).getName_artist(), songInfoList.get(i).getLinkSong(), songInfoList.get(i).getName_category(), songInfoList.get(i).getPlayedTime()));
                                    updateSectionAdapter();
                                }
                            }
                            else {
                                Toast.makeText(mainActivity, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<SongInfo>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"getListSection()->onFailure: "+t.getMessage());
                    }
                });

        playList = new ArrayList<>();

        PlaylistService.playlistservice.getPlayListList(Tmp.current_username)
            .enqueue(new Callback<DataJson<Playlist>>() {
                @Override
                public void onResponse(Call<DataJson<Playlist>> call, Response<DataJson<Playlist>> response) {
                    DataJson<Playlist> dataJson = response.body();
                    if (dataJson != null) {
                        if (dataJson.isStatus() == true) {
                            for (int i = 0; i < dataJson.getData().size(); i++) {
                                if (dataJson.getData().get(i).getImageUrl() == null) {
                                    dataJson.getData().get(i).setImageUrl("https://raw.githubusercontent.com/Tamashi-Dake/Online_Music_Player_Android/main/app/src/main/res/drawable/img_2.jpg");
                                } else {
                                    playList.add(new Playlist(dataJson.getData().get(i).getImageUrl(), dataJson.getData().get(i).getTitle(), dataJson.getData().get(i).getId()));
                                }
                            }
                            updateSectionAdapter();
                            updatePlayListAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DataJson<Playlist>> call, Throwable t) {
                    Log.e("ERROR", this.getClass().getName()+"getListSection()->onFailure: "+t.getMessage());
                    Toast.makeText(mainActivity, R.string.not_connection, Toast.LENGTH_SHORT).show();
                }
            });

        sections.add(new Section("", history));
        sections.add(new Section("Favorites", favorite));
        sections.add(new Section("Playlist", playList));

        return sections;
    }

    private void updateSectionAdapter() {
        section_adapter.setSections(sections, new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {

            }

            @Override
            public void onClickSong(Song song) {

                mainActivity.openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = favorite;
            }
        });
    }
    private void updatePlayListAdapter() {
        section_adapter.setItems(playList, new IClickItemPlaylist() {
            @Override
            public void onclickItemPlaylist(Playlist playlist) {

            }

            @Override
            public void onLongClickItemPlaylist(Playlist playlist) {

            }
        });
    }
}