package huce.fit.mvvmpattern.views.fragments.home.itemHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private IClickSongOption iClickSongOption;

private List<Song> items;
public void setItems(List<Song> list,   IClickSongOption iClickSongOption){
    this.iClickSongOption = iClickSongOption;
    this.items = list;
//    load và bind dữ liệu vào adapter
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Song item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(item.getImage()).centerCrop().into(holder.imageView);
        holder.tvTitle.setText(item.getTrackName());
        holder.itemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickSongOption != null) {
                    iClickSongOption.onClickSong(item);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
    if (items != null){
        return items.size();
    }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView tvTitle;
    private LinearLayout itemHistory;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgHistory);
        tvTitle = itemView.findViewById(R.id.tvHistoryTitle);
        itemHistory = itemView.findViewById(R.id.itemHistory);
        itemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickSongOption != null) {
                    iClickSongOption.onClickSong(items.get(getAdapterPosition()));
                }
            }
        });
    }
}
}