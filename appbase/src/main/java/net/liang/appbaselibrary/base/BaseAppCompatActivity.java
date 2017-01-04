package net.liang.appbaselibrary.base;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;

import net.liang.appbaselibrary.AppManager;
import net.liang.appbaselibrary.R;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.utils.StringUtils;

import butterknife.ButterKnife;

/**
 * Created by lianghuiyong@outlook.com on 2016/5/25.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseViewInterface {

    protected abstract int getLayoutId();

    protected abstract MvpPresenter getPresenter();

    private ViewDataBinding binding;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        binding = DataBindingUtil.setContentView(this, getLayoutId());
        ButterKnife.bind(this, getView());
        //disposables = new CompositeDisposable();
        //presenter = addPresenter();
        /*dialogHelper = new DialogHelper(this);*/

        init();
        initTabs();
    }

    protected ViewDataBinding getBinding() {
        return binding;
    }

    public View getView() {
        return binding.getRoot();
    }

    @Override
    public void init() {

    }

    @Override
    public void initTabs() {

    }

    public void setToolbar(Boolean hasBackHome) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);    //该设置要放setTitle之后，否则setTitle会无效
        getSupportActionBar().setDisplayShowTitleEnabled(false); //取消显示默认标题

        if (hasBackHome) {
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    public void setToolbar(Boolean hasBackHome, String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }

        toolbar.setTitle(title);

        setSupportActionBar(toolbar);   //该设置要放setTitle之后，否则setTitle会无效

        if (hasBackHome) {
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    public void setToolbarCentel(Boolean hasBackHome, String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }

        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (hasBackHome) {
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    public void setToolbarCentel_Img(Boolean hasBackHome, String title, int imageID) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }

        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (hasBackHome) {
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageResource(imageID);
    }

    public void setToolbarCentel_tv(Boolean hasBackHome, String title, String right_title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }

        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (hasBackHome) {
            ImageView back = (ImageView) findViewById(R.id.toolbar_back);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        TextView right_tv = (TextView) findViewById(R.id.right_tv);
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText(right_title);
    }

    public void nextActivity(Class<?> firstCls, Class<?> SecondCls) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(this, SecondCls));
        intents[1] = new Intent(this, firstCls);
        startActivities(intents);
    }

    public void nextActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void nextActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    public void nextActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void nextActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getPresenter() != null) {
            KLog.e("onResume subscribe");
            getPresenter().subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getPresenter() != null) {
            KLog.e("onResume unsubscribe");
            getPresenter().unSubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
    }
}
