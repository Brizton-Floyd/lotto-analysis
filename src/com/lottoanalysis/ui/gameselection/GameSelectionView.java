package com.lottoanalysis.ui.gameselection;

import javafx.scene.layout.AnchorPane;

import java.util.List;

public interface GameSelectionView {

    void initializeListener(GameSelectionViewListener gameSelectionViewListener);

    void initializeView();

    void setMenuBarItems(List<String> values);

    AnchorPane view();

}