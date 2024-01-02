package huce.fit.mvvmpattern.views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.SongPlaylistService;
import huce.fit.mvvmpattern.databinding.ActivityMainBinding;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongPlaylist;
import huce.fit.mvvmpattern.services.MediaService;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.viewmodels.HomeViewModel;
import huce.fit.mvvmpattern.views.adapter.ViewPageAdapter;
import huce.fit.mvvmpattern.views.fragments.library.LibraryFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private HomeViewModel homeViewModel;
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private TextView tvSongName;
    private TextView tvArtistName;
    private ImageButton ibFavorite;
    public ImageView playPause;
    public ImageView next;
    public MediaPlayer mediaPlayer;
    private ImageView ivSongImage;
    private BottomNavigationView bottomNav;
    private String id_song;
    private LinearLayout linearLayout;
    private BottomSheetDialog bottomSheetSongOption;
    public static Song song = new Song("", "", "", "", "", "", 0);
    public static List<Song> songList = new ArrayList<>();

    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setHomeViewModel(homeViewModel);

        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNavigation);

        setViewPager();
        tvSongName = findViewById(R.id.tvMiniSongTitle);
        tvArtistName = findViewById(R.id.tvMiniSongArtist);
        ivSongImage = findViewById(R.id.imgMiniImage);
        ibFavorite = findViewById(R.id.iconFavorite);
        playPause = findViewById(R.id.iconPlayPause);
        next = findViewById(R.id.iconNextTrack);

        View view = findViewById(R.id.mini_player);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMusicPlayerFromMiniPlayer();
            }
        });

        ibFavorite.setOnClickListener(view_ -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", Tmp.current_username);
            hashMap.put("id_song", id_song);
            SongPlaylistService.songPlaylistService.addToFavorite(hashMap)
                    .enqueue(new Callback<DataJson<SongPlaylist>>() {
                        @Override
                        public void onResponse(Call<DataJson<SongPlaylist>> call, Response<DataJson<SongPlaylist>> response) {
                            DataJson<SongPlaylist> dataJson = response.body();
                            if (dataJson != null) {
                                if (dataJson.isStatus() == true) {
                                    Toast.makeText(MainActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {
                            Log.e("ERROR", this.getClass().getName()+"openSongBottomSheet()->onFailure: "+t.getMessage());
                        }
                    });
        });

        playPause.setOnClickListener(view_ -> {
            MediaService.eventPlayPause();
        });

        next.setOnClickListener(view_ -> {
            MediaService.eventNext();
        });

        MediaService.getStatusPrepareMutableLiveData().observe(this, statusPrepare -> {
            if (statusPrepare) {
                view.setVisibility(View.VISIBLE);
                thawingButtonControl();
            }
            else {
                freezingButtonControl();
            }
        });
        MediaService.getStatusPlayingMutableLiveData().observe(this, statusPlaying -> {
            if (statusPlaying) {
                playPause.setImageResource(R.drawable.ic_pause);
                startAnimation();
            }
            else {
                playPause.setImageResource(R.drawable.ic_play);
                stopAnimation();
            }
        });
        MediaService.getIdSongMutableLiveData().observe(this, idSong -> {
            id_song = idSong;
        });
        MediaService.getTitleMutableLiveData().observe(this, songName -> {
            tvSongName.setText(songName);
        });
        MediaService.getArtistMutableLiveData().observe(this, artistName -> {
            tvArtistName.setText(artistName);
        });
        MediaService.getLinkPictureMutableLiveData().observe(this, linkPicture -> {
            Glide.with(ivSongImage.getContext()).load(linkPicture).into(ivSongImage);
        });

//        handle event when user select item on bottom navigation
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//               switch không nhận được R.id
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    viewPager.setCurrentItem(0);
                } else if (id == R.id.action_search) {
                    viewPager.setCurrentItem(1);
                } else if (id == R.id.action_library) {
                    isComingLibrary.setValue(true);
                    viewPager.setCurrentItem(2);
                } else if (id == R.id.action_profile) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

    public void openMusicPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openMusicPlayerFromMiniPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("isComingFromMiniPlayer", true);
        startActivity(intent);
    }
    public void openArtistFragment() {
        Intent intent = new Intent(this, ArtistActivity.class);
        startActivity(intent);
    }
    public void openSongBottomSheet(Song song) {
        View viewBottom = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_song, null);
        ImageView ivSong = viewBottom.findViewById(R.id.iv_song);
        TextView tvSongName = viewBottom.findViewById(R.id.textView6);
        TextView tvArtist = viewBottom.findViewById(R.id.textView5);
        Glide.with(MainActivity.this).load(song.getImage()).centerCrop().into(ivSong);
        tvSongName.setText(song.getTrackName());
        tvArtist.setText(song.getArtistName());

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(viewBottom);
        dialog.setCanceledOnTouchOutside(true);

        ConstraintLayout btnFavorite = dialog.findViewById(R.id.bottom_sheet_options_Favorite);
        ConstraintLayout btnAddToPlaylist = dialog.findViewById(R.id.bottom_sheet_options_Playlist);
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
                                        Toast.makeText(MainActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Add to playlist", Toast.LENGTH_SHORT).show();
                dialog.cancel();

                Intent intent = new Intent(MainActivity.this, ChoosePlaylist.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id_song", song.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void startAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ivSongImage.animate().rotationBy(360f).setDuration(10000).withEndAction(this).setInterpolator(new LinearInterpolator()).start();
            }
        };
        ivSongImage.animate().rotationBy(360f).setDuration(10000).withEndAction(runnable).setInterpolator(new LinearInterpolator()).start();
    }
    private void stopAnimation() {
        ivSongImage.animate().cancel();
    }
    private void setViewPager() {
        //        set adapter for viewpager
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setUserInputEnabled(false);
//        set active bottom navigation when user swipe viewpager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        bottomNav.getMenu().findItem(R.id.action_search).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.action_library).setChecked(true);
                        break;
                    case 3:
                        bottomNav.getMenu().findItem(R.id.action_profile).setChecked(true);
                        break;
                    default:
                        bottomNav.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                }
            }
        });
    }


    public void openCategoryFragment() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }

//  NEW CODE:
    private void freezingButtonControl () {
        playPause.setEnabled(false);
    }

    private void thawingButtonControl () {
        playPause.setEnabled(true);
    }

    private static MutableLiveData<Boolean> isComingLibrary = new MutableLiveData<>();
    public static MutableLiveData<Boolean> getIsComingLibrary () {
        return isComingLibrary;
    }
}
