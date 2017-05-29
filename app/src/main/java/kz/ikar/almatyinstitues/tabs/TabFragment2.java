package kz.ikar.almatyinstitues.tabs;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import kz.ikar.almatyinstitues.R;
import kz.ikar.almatyinstitues.classes.Comment;

public class TabFragment2 extends Fragment {

    RecyclerView commentsRecyclerView;
    CommentsAdapter commentsAdapter;
    LinearLayoutManager linearLayoutManager;

    FloatingActionButton floatingBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);

        commentsRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_comments);
        floatingBtn = (FloatingActionButton) v.findViewById(R.id.floationActBtn);

        commentsAdapter = new CommentsAdapter(Comment.getFakeComments());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        commentsRecyclerView.setAdapter(commentsAdapter);

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Wow", Snackbar.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
