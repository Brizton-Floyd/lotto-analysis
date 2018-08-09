package com.lottoanalysis.ui.gameselection;

import com.lottoanalysis.ui.homeview.HomeViewListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface GameSelectionViewListener {

    void injectMenuItemValues();

    void setHomeViewListener(HomeViewListener homeViewListener);

    void notifyMainViewOfValueChange(String text, boolean flag);

    void executeGameUpdates();

    void invokeDbService();

    void reloadViewPostUpdate(boolean flag);

    void bindToMessageProperty(ReadOnlyStringProperty s);

    void unbindDataFromProgressAndMessage();

    void bindToMessageAndProgress(ReadOnlyStringProperty readOnlyStringProperty, ReadOnlyDoubleProperty readOnlyDoubleProperty);

    void showMessageLabel();

    void showProgess();
}
