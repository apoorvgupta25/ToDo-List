package com.example.todolist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;
    private Activity mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
        void onChangeClick(int position);
    }

    //  Constructor
    public ExampleAdapter(MainActivity context, ArrayList<ExampleItem> exampleList) {
        this.mExampleList = exampleList;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView statusImageView;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            statusImageView = itemView.findViewById(R.id.statusImageView);

            //OnClick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            //OnLongClick
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });

            //OnViewClick
            statusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onChangeClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        //These things are shown in the recycler view
        holder.titleTextView.setText(currentItem.getTitleText());
        holder.statusImageView.setImageResource(currentItem.getStatusImage());

    }


    //View
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.mContext.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.example_item, parent, false);
        }

        //Set the name
        ((TextView) view.findViewById(R.id.titleTextView)).setText(mExampleList.get(position).getTitleText());

        return view;
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
