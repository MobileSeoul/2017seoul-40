package com.js.sci.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.js.sci.R;
import com.js.sci.adapter.BaseRecyclerAdapter;
import com.js.sci.adapter.BaseViewHolder;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.network.SCIImageLoader;
import com.js.sci.util.SCILog;

/**
 * Created by jskim on 2017. 10. 11..
 */

public class BookmarkActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private BookmarkRecyclerAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bookmark;
    }

    @Override
    protected void initView() {
        setActionbar(R.string.bookmark, true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initEvent() {
        adapter = new BookmarkRecyclerAdapter();
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SCIData>() {
            @Override
            public void onItemClick(SCIData data) {
                Intent intent = new Intent(BookmarkActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_CULTCODE, data.getCULTCODE());
                startActivityForResult(intent, REQUEST_CDDE_DETAIL);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        refreshList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SCILog.debug("onActivityResult : " + requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CDDE_DETAIL:
                    refreshList();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }


    @Override
    protected void clear() {

    }

    private void refreshList() {
        adapter.clear();
        adapter.addAll(SCIDatabaseManager.getInstance().getBookmarkList());
    }

    class BookmarkRecyclerAdapter extends BaseRecyclerAdapter<SCIData> {

        @Override
        protected int getResource() {
            return R.layout.view_list_bookmark_item;
        }

        @Override
        protected BaseViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }
    }

    class ItemViewHolder extends BaseViewHolder<SCIData> {

        NetworkImageView image;
        TextView title;
        TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            image = (NetworkImageView)itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            date = (TextView)itemView.findViewById(R.id.tv_date);
        }

        @Override
        protected void bind(SCIData item, int position) {
            image.setImageUrl(item.getMAIN_IMG(), SCIImageLoader.getInstance().getImageLoader());
            title.setText(item.getTITLE());
            date.setText(item.getSTRTDATE() +  "~" + item.getEND_DATE() + "\n\n" + item.getPLACE());
        }
    }
}
