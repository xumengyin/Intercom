package com.jerry.simpleui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class LazyFragment extends BaseFragment {

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void loadData(Bundle savedInstanceState) {
       // super.loadData(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public abstract void fetchData();

    public void dataInit() {

    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated) {
            if (!isDataInitiated) {
                dataInit();
                isDataInitiated = true;
            }
            fetchData();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewInitiated = false;
    }
}
