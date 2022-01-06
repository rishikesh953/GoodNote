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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.goodnote.Activities.MainActivity;
import com.google.goodnote.Fragments.UpdateCalenderEvent;
import com.google.goodnote.Fragments.UpdateNewTaskFragment;
import com.google.goodnote.Models.NewTask;
import com.google.goodnote.R;
import com.google.goodnote.databinding.CalenderEventListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CalenderTaskAdapter extends RecyclerView.Adapter<CalenderTaskAdapter.CalenderViewHolder> {

    Context context;
    ArrayList<NewTask> events;

    public CalenderTaskAdapter(Context context, ArrayList<NewTask> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calender_event_list, parent, false);
        return new CalenderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {
        NewTask event = events.get(position);
        holder.binding.titleTextView.setText(event.getTitle());
        holder.binding.taskTextView.setText(event.getTask());
        holder.binding.dateTextView.setText(event.getDate());
        if(event.getPriority() == 1)
        {
            holder.binding.imageView7.setImageResource(R.drawable.ic_baseline_stars_red_24);
        }
        else if(event.getPriority() == 2)
        {
            holder.binding.imageView7.setImageResource(R.drawable.ic_baseline_stars_24);
        }
        else
        {
            holder.binding.imageView7.setImageResource(R.drawable.ic_baseline_stars_green_24);
        }

        int colorCode = getRandomColor();
        holder.binding.parentLayout.setBackgroundColor(holder.itemView.getResources().getColor(colorCode));

        holder.binding.editDeleteCalender.setOnClickListener(view -> showPopUpMenu(view, position));

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

    private void showPopUpMenu(View view, int position) {

        final NewTask event = events.get(position);
        String Id = event.getTitle()+ event.getDate()+ event.getPriority();
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.edit_delete_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,
                            R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).
                            setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteTask(Id);
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.edit:

                    UpdateCalenderEvent fragmentSheet = new UpdateCalenderEvent();
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", Id);
                    bundle.putString("Date", event.getDate());
                    fragmentSheet.setArguments(bundle);
                    fragmentSheet .show(((MainActivity) context).getSupportFragmentManager(),
                            fragmentSheet.getTag());

                    break;
                case R.id.complete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    completeAlertDialog.setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton(R.string.yes,
                                    (dialog, which) ->{ showCompleteDialog(Id);})
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
            }
            return false;
        });
        popupMenu.show();

    }

    private void deleteTask(String id) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference mReference =
                mDatabase.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        Log.v("TaskAdapter", id);

        mReference.child("CalenderEvents")
                .child(id)
                .setValue(null);

    }


    private void showCompleteDialog(String Id)
    {
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
        return events.size();
    }

    public class CalenderViewHolder extends RecyclerView.ViewHolder
    {
        CalenderEventListBinding binding;

        public CalenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CalenderEventListBinding.bind(itemView);

        }


    }

}
