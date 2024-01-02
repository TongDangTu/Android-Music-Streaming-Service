package huce.fit.mvvmpattern.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import huce.fit.mvvmpattern.api.SongInfoService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaService extends Service {
    private static MediaPlayer mediaPlayer;
    private static List<String> linkSongList = new ArrayList<>();
    private static List<Song> songList = new ArrayList<>();
    private static int position = 0;
    private static int playedTime = 0;
    public static boolean isUpdatingSeekBar = true;
    private static boolean autoStart = false;
    private static String statusRepeat = "2";
    private static String statusShuffle = "0";
    public static boolean isComing = false;
    public static boolean isNewList = false;
    private static Handler handler = new Handler();
    private static Runnable runnable;

    public MediaService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        addSong(linkSongAdapterList);
        if (mediaPlayer == null) {
            initMediaPlayer(position);
        }
        else {
            if (isNewList == true) {
                releaseResourceMediaPlayer();
                initMediaPlayer(position);
            }
        }
        updateTime();
        updateSeekBar();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResourceMediaPlayer();
    }

    private static String getStartTime () {
        int milliseconds = mediaPlayer.getCurrentPosition();
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        return timeFormat.format(milliseconds);
    }

    private static String getEndTime () {
        int milliseconds = mediaPlayer.getDuration();
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        return timeFormat.format(milliseconds);
    }

    private static void updateTime () {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTimeMutableLiveData.setValue(getStartTime());
                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    private static void updateSeekBar () {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isUpdatingSeekBar == true) {
                    startMillisecondsMutableLiveData.setValue(mediaPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    static void removeUpdateSeekBarHandle () {
        handler.removeCallbacks(runnable);
    }

    static void postUpdateSeekBarHandle () {
        handler.post(runnable);
    }

    public static void eventSeekTo (int milliseconds) {
        mediaPlayer.seekTo(milliseconds);
    }

    public static void releaseResourceMediaPlayer () {
        if (mediaPlayer != null) {
//            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public static void eventPlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            statusPlayingMutableLiveData.setValue(false);
        }
        else {
            mediaPlayer.start();
            statusPlayingMutableLiveData.setValue(true);
        }
    }

    public static void eventPrevious () {
        autoStart = true;
        isComing = false;
        position--;
        if (position < 0) {
            position = songList.size()-1;
        }
        releaseResourceMediaPlayer();
        initMediaPlayer(position);
        statusPlayingMutableLiveData.setValue(true);
    }

    public static void eventNext () {
        autoStart = true;
        isComing = false;

        if (statusShuffle.equals("1")) {
            position = new Random().nextInt(songList.size());
        }
        else {
            position++;
        }

        if (position > songList.size() - 1) {
            position = 0;
        }
        releaseResourceMediaPlayer();
        initMediaPlayer(position);
        statusPlayingMutableLiveData.setValue(true);
    }

    public static void eventRepeat () {
//        if (statusRepeat == null) {
//            statusRepeatMutableLiveData.setValue("2");
//        }

        // 0: off  |  1: one  |  2: all
        switch (statusRepeat) {
            case "0":
            case "1":
                statusRepeat = "2";
                statusShuffle = "0";
                statusRepeatMutableLiveData.setValue("2");
                statusShuffleMutableLiveData.setValue("0");
                break;
            case "":
            case "2":
                statusRepeat = "1";
                statusShuffle = "0";
                statusRepeatMutableLiveData.setValue("1");
                statusShuffleMutableLiveData.setValue("0");
                break;
        }
    }

    public static void eventShuffle () {
        switch (statusShuffle) {
            case "0":
                statusRepeat = "0";
                statusShuffle = "1";
                statusRepeatMutableLiveData.setValue("0");
                statusShuffleMutableLiveData.setValue("1");
                break;
            case "":
            case "1":
                statusRepeat = "2";
                statusShuffle = "0";
                statusRepeatMutableLiveData.setValue("2");
                statusShuffleMutableLiveData.setValue("0");
                break;
        }
    }

    static void nextWhenComplete () {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                autoStart = true;
                isComing = false;

                if (statusRepeat.equals("1")) {

                }
                else if (statusRepeat.equals("2")) {
                    position++;
                }
                else if (statusShuffle.equals("1")) {
                    position = new Random().nextInt(songList.size());
                }

                if (position > songList.size() - 1) {
                    position = 0;
                }
                releaseResourceMediaPlayer();
                initMediaPlayer(position);
            }
        });
    }

//    public static void addSong (List<String> list) {
//        if (linkSongAdapterList.equals(list)) {
//            isNewList = false;
//        }
//        else {
//            linkSongAdapterList = list;
//            position = 0;
//            isNewList = true;
//            statusPlayingMutableLiveData.setValue(false);
//        }
//    }

    // Sau khi hoàn thành Chức năng playlist có thể thêm tham số thứ 2 lưu id playlist, hoặc là không cần tham số thứ 2
    public static void addSongAdapter (List<Song> list) {
        int listSongAdapterSize = songList.size();
        int listSize = list.size();
        boolean isMatch = true; // kiểm tra xem 2 danh sách bài hát chuẩn bị mở với đang phát có giống không?
        // Kiểm tra bài hát đầu tiên của 2 danh sách. (Sau khi hoàn thành Chức năng playlist, có thể không cần tới lần IF này)
        if (listSongAdapterSize > 0) {
            if (songList.get(position).getId().equals(list.get(0).getId()) == false) {
                isMatch = false;
            }
        }

        if (listSongAdapterSize == listSize) {
            for (int i = 0; i < listSize; i++) {
                if (songList.get(i).getId().equals(list.get(i).getId()) == false) {
                    isMatch = false;
                }
            }
        }
        else {
            isMatch = false;
        }

        if (isMatch == true) {
            isNewList = false;
        }
        else {
            songList = list;
            position = 0;
            isNewList = true;
            statusPlayingMutableLiveData.setValue(false);
        }

//        if (linkSongAdapterList.equals(list)) {
//            isNewList = false;
//        }
//        else {
//            linkSongAdapterList = list;
//            position = 0;
//            isNewList = true;
//            statusPlayingMutableLiveData.setValue(false);
//        }
    }

//    private static void initMediaPlayer (int position) {
//        try {
//            statusPrepareMutableLiveData.setValue(false);
//            startTimeMutableLiveData.setValue("00:00");
//            endTimeMutableLiveData.setValue("00:00");
//            titleMutableLiveDate.setValue(getTitle());
//            mediaPlayer = new MediaPlayer ();
//            mediaPlayer.setDataSource(linkSongAdapterList.get(position));
//            mediaPlayer.prepareAsync();
//
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    statusPrepareMutableLiveData.setValue(true);
//                    endTimeMutableLiveData.setValue(getEndTime());
//                    endMillisecondsMutableLiveDate.setValue(mediaPlayer.getDuration());
//                    if(autoStart == true && isComing == false) {
//                        mediaPlayer.start();
//                    }
//                }
//            });
//
//            nextWhenComplete();
//        } catch (IOException e) {
//            Log.e("ERROR", MediaService.class.getName()+": initMediaPlayer()");
//            throw new RuntimeException(e);
//        }
//    }

    private static void initMediaPlayer (int position) {
        statusPrepareMutableLiveData.setValue(false);
        startTimeMutableLiveData.setValue("00:00");
        endTimeMutableLiveData.setValue("00:00");
        idSongMutableLiveData.setValue(songList.get(position).getId());
        titleMutableLiveData.setValue(songList.get(position).getTrackName());
        artistMutableLiveData.setValue(songList.get(position).getArtistName());
        categoryMutableLiveData.setValue(songList.get(position).getCategoryName());
        playedTimeMutableLiveData.setValue(songList.get(position).getPlayedTime());
        linkPictureMutableLiveData.setValue(songList.get(position).getImage());
        statusRepeatMutableLiveData.setValue(statusRepeat);
        statusShuffleMutableLiveData.setValue(statusShuffle);
        try {
            mediaPlayer = new MediaPlayer ();
            mediaPlayer.setDataSource(songList.get(position).getLinkSong());
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    increasePlayedTime();
                    statusPrepareMutableLiveData.setValue(true);
                    endTimeMutableLiveData.setValue(getEndTime());
                    endMillisecondsMutableLiveData.setValue(mediaPlayer.getDuration());
                    if(autoStart == true && isComing == false) {
                        mediaPlayer.start();
                    }
                }
            });
//            eventRepeat();
            nextWhenComplete();
        } catch (IOException e) {
            Log.e("ERROR", MediaService.class.getName()+": initMediaPlayer()");
            throw new RuntimeException(e);
        }
    }

    public static void increasePlayedTime () {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", songList.get(position).getId());
        SongInfoService.songInfoService.increasePlayedTime(hashMap)
                .enqueue(new Callback<DataJson<String>>() {
                    @Override
                    public void onResponse(Call<DataJson<String>> call, Response<DataJson<String>> response) {
                        DataJson<String> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus()) {
//                                playedTime = songList.get(position).getPlayedTime();
//                                playedTime++;
//                                playedTimeMutableLiveData.setValue(playedTime);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<String>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"increasePlayedTime()->onFailure: "+t.getMessage());
                    }
                });
    }


    //    MutableLiveData
    static private MutableLiveData<Boolean> statusPrepareMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<Boolean> getStatusPrepareMutableLiveData () {
        return statusPrepareMutableLiveData;
    }
    static private MutableLiveData<Boolean> statusPlayingMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<Boolean> getStatusPlayingMutableLiveData () {
        return statusPlayingMutableLiveData;
    }

    static private MutableLiveData<String> statusRepeatMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getStatusRepeatMutableLiveData () {
        return statusRepeatMutableLiveData;
    }

    static private MutableLiveData<String> statusShuffleMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getStatusShuffleMutableLiveData () {
        return statusShuffleMutableLiveData;
    }

    static private MutableLiveData<String> idSongMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getIdSongMutableLiveData () {
        return idSongMutableLiveData;
    }

    static private MutableLiveData<String> titleMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getTitleMutableLiveData () {
        return titleMutableLiveData;
    }

    static private MutableLiveData<String> artistMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getArtistMutableLiveData () {
        return artistMutableLiveData;
    }

    static private MutableLiveData<Integer> playedTimeMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<Integer> getPlayedTimeMutableLiveData () {
        return playedTimeMutableLiveData;
    }

    static private MutableLiveData<String> categoryMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getCategoryMutableLiveData () {
        return categoryMutableLiveData;
    }

    static private MutableLiveData<String> linkPictureMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getLinkPictureMutableLiveData () {
        return linkPictureMutableLiveData;
    }

    static private MutableLiveData<Integer> startMillisecondsMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<Integer> getStartMillisecondsMutableLiveData() {
        return startMillisecondsMutableLiveData;
    }

    static private MutableLiveData<Integer> endMillisecondsMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<Integer> getEndMillisecondsMutableLiveData() {
        return endMillisecondsMutableLiveData;
    }

    static private MutableLiveData<String> startTimeMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getStartTimeMutableLiveData () {
        return startTimeMutableLiveData;
    }

    static private MutableLiveData<String> endTimeMutableLiveData = new MutableLiveData<>();
    static public MutableLiveData<String> getEndTimeMutableLiveData () {
        return endTimeMutableLiveData;
    }
}