package com.sprout.clipcon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sprout.clipcon.R;
import com.sprout.clipcon.adapter.MemberAdapter;
import com.sprout.clipcon.model.Member;

import java.util.ArrayList;


/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class InfoFragment extends Fragment {

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        System.out.println("그룹화면으로 진입");

        TextView infoGroupName = (TextView) view.findViewById(R.id.group_name);
        TextView infoGroupKey = (TextView) view.findViewById(R.id.group_key);
        String groupName = getActivity().getIntent().getStringExtra("name");
        String groupKey = getActivity().getIntent().getStringExtra("key");

        infoGroupName.setText(groupName);
        infoGroupKey.setText(groupKey);

        ArrayList<Member> membersArrayList = new ArrayList<>();
        membersArrayList.add(new Member("Member 1"));
        membersArrayList.add(new Member("Member 2"));
        membersArrayList.add(new Member("Member 3"));
        membersArrayList.add(new Member("Member 4"));
        membersArrayList.add(new Member("Member 5"));
        membersArrayList.add(new Member("Member 6"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        memberAdapter = new MemberAdapter(membersArrayList);
        recyclerView.setAdapter(memberAdapter);

        return view;
    }
}
