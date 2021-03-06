package com.lottoanalysis.ui.modifieddrawview;

import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;

import java.util.Map;

public interface ModifiedDrawListener {

    void show();

    void updateModelList(Map<Integer, String> valAndKeys);

    void updateCrudeOpertaion(CrudOperation crudOperation);
}
