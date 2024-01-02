package huce.fit.mvvmpattern.views.fragments.nowPlaying;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.SongPlaylistService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongPlaylist;
import huce.fit.mvvmpattern.services.MediaService;
import huce.fit.mvvmpattern.utils.Tmp;
import huce.fit.mvvmpattern.views.MainActivity;
import huce.fit.mvvmpattern.views.MusicPlayerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MediaPlayerFragment extends Fragment {

    public static final String TAG = "MediaPlayerFragment";

    private TextView tvSongName;
    private TextView tvArtistName;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private TextView tvHintTime;
    private ImageView ivDisc;
    private ImageButton ibBack;
    private ImageButton ibPlay;
    private ImageButton ibPrevious;
    private ImageButton ibNext;
    private ImageButton ibRepeat;
    private ImageButton ibShuffle;
    private ImageButton ibFavorite;
    private SeekBar sbTimeLine;
    private ProgressBar pbLoading;
    private List<String> linkSongList;
    private List<Song> songList;
    private String id_song;

    private MusicPlayerActivity musicPlayerActivity;
    private Intent intent;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);
        musicPlayerActivity = (MusicPlayerActivity) getActivity();

        MediaService.isComing = true;

        if (musicPlayerActivity.getIntent().getBooleanExtra("isComingFromMiniPlayer", false) == true) {
            init(view);
        }
        else {
            intent = new Intent(musicPlayerActivity, MediaService.class);
            musicPlayerActivity.startService(intent);

            init(view);
            addSongAdapter();
        }
        initMediaPlayer();
        processEvent();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ivDisc.animate().rotationBy(360f).setDuration(10000).withEndAction(this).setInterpolator(new LinearInterpolator()).start();
            }
        };
        ivDisc.animate().rotationBy(360f).setDuration(10000).withEndAction(runnable).setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimation() {
        ivDisc.animate().cancel();
    }

//    NEW CODE:

    private void eventIbBack () {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void updateSbTimeLine() {
        MediaService.getStartMillisecondsMutableLiveData().observe(musicPlayerActivity, milliseconds -> {
            sbTimeLine.setProgress(milliseconds);
        });
    }

    private void freezingButtonControl () {
        ibPlay.setEnabled(false);
        sbTimeLine.setEnabled(false);
    }

    private void thawingButtonControl () {
        ibPlay.setEnabled(true);
        sbTimeLine.setEnabled(true);
    }

    private void eventBtnBack () {
        ibBack.setOnClickListener(view -> {
            musicPlayerActivity.finish();
        });
    }

    private void eventIbPlay () {
        ibPlay.setOnClickListener(view -> {
            MediaService.eventPlayPause();
        });
    }

    private void eventIbPrevious () {
        ibPrevious.setOnClickListener(view -> {
            MediaService.eventPrevious();
            ibPlay.setImageResource(R.drawable.ic_pause_white);
        });
    }

    private void eventIbNext () {
        ibNext.setOnClickListener(view -> {
            MediaService.eventNext();
            ibPlay.setImageResource(R.drawable.ic_pause_white);
        });
    }

    private void eventIbRepeat () {
        ibRepeat.setOnClickListener(view -> {
            MediaService.eventRepeat();
        });
    }

    private void eventIbShuffle () {
        ibShuffle.setOnClickListener(view -> {
            MediaService.eventShuffle();
        });
    }

    private  void eventIbFavorite () {
        ibFavorite.setOnClickListener(view -> {
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
                                    Toast.makeText(getContext(), dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataJson<SongPlaylist>> call, Throwable t) {
                            Log.e("ERROR", this.getClass().getName()+"openSongBottomSheet()->onFailure: "+t.getMessage());
                        }
                    });
        });
    }

    private void eventSbTimeLine () {
        sbTimeLine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int milliseconds = seekBar.getProgress();
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                tvHintTime.setText(timeFormat.format(milliseconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvHintTime.setVisibility(View.VISIBLE);
//                MediaService.removeUpdateSeekBarHandle();
                MediaService.isUpdatingSeekBar = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvHintTime.setVisibility(View.INVISIBLE);
                int milliseconds = seekBar.getProgress();
                MediaService.eventSeekTo(milliseconds);
//                MediaService.postUpdateSeekBarHandle();
                MediaService.isUpdatingSeekBar = true;
            }
        });
    }

    private void init (View view) {
        tvSongName = view.findViewById(R.id.tv_song_name);
        tvArtistName = view.findViewById(R.id.tv_artist_name);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        tvHintTime = view.findViewById(R.id.tv_hint_time);
        sbTimeLine = view.findViewById(R.id.sb_timeline);
        ibPlay = view.findViewById(R.id.ib_play);
        ibPrevious = view.findViewById(R.id.ib_previous);
        ibNext = view.findViewById(R.id.ib_next);
        ibRepeat = view.findViewById(R.id.ib_repeat);
        ibShuffle = view.findViewById(R.id.ib_shuffle);
        ibFavorite = view.findViewById(R.id.ib_favorite);
        ivDisc = view.findViewById(R.id.iv_disc);
        pbLoading = view.findViewById(R.id.pb_loading);
        ibBack = view.findViewById(R.id.ib_back);
    }

    private void addSongAdapter () {
        songList = new ArrayList<>();
        if (MainActivity.song != null && MainActivity.songList != null) {
            for (int i = 0; i < MainActivity.songList.size(); i++) {
                if (MainActivity.song.getId().equals(MainActivity.songList.get(i).getId())) {
                    Collections.rotate(MainActivity.songList, -i);
                    songList.addAll(MainActivity.songList);
                }
            }
        }
        else if (MainActivity.song != null) {
            songList.add(MainActivity.song);
        }
        else if (MainActivity.songList != null) {
            songList.addAll(MainActivity.songList);
        }
        MediaService.addSongAdapter(songList);
    }

    private void initMediaPlayer () {
        try {
            MediaService.getStatusPrepareMutableLiveData().observe(musicPlayerActivity, statusPrepare -> {
                if (statusPrepare == true) {
                    pbLoading.setVisibility(View.INVISIBLE);
                    thawingButtonControl();
                }
                else {
                    pbLoading.setVisibility(View.VISIBLE);
                    freezingButtonControl();
                }
            });
            MediaService.getStatusPlayingMutableLiveData().observe(musicPlayerActivity, statusPlaying -> {
                if (statusPlaying) {
                    ibPlay.setImageResource(R.drawable.ic_pause_white);
                    startAnimation();
                }
                else {
                    ibPlay.setImageResource(R.drawable.ic_play_white);
                    stopAnimation();
                }
            });
            MediaService.getStatusRepeatMutableLiveData().observe(musicPlayerActivity, statusRepeat -> {
                switch (statusRepeat) {
                    case "0":
                        ibRepeat.setImageResource(R.drawable.ic_repeat_white);
                        break;
                    case "1":
                        ibRepeat.setImageResource(R.drawable.ic_repeat_one_red);
                        break;
                    case "2":
                        ibRepeat.setImageResource(R.drawable.ic_repeat_red);
                        break;
                }
            });
            MediaService.getStatusShuffleMutableLiveData().observe(musicPlayerActivity, statusShuffle -> {
                switch (statusShuffle) {
                    case "0":
                        ibShuffle.setImageResource(R.drawable.ic_shuffle_white);
                        break;
                    case "1":
                        ibShuffle.setImageResource(R.drawable.ic_shuffle_red);
                        break;
                }
            });
            MediaService.getIdSongMutableLiveData().observe(musicPlayerActivity, idSong -> {
                id_song = idSong;
            });
            MediaService.getTitleMutableLiveData().observe(musicPlayerActivity, songName -> {
                tvSongName.setText(songName);
            });
            MediaService.getArtistMutableLiveData().observe(musicPlayerActivity, artistName -> {
                tvArtistName.setText(artistName);
            });
            MediaService.getLinkPictureMutableLiveData().observe(musicPlayerActivity, linkPicture -> {
                Glide.with(ivDisc.getContext()).load(linkPicture).into(ivDisc);
            });
            MediaService.getStartTimeMutableLiveData().observe(musicPlayerActivity, startTime -> {
                tvCurrentTime.setText(startTime);
            });
            MediaService.getEndTimeMutableLiveData().observe(musicPlayerActivity, endTime -> {
                tvTotalTime.setText(endTime);
            });
            MediaService.getEndMillisecondsMutableLiveData().observe(musicPlayerActivity, milliseconds -> {
                sbTimeLine.setMax(milliseconds);
            });
        } catch (Exception e) {
            Log.e("ERROR", this.getClass().getName()+": initMediaPlayer()");
        }
    }

    private void processEvent () {
        eventIbBack();
        eventIbPlay();
        eventIbPrevious();
        eventIbNext();
        eventIbRepeat();
        eventIbShuffle();
        eventIbFavorite();
        updateSbTimeLine();
        eventSbTimeLine();
    }
}