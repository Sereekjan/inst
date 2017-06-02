package kz.ikar.almatyinstitutes.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kz.ikar.almatyinstitutes.AboutActivity;
import kz.ikar.almatyinstitutes.R;
import kz.ikar.almatyinstitutes.classes.Comment;
import kz.ikar.almatyinstitutes.classes.Institute;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class TabFragment2 extends Fragment {

    RecyclerView commentsRecyclerView;
    CommentsAdapter commentsAdapter;
    LinearLayoutManager linearLayoutManager;

    FloatingActionButton floatingBtn;
    TextView emptyView;

    private void addCommentToFirebase(String key, int in,String comStr,int ball) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Institutes").child(key);
        Calendar dt = Calendar.getInstance();
        Comment comment = new Comment(in,ball,comStr, dt.getTime());
        databaseReference.child("comments").child(String.valueOf(in)).setValue(comment);
    }

    private void getInstituteByAddress(final String address, final String comment, final int ball) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Institutes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Institute inst = ds.getValue(Institute.class);
                    if (inst.getAddress().equals(address)) {
                        List<Comment> comments = inst.getComments();
                        if (comments == null) {
                            addCommentToFirebase(ds.getKey(), 0,comment,ball);
                        } else
                            addCommentToFirebase(ds.getKey(), comments.size(),comment,ball);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInstituteComments(final String address) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Institutes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Institute inst = ds.getValue(Institute.class);
                    if (inst.getAddress().equals(address)) {
                        //List<Comment> comments=inst.getComments();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);

        commentsRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_comments);
        floatingBtn = (FloatingActionButton) v.findViewById(R.id.floationActBtn);
        emptyView = (TextView) v.findViewById(R.id.emptyView);

        final Institute institute = ((AboutActivity)getActivity()).institute;

        // TODO: Change data source
        if (institute.getComments() != null) {
            commentsAdapter = new CommentsAdapter(institute.getComments());
        } else {
            commentsAdapter = new CommentsAdapter(new ArrayList<Comment>());
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        commentsRecyclerView.setAdapter(commentsAdapter);

        if (commentsAdapter.getItemCount() == 0) {
            commentsRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            commentsRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                View viewInflated = LayoutInflater.from(getContext())
                        .inflate(R.layout.dialog_comment,
                                (ViewGroup)getView(),
                                false);

                int color = R.color.colorAccent;
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                input.getBackground().mutate().setColorFilter(
                        ContextCompat.getColor(getContext(), R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP
                );
                final MaterialRatingBar ratebar = (MaterialRatingBar)
                        viewInflated.findViewById(R.id.ratebar);

                Drawable stars = ratebar.getProgressDrawable();
                DrawableCompat.setTint(stars,
                        ContextCompat.getColor(getContext(), R.color.colorPrimary));

                ((Button) viewInflated.findViewById(R.id.button_cancel))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                ((Button) viewInflated.findViewById(R.id.button_send))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getInstituteByAddress(institute.getAddress(),input.getText().toString(),ratebar.getProgress());
                                Snackbar.make(view, input.getText(), Snackbar.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                dialog.setView(viewInflated);
                dialog.show();
            }
        });

        return v;
    }


}
