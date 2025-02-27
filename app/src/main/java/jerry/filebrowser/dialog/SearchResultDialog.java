package jerry.filebrowser.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jerry.filebrowser.R;
import jerry.filebrowser.activity.MainActivity;
import jerry.filebrowser.adapter.FileSearchListAdapter;
import jerry.filebrowser.file.UnixFile;

public class SearchResultDialog extends BaseDialog implements View.OnClickListener {
    private MainActivity activity;
    private Button bu_sure;
    private ArrayList<UnixFile> list;
    private RecyclerView recyclerView;
    private FileSearchListAdapter adapter;

    public SearchResultDialog(@NonNull Context context) {
        super(context);
        activity = (MainActivity) context;
        recyclerView = findViewById(R.id.dialog_rev);
        recyclerView.setHasFixedSize(true);
        adapter = new FileSearchListAdapter(activity, recyclerView);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));


        findViewById(R.id.bu_cancel).setOnClickListener(v -> dismiss());
        bu_sure = findViewById(R.id.bu_sure);
        bu_sure.setOnClickListener(v -> {
            dismiss();
        });

        setOnDismissListener(dialog -> {
            list = null;
            adapter.setResult(null);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_search_result;
    }

    @Override
    public void onClick(View view) {
        final int position = recyclerView.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) return;
        final UnixFile file = list.get(position);
        if (file.isDir()) {
            MainActivity.sendNavigation(activity, file.getAbsPath());
        } else {
            MainActivity.sendNavigation(activity, file.getParentPath());
        }
        dismiss();
    }

    public void show(ArrayList<UnixFile> list) {
        this.list = list;
        adapter.setResult(list);
        adapter.notifyDataSetChanged();
        show();
    }
}