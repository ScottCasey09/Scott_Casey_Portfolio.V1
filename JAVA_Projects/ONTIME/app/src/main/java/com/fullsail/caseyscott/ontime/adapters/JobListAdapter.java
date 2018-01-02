// Casey Scott
// FINAL PROJECT - 1712
// JobListAdapter.java

package com.fullsail.caseyscott.ontime.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;

import java.util.ArrayList;

public class JobListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Job> jobs;
    private static final int ID_CONSTANT = 0x0001;

    private final Employee employee;

    public JobListAdapter(Context context, ArrayList<Job> jobs, Employee employee) {
        this.context = context;
        this.jobs = jobs;
        this.employee = employee;
    }

    class ViewHolder {
        final ImageButton deleteButton;
        final TextView jobTitle;
        final TextView jobDate;
        final TextView notes;
        final TextView location;
        final ImageButton expander;

        ViewHolder(View v) {
            jobDate = v.findViewById(R.id.job_date);
            jobTitle = v.findViewById(R.id.job_title_cell);
            deleteButton = v.findViewById(R.id.job_list_cell_delete);
            notes = v.findViewById(R.id.job_notes);
            location = v.findViewById(R.id.job_location_cell);
            expander = v.findViewById(R.id.expander);
        }
    }


    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Object getItem(int i) {
        return jobs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i + ID_CONSTANT;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.job_list_cell, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(jobs.get(i)!=null){
            holder.jobDate.setText(jobs.get(i).getDateCreated());
            holder.jobTitle.setText(jobs.get(i).getTitle());
            holder.deleteButton.setTag(jobs.get(i));
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert((Job) v.getTag());

                }
            });
            String location = jobs.get(i).getAddress();
            if(jobs.get(i).getAddress().equals("") && !jobs.get(i).getLocation_lat_long().equals("")){
                location=jobs.get(i).getLocation_lat_long();
            }else if (jobs.get(i).getAddress().equals("") && jobs.get(i).getLocation_lat_long().equals("")){
                location = context.getString(R.string.no_locat_listed);
            }
            holder.notes.setText(jobs.get(i).getNotes());
            holder.location.setText(location);
            final String finalLocation = location;
            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    askIfUserWantsToGoToLocationNav(finalLocation);
                }
            });
            holder.expander.setTag(R.id.arrow_button, holder.expander);
            holder.expander.setTag(R.id.notes_view, holder.notes);
            holder.expander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getId() == R.id.expander) {
                        ImageButton expander = (ImageButton) view.getTag(R.id.arrow_button);
                        TextView notesView = (TextView)view.getTag(R.id.notes_view);
                        if (notesView.getVisibility() == View.GONE) {
                        notesView.setVisibility(View.VISIBLE);
                        expander.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_up));
                        } else {
                        notesView.setVisibility(View.GONE);
                        expander.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down));
                        }
                    }
                }
            });
        }
        return view;
    }

    private void askIfUserWantsToGoToLocationNav(final String job){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(context.getString(R.string.go_to_google_map_question) + job);
        dialog.setTitle(context.getString(R.string.go_to_google_map));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String lat_longs = "";
                String addressQuery = "";
                if(job != null) {
                    lat_longs = job.replace("_", ",");
                }
                if(job != null) {
                    addressQuery = job.replace(" ", "+");
                }
                String query = lat_longs;
                if(lat_longs.equals("") || lat_longs.equals("0.0,0.0")){
                    query = addressQuery;
                }
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + query );
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                // Attempt to start an activity that can handle the Intent
                context.startActivity(mapIntent);
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //Dialog to ask if the user really wants to delete the job selected
    private void alert(final Job job) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog);
        Button yes = dialog.findViewById(R.id.yes);
        Button cancel = dialog.findViewById(R.id.cancel);
        TextView textView = dialog.findViewById(R.id.alert_job_name_space);
        String jobName = "--> " + job.getTitle() + " <--";
        textView.setText(jobName);
        final FirebaseDatabaseHelper[] helper = new FirebaseDatabaseHelper[1];
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.yes:
                        jobs.remove(job);
                        helper[0] = new FirebaseDatabaseHelper();
                        helper[0].addJobsToDatabase(employee);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        return;
                    case R.id.cancel:
                        dialog.dismiss();

                }
            }
        };
        yes.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        dialog.show();

    }
}
