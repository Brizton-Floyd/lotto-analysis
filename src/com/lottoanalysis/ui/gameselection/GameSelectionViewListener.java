package com.lottoanalysis.ui.gameselection;

import com.lottoanalysis.ui.homeview.HomeViewListener;

public interface GameSelectionViewListener {

    void injectMenuItemValues();

    void setHomeViewListener(HomeViewListener homeViewListener);

    void notifyMainViewOfValueChange(String text, boolean flag);

    void executeGameUpdates();

    void invokeDbService();

    void reloadViewPostUpdate(boolean flag);
}
