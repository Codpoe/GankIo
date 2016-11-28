package me.codpoe.gankio.main.info;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import me.codpoe.gankio.R;
import me.codpoe.gankio.data.Repository;

/**
 * Created by Codpoe on 2016/11/1.
 */

public class InfoPresenter implements InfoContract.Presenter {

    private Context mContext;
    private InfoContract.View mView;
    private Repository mRepository;

    @Inject
    public InfoPresenter(InfoContract.View view) {
        mView = view;
        mContext = ((InfoFrag)mView).getContext();
        mRepository = Repository.getInstance();
    }

    @Override
    public void share(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_application)));
    }

}
