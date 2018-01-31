package com.js.sci.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.js.sci.R;
import com.js.sci.adapter.BaseHeaderRecyclerAdapter;
import com.js.sci.adapter.BaseRecyclerAdapter;
import com.js.sci.adapter.BaseViewHolder;
import com.js.sci.adapter.HeaderSpanSizeLookup;
import com.js.sci.constant.LIST_COUNT;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.network.SCIImageLoader;
import com.js.sci.util.SCILog;
import com.js.sci.util.SCIPreferences;

/**
 * Created by jskim on 2017. 9. 12..
 */

public class MainActivity extends BaseActivity {

    private int finishIntervalTime = 2000;
    private long backPressedTime = 0;
    private int finishTextId = R.string.finish_text;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private MainReyclerAdapter adapter;

    private int offset = 0;
    private int count = 0;

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && finishIntervalTime >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            showToast(finishTextId);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    @Override
    protected void initEvent() {
        setActionbar(R.string.app_name, false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        adapter = new MainReyclerAdapter();

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SCIData>() {
            @Override
            public void onItemClick(SCIData data) {
                goDetail(data.getCULTCODE());
            }
        });

        layoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(adapter, layoutManager));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        refreshList();
        refreshHeader();
        refreshFooter();

        String id = getIntent().getStringExtra(EXTRA_CULTCODE);
        if (!TextUtils.isEmpty(id)) {
            goDetail(id);
        }
    }

    @Override
    protected void clear() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                startActivityForResult(new Intent(this, SearchActivity.class), REQUEST_CDDE_SEARCH);
                break;
            case R.id.action_setting:
                startActivityForResult(new Intent(this, SettingActivity.class), REQUEST_CDDE_SETTING);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SCILog.debug("onActivityResult : " + requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CDDE_SETTING:
                    refreshList();
                    refreshFooter();
                    break;
                case REQUEST_CDDE_SEARCH:
                case REQUEST_CDDE_BOOKMARK:
                case REQUEST_CDDE_DETAIL:
                    refreshHeader();
                    break;
            }
        } else {
            if (requestCode == REQUEST_CDDE_SETTING && resultCode == RESULT_DATABASE_FAIL) {
                finish();
            } else if (requestCode == REQUEST_CDDE_SETTING && resultCode == RESULT_RESET) {
                startActivity(new Intent(this, IntroActivity.class));
                finish();
            }
        }
    }

    private void refreshList() {
        adapter.clear();
        adapter.addAll(SCIDatabaseManager.getInstance().getList());
        offset = 0;
    }
    private void refreshHeader() {
        SCILog.debug("refreshHeader : ");
        SCIData bookmarkData = SCIDatabaseManager.getInstance().getBookmark();
        if (bookmarkData != null) {
            adapter.showHeader(bookmarkData);
        } else {
            adapter.hideHeader();
        }
    }
    private void refreshFooter() {
        SCILog.debug("refreshFooter : ");
        count = SCIDatabaseManager.getInstance().getCount();
        offset += LIST_COUNT.valueOf(SCIPreferences.getInstance().getListCount()).getCount();
        if (count > offset) {
            adapter.showFooter(null);
        } else {
            adapter.hideFooter();
        }
    }

    private void goDetail(String cultcode) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_CULTCODE, cultcode);
        startActivityForResult(intent, REQUEST_CDDE_DETAIL);
    }



    class MainReyclerAdapter extends BaseHeaderRecyclerAdapter<SCIData, SCIData, Void> {

        public MainReyclerAdapter() {
            super();
        }

        @Override
        protected int getResource() {
            return R.layout.view_list_main_item;
        }

        @Override
        protected ItemViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }


        @Override
        protected int getHeaderResource() {
            return R.layout.view_list_main_header;
        }

        @Override
        protected HeaderViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        protected int getFooterResource() {
            return R.layout.view_list_main_footer;
        }

        @Override
        protected FooterViewHolder getFooterViewHolder(View view) {
            return new FooterViewHolder(view);
        }
    }

    class ItemViewHolder extends BaseViewHolder<SCIData> {

        NetworkImageView image;
        TextView tvHash1;
        TextView tvHash2;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            image = (NetworkImageView)itemView.findViewById(R.id.image);
            tvHash1 = (TextView)itemView.findViewById(R.id.tv_hash1);
            tvHash2 = (TextView)itemView.findViewById(R.id.tv_hash2);
        }

        @Override
        protected void bind(SCIData data, int position) {

            if (TextUtils.isEmpty(data.getCODENAME())) {
                tvHash1.setVisibility(View.GONE);
            } else {
                tvHash1.setVisibility(View.VISIBLE);
                tvHash1.setText(data.getCODENAME());
            }

            if (TextUtils.isEmpty(data.getGCODE())) {
                tvHash2.setVisibility(View.GONE);
            } else {
                tvHash2.setVisibility(View.VISIBLE);
                tvHash2.setText(data.getGCODE());
            }

            image.setImageUrl(data.getMAIN_IMG(), SCIImageLoader.getInstance().getImageLoader());
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams)itemView.getLayoutParams();
            if (position % 2 == 0) {
                layoutParams.leftMargin = Math.round(8 * getResources().getDisplayMetrics().density);
                layoutParams.rightMargin = Math.round(4 * getResources().getDisplayMetrics().density);
            } else {
                layoutParams.leftMargin = Math.round(4 * getResources().getDisplayMetrics().density);
                layoutParams.rightMargin = Math.round(8 * getResources().getDisplayMetrics().density);
            }
            itemView.setLayoutParams(layoutParams);
        }
    }

    class HeaderViewHolder extends BaseViewHolder<SCIData> {

        NetworkImageView image;
        TextView title;
        TextView date;

        public HeaderViewHolder(View itemView) {
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

            itemView.findViewById(R.id.ll_bookmark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                    startActivityForResult(intent, REQUEST_CDDE_BOOKMARK);
                }
            });

            final String cultcode = item.getCULTCODE();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(EXTRA_CULTCODE, cultcode);
                    startActivityForResult(intent, REQUEST_CDDE_DETAIL);
                }
            });
        }
    }

    class FooterViewHolder extends BaseViewHolder {
        TextView more;

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            more = (TextView) itemView.findViewById(R.id.tv_more);
        }

        @Override
        protected void bind(Object item, int position) {
            more.setText(getString(R.string.more) + "(" +  offset + " / " + count + ")");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.addAll(SCIDatabaseManager.getInstance().getList(offset));
                    refreshFooter();
                }
            });
        }
    }
}
