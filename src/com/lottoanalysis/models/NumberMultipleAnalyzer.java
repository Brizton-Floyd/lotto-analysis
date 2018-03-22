package com.lottoanalysis.models;

import java.util.*;
import java.util.stream.*;

import com.lottoanalysis.lottogames.LottoGame;

public class NumberMultipleAnalyzer {

    private Map<Integer, NumberMultipleAnalyzer> multipleHitOccurenceHolder = new TreeMap<>();
    private Map<Integer, LottoNumber> lottoNumberHitOccurenceHolder = new TreeMap<>();
    private int multipleHits;
    private int multipleGamesOut;
    private int multipleHitsAtGamesOut;
    private int multipleGameOutLastAppearance;
    private List<Integer> gamesOutHolder = new ArrayList<>();
    private LottoNumber lottoNumber;

    private static Map<Integer, List<Integer>> multipleRanges = new LinkedHashMap<>();

    static {
        multipleRanges.put(7, new ArrayList<>());
        multipleRanges.put(5, new ArrayList<>());
        multipleRanges.put(3, new ArrayList<>());
        multipleRanges.put(2, new ArrayList<>());
        multipleRanges.put(1, new ArrayList<>());
    }

    private NumberMultipleAnalyzer() {
        this.multipleGamesOut = -1;
        this.multipleHits = -1;
        this.multipleHitsAtGamesOut = -1;
        this.multipleGameOutLastAppearance = -1;
        lottoNumber = new LottoNumber();
    }

    public NumberMultipleAnalyzer(LottoGame game) {

        popluateRangeBuckets(game.getMinNumber(), game.getMaxNumber());
    }

    // getters and setters


    public void setMultipleHits(int val) {
        this.multipleHits = val;
    }

    public void setMultipleHitsAtGamesOut(int val) {
        this.multipleHitsAtGamesOut = val;
    }

    public void setMultipleGameOutLastAppearance(int val) {
        this.multipleGameOutLastAppearance = val;
    }

    public void setMultipleGamesOut(int val) {
        this.multipleGamesOut = val;
    }

    public int getMultipleHits() {
        return this.multipleHits;
    }

    public int getMultipleGamesOut() {
        return this.multipleGamesOut;
    }

    public int getMultipleHitsAtGamesOut() {
        return this.multipleHitsAtGamesOut;
    }

    public int getMultipleGameOutLastAppearance() {
        return this.multipleGameOutLastAppearance;
    }

    private List<Integer> getGameOutHolder() {
        return gamesOutHolder;
    }

    private Map<Integer, LottoNumber> getLottoNumberHitOccurenceHolder() {
        return this.lottoNumberHitOccurenceHolder;
    }

    public void print() {

        multipleHitOccurenceHolder.forEach((k, v) -> {

            System.out.printf("\n<----- Multiple: %s %10s %4s %15s %4s %15s %4s %15s %4s ----->", k, "Hits:", v.getMultipleHits(), "Games Out:", v.getMultipleGamesOut(),
                    "Hits @ G Out:", v.getMultipleHitsAtGamesOut(), "Last Seen:", v.getMultipleGameOutLastAppearance());

            System.out.println();

            Map<Integer, LottoNumber> numData = v.getLottoNumberHitOccurenceHolder();
            numData.forEach((lottoNumber, hitHolder) -> {
                System.out.printf("\nLotto #: %3s %9s %4s %15s %4s %20s %4s %15s %4s", lottoNumber, "Hits:", hitHolder.getLottoNumberHits(), "Games Out:", hitHolder.lottoNumberGamesOut,
                        "Hits @ Games Out:", hitHolder.getHitsAtGamesOut(),"Last Seen:",hitHolder.getOutLastSeen());
            });

            System.out.println();
        });
    }

    /**
     * Method will take in a lotto number and analyze it for the multiple it represents and then that value will be added to map
     * for storage
     */
    public void analyzeLottoNumber(final int number) {

        Set<Integer> keys = new LinkedHashSet<>(multipleRanges.keySet());
        for (Iterator<Integer> data = keys.iterator(); data.hasNext(); ) {

            int key = data.next();
            List<Integer> numbersHolder = multipleRanges.get(key);
            if (numbersHolder.contains(number)) {

                if (!multipleHitOccurenceHolder.containsKey(key)) {

                    NumberMultipleAnalyzer analyzer = new NumberMultipleAnalyzer();
                    analyzer.setMultipleHits(1);
                    analyzer.setMultipleGamesOut(0);

                    Map<Integer,LottoNumber> lottoNumData = analyzer.getLottoNumberHitOccurenceHolder();
                    LottoNumber lottoNumber = new LottoNumber();
                    lottoNumber.setLottoNumberHits(1);
                    lottoNumber.setLottoNumberGamesOut(0);

                    lottoNumData.put(number, lottoNumber);
                    multipleHitOccurenceHolder.put(key, analyzer);

                    incrementLottoNumberGamesOut(lottoNumData, number);
                    incrementGamesOut(multipleHitOccurenceHolder, key);



                } else {

                    NumberMultipleAnalyzer analyzer = multipleHitOccurenceHolder.get(key);
                    int hits = analyzer.getMultipleHits();
                    analyzer.setMultipleHits(++hits);
                    analyzer.gamesOutHolder.add(analyzer.getMultipleGamesOut());
                    analyzer.setMultipleGamesOut(0);

                    Map<Integer, LottoNumber> lottoNumData = analyzer.getLottoNumberHitOccurenceHolder();
                    if (!lottoNumData.containsKey(number)) {

                        LottoNumber lottoNumber = new LottoNumber();
                        lottoNumber.setLottoNumberHits(1);
                        lottoNumber.setLottoNumberGamesOut(0);

                        lottoNumData.put(number, lottoNumber);

                        incrementLottoNumberGamesOut(lottoNumData, number);

                    } else {
                        LottoNumber numData = lottoNumData.get(number);
                        int hitss = numData.getLottoNumberHits();
                        numData.setLottoNumberHits(++hitss);

                        List<Integer> p = numData.getOutHolder();
                        p.add(numData.getLottoNumberGamesOut());

                        numData.setLottoNumberGamesOut(0);

                        incrementLottoNumberGamesOut(lottoNumData, number);
                    }

                    incrementGamesOut(multipleHitOccurenceHolder, key);

                }

                break;
            }

        }
    }

    private void incrementLottoNumberGamesOut(Map<Integer, LottoNumber> data, int winningNumber) {

        data.forEach((k, v) -> {

            if (k != winningNumber) {
                int gamesOut = v.getLottoNumberGamesOut();
                v.setLottoNumberGamesOut(++gamesOut);
            }
        });
    }

    /**
     * Will increment the games out for all non winning multiples
     */
    private void incrementGamesOut(Map<Integer, NumberMultipleAnalyzer> data, int winningMultiple) {

        data.forEach((k, v) -> {

            if (k != winningMultiple) {
                int gamesOut = v.getMultipleGamesOut();
                v.setMultipleGamesOut(++gamesOut);
            }
        });
    }

    public void computeHitsAtGamesOutAndLastAppearance() {

        multipleHitOccurenceHolder.forEach((k, v) -> {

            List<Integer> gameOutHolderr = v.getGameOutHolder();

            long val = gameOutHolderr.stream().filter(i -> i == v.getMultipleGamesOut()).count();
            v.setMultipleHitsAtGamesOut((int) val);

            int lastAppear = Math.abs(gameOutHolderr.size() - gameOutHolderr.lastIndexOf(v.getMultipleGamesOut()));
            v.setMultipleGameOutLastAppearance(lastAppear);

            v.lottoNumberHitOccurenceHolder.forEach((key,value) -> {

                long valTwo = value.getOutHolder().stream().filter( i -> i == value.getLottoNumberGamesOut()).count();
                value.setHitsAtGamesOut((int)valTwo);

                int lastAppearTwo = Math.abs(value.getOutHolder().size() - value.getOutHolder().lastIndexOf(value.getLottoNumberGamesOut()));
                value.setOutLastSeen(lastAppearTwo);
            });
        });

    }


    /*
     *
     * Method populates the range buckets into certain multiple buckets 
     */
    private void popluateRangeBuckets(int min, int max) {

        List<Integer> gameNumRangeHolder = new ArrayList<>();

        for (int i = min; i <= max; i++) {
            gameNumRangeHolder.add(i);
        }

        final Set<Integer> set = new LinkedHashSet<>(multipleRanges.keySet());

        for (int num : gameNumRangeHolder) {

            for (Iterator<Integer> multiple = set.iterator(); multiple.hasNext(); ) {

                int mult = multiple.next();
                int remainder = num % mult;
                if (remainder == 0) {

                    List<Integer> holder = multipleRanges.get(mult);
                    holder.add(num);
                    break;
                }
            }
        }
    }

    private class LottoNumber{
        private int lottoNumberHits;
        private int lottoNumberGamesOut;
        private int hitsAtGamesOut;
        private int outLastSeen;
        private List<Integer> outHolder = new ArrayList<>();

        void setLottoNumberHits(int hits){
            this.lottoNumberHits = hits;
        }
        void setLottoNumberGamesOut(int gamesOut){
            this.lottoNumberGamesOut = gamesOut;
        }
        void setHitsAtGamesOut(int hits){
            this.hitsAtGamesOut = hits;
        }
        void setOutLastSeen(int val){
            this.outLastSeen = val;
        }
        int getLottoNumberHits(){
            return lottoNumberHits;
        }
        int getLottoNumberGamesOut(){
            return lottoNumberGamesOut;
        }
        int getHitsAtGamesOut(){
            return hitsAtGamesOut;
        }
        int getOutLastSeen(){
            return outLastSeen;
        }

        List<Integer> getOutHolder() {
            return outHolder;
        }
    }
}