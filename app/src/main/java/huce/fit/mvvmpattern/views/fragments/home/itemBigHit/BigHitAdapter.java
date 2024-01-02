package huce.fit.mvvmpattern.views.fragments.home.itemBigHit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Song;

public class BigHitAdapter extends RecyclerView.Adapter<BigHitAdapter.BigHitViewHolder> {
    private Context context;

    private List<Song> items;

    public BigHitAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Song> list) {
        this.items = list;
//    load và bind dữ liệu vào adapter
        notifyDataSetChanged();
    }

    public BigHitAdapter(Context context, List<Song> list) {
        this.context = context;
        this.items = list;
    }

    @NonNull
    @Override
    public BigHitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_hit, parent, false);
        return new BigHitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BigHitViewHolder holder, int position) {
//    khai báo url để load ảnh dựa vào vị trí của item
//        String url = String.valueOf(items.get(position % items.size()));
//        load ảnh vào imageView qua Glide
//        Glide.with(context).load(url).centerCrop().placeholder(R.drawable.ic_launcher_background).into(holder.imageView);

        Song item = items.get(position % items.size());
        if (item == null) {
            return;
        }
        Glide.with(holder.imageView.getContext()).load(item.getImage()).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size() * 100;
        }
        return 0;
    }

    public class BigHitViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public BigHitViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgBigHit);
        }
    }
}