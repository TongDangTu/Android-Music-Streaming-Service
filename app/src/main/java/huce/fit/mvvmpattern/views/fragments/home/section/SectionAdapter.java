package huce.fit.mvvmpattern.views.fragments.home.section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.model.Category;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.appInterface.IClickArtist;
import huce.fit.mvvmpattern.views.appInterface.IClickCategory;
import huce.fit.mvvmpattern.views.fragments.home.itemArtist.ArtistAdapter;
import huce.fit.mvvmpattern.views.fragments.home.itemBigHit.BigHitAdapter;
import huce.fit.mvvmpattern.views.fragments.home.itemCategories.CategoriesAdapter;
import huce.fit.mvvmpattern.views.fragments.home.itemHistory.ItemAdapter;
import huce.fit.mvvmpattern.views.fragments.home.itemPopular.PopularAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.home.itemRandom.RandomAdapter;

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HISTORY = 0;
    private static final int TYPE_BIG_HIT = 1;
    private static final int TYPE_POPULAR = 2;
    private static final int TYPE_ARTIST = 3;
    private static final int TYPE_CATEGORIES = 4;
    private static final int TYPE_RANDOM = 5;


    private Context context;
    private List<Section> sections;
    private IClickSongOption iClickSongOption;
    private IClickArtist iClickArtist;
    private IClickCategory iClickCategory;
    public SectionAdapter(Context context) {
        this.context = context;
    }

    public void setSections(List<Section> sections, IClickSongOption iClickSongOption) {
        if (sections == null) {
            this.sections = new ArrayList<>();
        } else {
            this.sections = sections;
        }
        this.iClickSongOption = iClickSongOption;
        notifyDataSetChanged();
    }
    public void setSections(List<Section> sections, IClickArtist iClickArtist) {
        if (sections == null) {
            this.sections = new ArrayList<>();
        } else {
            this.sections = sections;
        }
        this.iClickArtist = iClickArtist;
        notifyDataSetChanged();
    }
    public void setSections(List<Section> sections, IClickCategory iClickCategory) {
        if (sections == null) {
            this.sections = new ArrayList<>();
        } else {
            this.sections = sections;
        }
        this.iClickCategory = iClickCategory;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HISTORY:
                View viewHistory = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new HistoryViewHolder(viewHistory);
            case TYPE_BIG_HIT:
                View viewBigHit = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new BigHitViewHolder(viewBigHit);
            case TYPE_POPULAR:
                View viewPopular = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new PopularViewHolder(viewPopular);
            case TYPE_ARTIST:
                View viewArtist = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new ArtistViewHolder(viewArtist);
            case TYPE_CATEGORIES:
                View viewCategories = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new CategoriesViewHolder(viewCategories);
            case TYPE_RANDOM:
                View viewRandom = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section, parent, false);
                return new RandomViewHolder(viewRandom);
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
        switch (holder.getItemViewType()){
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
            case TYPE_BIG_HIT:
                BigHitViewHolder bigHitViewHolder = (BigHitViewHolder) holder;
                bigHitViewHolder.sectionName.setText(section.getSectionName());
                bigHitViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                BigHitAdapter bigHitAdapter = new BigHitAdapter(context,section.getItems());
                bigHitAdapter.setItems((List<Song>) section.getItems());
                bigHitViewHolder.rcvItem.setAdapter(bigHitAdapter);
                break;
            case TYPE_POPULAR:
                PopularViewHolder popularViewHolder = (PopularViewHolder) holder;
                popularViewHolder.sectionName.setText(section.getSectionName());
                popularViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                PopularAdapter popularAdapter = new PopularAdapter();
                popularAdapter.setItems((List<Song>) section.getItems(), new IClickSongOption() {
                    @Override
                    public void onClickSongOption(Song song) {
                        if (iClickSongOption != null) {
                            iClickSongOption.onClickSongOption(song);
                        }
                    }
                    @Override
                    public void onClickSong(Song song) {
                        if (iClickSongOption != null) {
                            iClickSongOption.onClickSong(song);
                        }
                    }
                });
                popularViewHolder.rcvItem.setAdapter(popularAdapter);
                break;
            case TYPE_ARTIST:
                ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
                artistViewHolder.sectionName.setText(section.getSectionName());
                artistViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                ArtistAdapter artistAdapter = new ArtistAdapter();
                artistAdapter.setItems((List<Artist>) section.getItems(), new IClickArtist() {
                    @Override
                    public void onClickArtist(Artist artist) {
                        if (iClickArtist != null) {
                            iClickArtist.onClickArtist(artist);
                        }
                    }
                });
                artistViewHolder.rcvItem.setAdapter(artistAdapter);
                break;
            case TYPE_CATEGORIES:
                CategoriesViewHolder categoriesViewHolder = (CategoriesViewHolder) holder;
                categoriesViewHolder.sectionName.setText(section.getSectionName());
                categoriesViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
                categoriesAdapter.setItems((List<Category>) section.getItems(), new IClickCategory() {
                    @Override
                    public void onClickCategory(Category category) {
                        if (iClickCategory != null) {
                            iClickCategory.onClickCategory(category);
                        }
                    }
                }   );
                categoriesViewHolder.rcvItem.setAdapter(categoriesAdapter);
                break;
            case TYPE_RANDOM:
                RandomViewHolder randomViewHolder = (RandomViewHolder) holder;
                randomViewHolder.sectionName.setText(section.getSectionName());
                randomViewHolder.rcvItem.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                RandomAdapter randomAdapter = new RandomAdapter();
                randomAdapter.setItems((List<Song>) section.getItems(), new IClickSongOption() {
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
                randomViewHolder.rcvItem.setAdapter(randomAdapter);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 1:
                return TYPE_BIG_HIT;
            case 2:
                return TYPE_POPULAR;
            case 3:
                return TYPE_ARTIST;
            case 4:
                return TYPE_CATEGORIES;
            case 5:
                return TYPE_RANDOM;
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
        }
    }
    public class BigHitViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionName;
        private RecyclerView rcvItem;
        public BigHitViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
        }

    }
    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView rcvItem;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);

        }
    }
    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView rcvItem;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
        }
    }
    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView rcvItem;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
        }
    }
    public class RandomViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView rcvItem;
        public RandomViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSectionName);
            rcvItem = itemView.findViewById(R.id.rcvSectionItem);
        }
    }

}
