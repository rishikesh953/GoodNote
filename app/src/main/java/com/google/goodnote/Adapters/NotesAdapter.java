package com.google.goodnote.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.goodnote.Activities.MainActivity;
import com.google.goodnote.Fragments.UpdateNewTaskFragment;
import com.google.goodnote.Fragments.UpdateNotesFrag;
import com.google.goodnote.Models.NoteClass;
import com.google.goodnote.R;
import com.google.goodnote.databinding.NotesListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    Context context;
    ArrayList<NoteClass> notelist;

    public NotesAdapter(Context context, ArrayList<NoteClass> notelist) {
        this.context = context;
        this.notelist = notelist;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        int colorCode = getRandomColor();
        holder.binding.noteBackground.setBackgroundColor(holder.itemView.getResources().getColor(colorCode));

        NoteClass note = notelist.get(position);
        holder.binding.noteTitle.setText(note.getTitle());
        holder.binding.noteContent.setText(note.getNote());
        String Id = note.getTitle();
        holder.binding.editDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu editDelete = new PopupMenu(v.getContext(), v);
                editDelete.getMenuInflater().inflate(R.menu.note_edit_delete,
                        editDelete.getMenu());
                editDelete.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.note_delete:
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,
                                    R.style.AppTheme_Dialog);
                            alertDialogBuilder.setTitle(R.string.delete_confirmation).
                                    setPositiveButton(R.string.yes, (dialog, which) -> {
                                        deleteTask(Id);
                                    })
                                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                            break;
                        case R.id.note_edit:

                            UpdateNotesFrag bottomSheet = new UpdateNotesFrag();
                            Bundle bundle = new Bundle();
                            bundle.putString("Id", Id);
                            bottomSheet.setArguments(bundle);
                            bottomSheet .show(((MainActivity) context).getSupportFragmentManager(),
                                    bottomSheet.getTag());

                            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                });
                editDelete.show();
            }
        });
    }

    private int getRandomColor() {
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.color1);
        colors.add(R.color.color2);
        colors.add(R.color.color3);
        colors.add(R.color.color4);
        colors.add(R.color.color5);
        colors.add(R.color.color6);
        colors.add(R.color.color7);
        colors.add(R.color.color8);
        colors.add(R.color.color9);
        colors.add(R.color.color10);
        colors.add(R.color.color11);
        colors.add(R.color.color12);

        Random random = new Random();
        int number = random.nextInt(colors.size());
        return colors.get(number);
    }

    private void deleteTask(String id) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference mReference =
                mDatabase.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        Log.v("TaskAdapter", id);

        mReference.child("Notes")
                .child(id)
                .setValue(null);

    }

    private void showCompleteDialog(String Id) {

        Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view -> {
            deleteTask(Id);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        NotesListBinding binding;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotesListBinding.bind(itemView);
        }
    }
}
