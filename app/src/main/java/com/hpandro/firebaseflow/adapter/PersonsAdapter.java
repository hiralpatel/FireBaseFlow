package com.hpandro.firebaseflow.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hpandro.firebaseflow.R;
import com.hpandro.firebaseflow.activity.MainActivity;
import com.hpandro.firebaseflow.pojo.Person;

import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.MyViewHolder> {

    private final Activity mActivity;
    private List<Person> personList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDetails, tvName;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDetails = (TextView) view.findViewById(R.id.tvDetails);
        }
    }

    public PersonsAdapter(Activity mActivity, List<Person> mList) {
        this.personList = mList;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_person, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Person mPerson = personList.get(position);
        holder.tvName.setText(mPerson.getName());
        holder.tvDetails.setText(mPerson.getDetails());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mActivity).updateData(mPerson.getId(), mPerson.getName(), mPerson.getDetails());
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}