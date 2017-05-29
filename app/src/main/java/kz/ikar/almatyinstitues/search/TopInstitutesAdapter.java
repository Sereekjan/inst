package kz.ikar.almatyinstitues.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

import kz.ikar.almatyinstitues.MainActivity;
import kz.ikar.almatyinstitues.R;
import kz.ikar.almatyinstitues.classes.Institute;

/**
 * Created by User on 24.05.2017.
 */

public class TopInstitutesAdapter extends RecyclerView.Adapter<TopInstitutesAdapter.TopIntitutesViewHolder> {
    private List<Institute> mItemList;
    private static MainActivity activity;

    public TopInstitutesAdapter(List<Institute> mItemList, MainActivity activity) {
        this.mItemList = mItemList;
        this.activity = activity;
    }

    @Override
    public TopIntitutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_item_search_result, parent, false);
        return new TopIntitutesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopIntitutesViewHolder holder, int position) {
        final int p = position;
        Institute result = mItemList.get(position);
        holder.mTitleTextView.setText(result.getName());
        holder.mDescriptionTextView.setText(result.getAddress());
        holder.mIconImageView.setImageResource(R.drawable.ic_school);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.pickLocation(mItemList.get(p));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class TopIntitutesViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mIconImageView;

        public TopIntitutesViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.textview_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.textview_description);
            mIconImageView = (ImageView) itemView.findViewById(R.id.imageview_icon);
        }
    }


    public void addAll(Collection<Institute> items) {
        int currentItemCount = mItemList.size();
        mItemList.addAll(items);
        notifyItemRangeInserted(currentItemCount, items.size());
    }

    public void addAll(int position, Collection<Institute> items) {
        int currentItemCount = mItemList.size();
        if (position > currentItemCount)
            throw new IndexOutOfBoundsException();
        else
            mItemList.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void replaceWith(Collection<Institute> items) {
        replaceWith(items, false);
    }

    public void clear() {
        int itemCount = mItemList.size();
        mItemList.clear();
        notifyItemRangeRemoved(0, itemCount);
    }


    public void replaceWith(Collection<Institute> items, boolean cleanToReplace) {
        if (cleanToReplace) {
            clear();
            addAll(items);
        } else {
            int oldCount = mItemList.size();
            int newCount = items.size();
            int delCount = oldCount - newCount;
            mItemList.clear();
            mItemList.addAll(items);
            if (delCount > 0) {
                notifyItemRangeChanged(0, newCount);
                notifyItemRangeRemoved(newCount, delCount);
            } else if (delCount < 0) {
                notifyItemRangeChanged(0, oldCount);
                notifyItemRangeInserted(oldCount, -delCount);
            } else {
                notifyItemRangeChanged(0, newCount);
            }
        }
    }
}