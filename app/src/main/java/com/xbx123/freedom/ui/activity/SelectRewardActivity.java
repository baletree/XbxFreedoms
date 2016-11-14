package com.xbx123.freedom.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xbx123.freedom.R;
import com.xbx123.freedom.adapter.RewardMonAdapter;
import com.xbx123.freedom.view.views.FullyLinearLayoutManager;
import com.xbx123.freedom.view.views.RecycleViewDivider;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EricYuan on 2016/6/14.
 */
public class SelectRewardActivity extends Activity implements RewardMonAdapter.OnRecyItemClickListener {
    @Bind(R.id.rewardMon_rv)
    RecyclerView rewardMonRv;

    private RewardMonAdapter adapter = null;
    private List<Integer> rewardList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_mon);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rewardList = getIntent().getIntegerArrayListExtra("rewardList");
        rewardMonRv.setLayoutManager(new FullyLinearLayoutManager(this));
        rewardMonRv.setItemAnimator(new DefaultItemAnimator());
        rewardMonRv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        if (rewardList == null)
            return;
        adapter = new RewardMonAdapter(this, rewardList);
        rewardMonRv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @OnClick(R.id.sexCancel_tv)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra("RewardMonPosition", -1);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent();
        intent.putExtra("RewardMonPosition", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
