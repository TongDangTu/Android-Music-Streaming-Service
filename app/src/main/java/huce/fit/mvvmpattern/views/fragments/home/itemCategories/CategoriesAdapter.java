
package huce.fit.mvvmpattern.views.fragments.home.itemCategories;

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
import huce.fit.mvvmpattern.model.Category;
import huce.fit.mvvmpattern.views.appInterface.IClickCategory;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<Category> items;
    private IClickCategory listener;

    public void setItems(List<Category> list, IClickCategory listener) {
        this.items = list;
        this.listener = listener;
//    load và bind dữ liệu vào adapter
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category item = items.get(position);
        if (item == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(item.getLinkPicture()).centerCrop().into(holder.imageView);
        holder.tvTitle.setText(item.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickCategory(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvTitle;
        private LinearLayout layout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCategory);
            tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
            layout = itemView.findViewById(R.id.Category);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickCategory(items.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}