package kz.ikar.almatyinstitutes.tabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kz.ikar.almatyinstitutes.R;
import kz.ikar.almatyinstitutes.classes.Comment;

public class TabFragment2 extends Fragment {

    RecyclerView commentsRecyclerView;
    CommentsAdapter commentsAdapter;
    LinearLayoutManager linearLayoutManager;

    FloatingActionButton floatingBtn;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
                final View view = v;
                //Snackbar.make(v, "Wow", Snackbar.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //builder.setTitle("Комментарий");

                final View viewInflated = LayoutInflater.from(getContext())
                        .inflate(R.layout.dialog_comment,
                                (ViewGroup)getView(),
                                false);

                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Snackbar.make(view, input.getText(), Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Snackbar.make(view, "Canceled", Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        return v;
    }
}
