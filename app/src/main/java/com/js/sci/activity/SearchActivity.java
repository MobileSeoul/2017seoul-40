package com.js.sci.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.js.sci.R;
import com.js.sci.adapter.BaseRecyclerAdapter;
import com.js.sci.adapter.BaseViewHolder;
import com.js.sci.database.SCIDatabaseManager;
import com.js.sci.dto.SCIData;
import com.js.sci.util.SCILog;

import java.util.List;

/**
 * Created by a80099709 on 2017. 9. 12..
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{
    private EditText etKeyword;
    private View ivSearch;
    private View ivClose;

    private RecyclerView recyclerView;

    private SearchRecyclerAdapter adapter;

    private String keyword;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        setActionbar(-1, true);
        etKeyword = (EditText) findViewById(R.id.et_keyword);
        ivSearch = findViewById(R.id.iv_search);
        ivClose = findViewById(R.id.iv_close);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initEvent() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    showActionButton(false);
                } else {
                    showActionButton(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    actionSearch();
                    return true;
                }
                return false;

            }
        });

        ivSearch.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        adapter = new SearchRecyclerAdapter();

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SCIData>() {
            @Override
            public void onItemClick(SCIData data) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_CULTCODE, data.getCULTCODE());
                startActivityForResult(intent, REQUEST_CDDE_DETAIL);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void clear() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                actionSearch();
                break;
            case R.id.iv_close:
                actionClose();
                break;
        }

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

    private void showActionButton(boolean isShow) {
        if (isShow) {
            if (ivSearch.getVisibility() != View.VISIBLE) {
                ivSearch.setVisibility(View.VISIBLE);
            }
            if (ivClose.getVisibility() != View.VISIBLE) {
                ivClose.setVisibility(View.VISIBLE);
            }
        } else {
            if (ivSearch.getVisibility() != View.INVISIBLE) {
                ivSearch.setVisibility(View.INVISIBLE);
            }
            if (ivClose.getVisibility() != View.INVISIBLE) {
                ivClose.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void actionSearch() {
        SCILog.debug("actionSearch : " + etKeyword.getText().toString());

        adapter.clear();
        keyword = etKeyword.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            showMsgDialog(getString(R.string.search_hint));
            return;
        }
        List<SCIData> list = SCIDatabaseManager.getInstance().getSearchList(keyword);

        if (list.size() == 0) {
            showMsgDialog(getString(R.string.search_no_data));
        } else {
            adapter.addAll(list);
        }

        etKeyword.clearFocus();
        hideKeyboard();
    }

    private void actionClose() {
        etKeyword.setText("");
        keyword = "";
        adapter.clear();
    }

    private void showMsgDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void hideKeyboard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SCILog.debug("hideKeyboard");
                InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }, 100);
    }

    class SearchRecyclerAdapter extends BaseRecyclerAdapter<SCIData> {

        @Override
        protected int getResource() {
            return R.layout.view_list_search;
        }

        @Override
        protected BaseViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }
    }

    class ItemViewHolder extends BaseViewHolder<SCIData> {
        TextView title;
        TextView date;
        TextView desc;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            title = (TextView) itemView.findViewById(R.id.tv_titie);
            date = (TextView)itemView.findViewById(R.id.tv_date);
            desc = (TextView)itemView.findViewById(R.id.tv_desc);
        }

        @Override
        protected void bind(SCIData data, int position) {

            title.setText(Html.fromHtml(highlight(data.getTITLE())));
            date.setText(Html.fromHtml(highlight(data.getSTRTDATE() + " ~ " + data.getEND_DATE() + " / " + data.getPLACE())));
            desc.setText(Html.fromHtml(highlight(data.getPROGRAM())));

        }

        private String highlight(String str) {

            return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(keyword, String.format(FONT_PATTERN, keyword));

        }
    }


}
