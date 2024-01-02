
package huce.fit.mvvmpattern.views.fragments.home.itemArtist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.views.appInterface.IClickArtist;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>{

private List<Artist> items;
private IClickArtist iClickArtist;
public void setItems(List<Artist> list, IClickArtist iClickArtist){
    this.items = list;
    this.iClickArtist = iClickArtist;
//    load và bind dữ liệu vào adapter
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(item.getLinkPicture()).centerCrop().into(holder.imageView);
        holder.tvArtist.setText(item.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickArtist.onClickArtist(item);
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

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView tvArtist;
        private LinearLayout layout;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgArtist);
            tvArtist = itemView.findViewById(R.id.tvArtistTitle);

            layout = itemView.findViewById(R.id.Artist);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iClickArtist != null) {
                        iClickArtist.onClickArtist(items.get(getAdapterPosition()));
                    }
                }
            });
    }
}
}