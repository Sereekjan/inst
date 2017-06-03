package kz.ikar.almatyinstitutes.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import kz.ikar.almatyinstitutes.AboutActivity;
import kz.ikar.almatyinstitutes.R;
import kz.ikar.almatyinstitutes.classes.Institute;

public class TabFragment1 extends Fragment {
    TextView textViewTitle;
    TextView textViewIsGov;
    TextView textViewHead;
    TextView textViewAddress;
    TextView textViewContacts;
    TextView textViewRating;

    public void updateRating(final Institute institute){
        //final String str=institute.getName();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Institutes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Institute inst = ds.getValue(Institute.class);
                    if (inst.getName().toString().equals(institute.getName())) {
                        if (inst.getComments()!=null) {
                            institute.setComments(inst.getComments());
                            textViewRating.setText(String.valueOf(inst.getAvgRating()));
                        }
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        textViewTitle = (TextView) view.findViewById(R.id.textview_name);
        textViewIsGov = (TextView) view.findViewById(R.id.textview_isgov);
        textViewHead = (TextView) view.findViewById(R.id.textview_head);
        textViewAddress = (TextView) view.findViewById(R.id.textview_address);
        textViewContacts = (TextView) view.findViewById(R.id.textview_contact_info);
        textViewRating = (TextView) view.findViewById(R.id.textview_rating);

        Institute institute = ((AboutActivity)getActivity()).institute;
        textViewTitle.setText(institute.getName());
        if (institute.isGov()) {
            textViewIsGov.setText("Государственное учреждение");
        } else {
            textViewIsGov.setText("Частное учреждение");
        }
        textViewHead.setText(institute.getHead());
        textViewAddress.setText(institute.getAddress());
        textViewContacts.setText(institute.getPhone());
        if (institute.getAvgRating() < 0f) {
            textViewRating.setText("-");
        } else {
            textViewRating.setText(String.valueOf(institute.getAvgRating()));
        }
        updateRating(institute);
        return view;
    }
}
