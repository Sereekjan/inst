package kz.ikar.almatyinstitues.tabs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kz.ikar.almatyinstitues.R;
import kz.ikar.almatyinstitues.classes.Comment;

/**
 * Created by User on 26.05.2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private List<Comment> mItemList;

    public CommentsAdapter(List<Comment> mItemList) {
        this.mItemList = mItemList;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_comment, parent, false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, final int position) {
        Comment result = mItemList.get(position);
        holder.mContentTextView.setText(result.getContent());
        holder.mTimeTextView.setText(result.getTimeText());
        holder.mRatingTextView.setText("Рейтинг:  " + result.getBall());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {

        private TextView mContentTextView;
        private TextView mTimeTextView;
        private TextView mRatingTextView;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            mContentTextView = (TextView) itemView.findViewById(R.id.textview_text);
            mTimeTextView = (TextView) itemView.findViewById(R.id.textview_time);
            mRatingTextView = (TextView) itemView.findViewById(R.id.textview_rating);
        }
    }
}
