package com.lottoanalysis.models.lottogames;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickThreeLotteryGameImpl extends LottoGame {

    public PickThreeLotteryGameImpl(){
        setPositionNumbersAllowed(3);
    }
    @Override
    public void startThreadForJackpotRetrieval() {

        setCurrentEstimatedJackpot("No Jackpot Available");
    }
}
