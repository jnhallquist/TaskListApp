package com.jnguyen.android.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jennifer on 3/24/2016.
 */
public class TaskListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private boolean mSubtitleVisible = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSocket.connect();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        mTaskRecyclerView = (RecyclerView) view
                .findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_task:
                Task task = new Task();
                TaskStore.get(getActivity()).addTask(task);
                Intent intent = TaskPagerActivity
                        .newIntent(getActivity(), task.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        TaskStore taskStore = TaskStore.get(getActivity());
        int taskCount = taskStore.getTasks().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, taskCount, taskCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        TaskStore taskStore = TaskStore.get(getActivity());
        List<Task> tasks = taskStore.getTasks();

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasks);
            Log.i("socket", "socket");
            //mSocket.emit("tasks update", tasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Task mTask;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mDoneCheckBox;

        public TaskHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_task_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_task_date_text_view);
            mDoneCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_task_done_check_box);
        }

        public void bindTask(Task task) {
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(DateFormat.format("EEE, MMM dd, yyyy kk:mm", mTask.getDate()));
            mDoneCheckBox.setChecked(mTask.isDone());
        }

        @Override
        public void onClick(View v) {
            Intent intent = TaskPagerActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }
    }
}