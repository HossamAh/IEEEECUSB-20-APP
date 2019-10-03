package com.example.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;


import com.example.firebase.R;
import com.example.firebase.RecyclerViewAdapterAnnouncements;
import com.example.firebase.models.Announcement;
import com.example.firebase.models.Event;
import com.example.firebase.models.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment  implements RecyclerViewAdapterAnnouncements.OnNotListener {
    ArrayList<Announcement> announcementArrayList;
    Announcement announcement ;
    RecyclerView recyclerView;
    private int index;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseReference databaseReference;
    private PageViewModel pageViewModel;
    DatabaseReference something;
    static int mam;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        Log.d("lmao", "newInstance: "+index);
        mam = index;
        return fragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            //You can do inside this method

        }
    }

    @Override
    public void onDestroy() {
        Log.d("AnnouncementFragment ", "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("AnnouncementFragment ", "onDetach: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("AnnouncementFragment ", "onDestroyView: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
         index = 1;
        announcementArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        switch (index){
            case 1:
                something = databaseReference.child("IEEECUSB").child("Events");

                break;
            case 2:
                something = databaseReference.child("Committees").child(UserInfo.sCommitteeName).child("Tasks");

                break;
            case 3 :
                something = databaseReference.child("Committees").child(UserInfo.sCommitteeName).child("Events");
                break;
        }
        something.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("AnnouncementFragment",dataSnapshot.getValue().toString());
                if(dataSnapshot.exists()) {
                    announcement = new Announcement();
                    Log.e("AnnouncementFragment","index is "+ index);
                    switch (index) {
                        case 1:
                        case 3:
                            Event event = dataSnapshot.getValue(Event.class);
                            Log.e("AnnouncementFragment","Event is "+event.getDetails());

                            announcement.setDetails(event.getDetails());
                            announcement.setImageURL(event.getImageUrl());
                            announcement.setLocation(event.getLocation());
                            announcement.setTarget(event.getTarget());
                            announcement.setTopic(event.getTopic());
                            announcement.setDate(event.getDate());
                            announcement.setId("Event");
                            announcement.setType(0);
                            break;
                        case 2:
                            Task task = dataSnapshot.getValue(Task.class);
                            Log.e("AnnouncementFragment","Task is "+dataSnapshot.getValue().toString());
                            announcement.setDetails(task.getDetails());
                            announcement.setTarget(task.getTarget());
                            announcement.setId("Task");
                            announcement.setTopic(task.getTopic());
                            announcement.setDate(task.getDeadlineDate());
                            announcement.setType(1);
                            break;
                    }
                    announcementArrayList.add(announcement);
                    adapterAnnouncements.notifyDataSetChanged();


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);



        Log.d("AnnouncementFragment ", "onCreateView: ");
               return root;
    }


    @Override
    public void onStart() {
        super.onStart();


    }
    RecyclerViewAdapterAnnouncements adapterAnnouncements;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.Recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterAnnouncements = new RecyclerViewAdapterAnnouncements(announcementArrayList,view.getContext(),this);
        recyclerView.setAdapter(adapterAnnouncements);
        Log.d("AnnouncementFragment ", "onViewCreated: ");
    }

    @Override
    public void OnNotClick(int position) {
        if(announcementArrayList.get(position).getId().equals("Event")){
        Intent intent = new Intent(getActivity(),DisplayEventActivity.class);
            intent.putExtra("Topic",announcementArrayList.get(position).getTopic());
            intent.putExtra("Details",announcementArrayList.get(position).getDetails());
            intent.putExtra("Date",announcementArrayList.get(position).getDate());
            intent.putExtra("Location",announcementArrayList.get(position).getLocation());
            if(announcementArrayList.get(position).getImageURL() !=null)
                intent.putExtra("ImageUrl",announcementArrayList.get(position).getImageURL());
            else
                intent.putExtra("ImageUrl","");
            intent.putExtra("Target",announcementArrayList.get(position).getTarget());
            intent.putExtra("EventID",announcementArrayList.get(position).getId());

        startActivity(intent);
    }
        else{
            Intent intent = new Intent(getActivity(),DisplayTaskActivity.class);

            intent.putExtra("Topic",announcementArrayList.get(position).getTopic());
            intent.putExtra("Details",announcementArrayList.get(position).getDetails());
            intent.putExtra("DeadLine",announcementArrayList.get(position).getDate());
            intent.putExtra("Target",announcementArrayList.get(position).getTarget());
            intent.putExtra("TaskID",announcementArrayList.get(position).getId());
            startActivity(intent);


        }
}
}