package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Note> notes;

    Adapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = notes.get(i).getTitle();
        String photo = notes.get(i).getPhoto();
        String last_modification = notes.get(i).getLast_modification();
        String created = notes.get(i).getCreation();
        int color = notes.get(i).getColor();
        if (color != 0) {
            viewHolder.colorView.setColorFilter(color);
        }
        viewHolder.nTitle.setText(title);

        if (last_modification != null) {
            String updatedModification = TimeAgo.getTimeAgo(Long.parseLong(last_modification), MainActivity.resources);
            viewHolder.nLastModification.setText(updatedModification);
        } else {
            String modification = TimeAgo.getTimeAgo(Long.parseLong(created), MainActivity.resources);
            viewHolder.nLastModification.setText(modification);
        }

            viewHolder.nPhoto.setImageURI(Uri.parse(photo));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle, nLastModification;
        ImageView nPhoto, colorView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nTitle = itemView.findViewById(R.id.nTitle);
            nPhoto = itemView.findViewById(R.id.photoView);
            nLastModification = itemView.findViewById(R.id.lastModificationView);
            colorView = itemView.findViewById(R.id.colorView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Edit.class);
                    i.putExtra("ID", notes.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
