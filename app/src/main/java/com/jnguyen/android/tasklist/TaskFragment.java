package com.jnguyen.android.tasklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jennifer on 3/24/2016.
 */
public class TaskFragment extends Fragment {

    private static final String ARG_TASK_ID = "task_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Task mTask;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mCompletedCheckBox;
    private Button mSendButton;

    public static TaskFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskStore.get(getActivity()).getTask(taskId);
    }

    @Override
    public void onPause() {
        super.onPause();

        TaskStore.get(getActivity())
                .updateTask(mTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        mTitleField = (EditText)v.findViewById(R.id.task_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mDateButton = (Button)v.findViewById(R.id.deadline_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mTask.getDate());
                dialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button)v.findViewById(R.id.deadline_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mTask.getDate());
                dialog.setTargetFragment(TaskFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });


        mCompletedCheckBox = (CheckBox)v.findViewById(R.id.task_completed);
        mCompletedCheckBox.setChecked(mTask.isDone());
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setDone(isChecked);
            }
        });

        mSendButton = (Button) v.findViewById(R.id.task_send);
        mSendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getTaskDetails());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.task_details_subject));
                i = Intent.createChooser(i, getString(R.string.assign_task));
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTask.setDate(date);
            updateDate();
        }

        else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_DATE);
            mTask.setTime(date);
            updateTime();
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEE, MMM dd, yyyy", mTask.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(DateFormat.format("kk:mm", mTask.getDate()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task, menu);
    }

    private String getTaskDetails() {
        String dateFormat = "EEE, MMM dd, yyyy";
        String dateString = DateFormat.format(dateFormat, mTask.getDate()).toString();
        String timeFormat = "kk:mm";
        String timeString = DateFormat.format(timeFormat, mTask.getDate()).toString();

        String details = getString(R.string.task_details,
                mTask.getTitle(), dateString, timeString);

        return details;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_task:
                UUID taskId = mTask.getId();
                TaskStore.get(getActivity()).removeTask(taskId);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
