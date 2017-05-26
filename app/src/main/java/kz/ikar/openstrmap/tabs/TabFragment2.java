package kz.ikar.openstrmap.tabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kz.ikar.openstrmap.R;
import kz.ikar.openstrmap.classes.Comment;

public class TabFragment2 extends Fragment {

    RecyclerView commentsRecyclerView;
    CommentsAdapter commentsAdapter;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);

        commentsRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_comments);
        commentsAdapter = new CommentsAdapter(Comment.getFakeComments());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        commentsRecyclerView.setAdapter(commentsAdapter);

        return v;
    }
}
