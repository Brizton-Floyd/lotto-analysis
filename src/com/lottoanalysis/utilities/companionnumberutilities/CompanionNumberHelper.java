package com.lottoanalysis.utilities.companionnumberutilities;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;

@SuppressWarnings("unchecked")
public class CompanionNumberHelper {

    private static Map<String,Object[]> patternHolder = new LinkedHashMap<>();
    private static Map<Integer,String> columnHeaders = new TreeMap<>();
    private static List<List<String>> listHolder = new LinkedList<>();
    private static Map<Integer,Integer[]> gameOutHolderMap = new LinkedHashMap<>();
    private static Map<Integer,Integer[]> lastDigitDueHolder = new TreeMap<>();
    private static Map<Integer,List<Map<Integer,Integer[]>>> gameOutFirstElemetHolder = new TreeMap<>();

    public static Map<Integer, Integer[]> getGameOutHolderMap() {
        return gameOutHolderMap;
    }
    public static Map<Integer, Integer[]> getLastDigitDueHolder() {
        return lastDigitDueHolder;
    }
    public static List<List<String>> getListHolder() {
        return listHolder;
    }
    public static Map<Integer, String> getColumnHeaders() {
        return columnHeaders;
    }
    public static Map<String, Object[]> getPatternHolder() {
        return patternHolder;
    }
    public static Map<Integer, List<Map<Integer, Integer[]>>> getGameOutFirstElemetHolder() {
        return gameOutFirstElemetHolder;
    }

    public static void analyze(int lottoDigit, List<Integer> currentPos, List<Integer> companionNumbers, List<Integer> pairingNumbers){

        clear();
        loadPatternIntoMap(lottoDigit, pairingNumbers);
        analyzePositionData(currentPos,companionNumbers,lottoDigit);
        populateFirstElementMap();
    }

    public static String[] stripBrackets( String value ){

        StringBuilder builder = new StringBuilder(value);
        builder.setCharAt(0,' ');
        builder.setCharAt(value.length() -1, ' ');

        String[] rangeValues = builder.toString().split(",");

        return rangeValues;
    }
    private static void populateFirstElementMap() {

        Set<Integer> keys = getGameOutHolderMap().keySet();
        for(Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();){

            String numAsString = iterator.next() + "";
            int elementOne = (numAsString.length() > 1) ? Character.getNumericValue( numAsString.charAt(0)) : 0;

            if(!gameOutFirstElemetHolder.containsKey(elementOne)){
                gameOutFirstElemetHolder.put(elementOne, new ArrayList<>());
            }
        }

        gameOutHolderMap.forEach( (k,v) -> {

            String numAsString = k + "";
            int elementOne = (numAsString.length() > 1) ? Character.getNumericValue( numAsString.charAt(0)) : 0;

            if(gameOutFirstElemetHolder.containsKey(elementOne)){

                List<Map<Integer,Integer[]>> data = gameOutFirstElemetHolder.get(elementOne);
                Map<Integer,Integer[]> d = new TreeMap<>();
                d.put(k, v);
                data.add( d );
            }

        });

        Comparator<Map<Integer,Integer[]>> comparator = (o1, o2) -> {

            Integer k1 = o1.keySet().iterator().next();
            Integer[] k1Data = o1.get(k1);

            Integer k2 = o2.keySet().iterator().next();
            Integer[] k2Data = o2.get(k2);

            int result = k1Data[0].compareTo( k2Data[0] );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}

        };

        gameOutFirstElemetHolder.forEach( (k,v) -> v.sort(comparator) );

    }

    @SuppressWarnings("unchecked")
    private static void analyzePositionData(List<Integer> currentPos, List<Integer> companionNumbers, int lottoDigit) {


        for(int i = 0; i < currentPos.size(); i++){

            if(currentPos.get(i) == lottoDigit){
                int num = companionNumbers.get(i);
                String pattern = lottoDigit + "-" + num;
                if(patternHolder.containsKey(pattern)){
                    Object[] data = patternHolder.get(pattern);
                    data[0] = (int) data[0] + 1;

                    if(!gameOutHolderMap.containsKey((Integer)data[1])){

                        gameOutHolderMap.put((Integer)data[1],new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(gameOutHolderMap,(int)data[1]);
                    }
                    else{
                        Integer[] dataaa = gameOutHolderMap.get((Integer)data[1]);
                        dataaa[0]++;
                        dataaa[1] = 0;
                        NumberAnalyzer.incrementGamesOut(gameOutHolderMap,(int)data[1]);
                    }

                    String outAsString = data[1] + "";
                    int digit = (outAsString.length() == 1) ? 0 :
                            Character.getNumericValue(outAsString.charAt(0));

                    if(!lastDigitDueHolder.containsKey(digit)){

                        lastDigitDueHolder.put(digit,new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(lastDigitDueHolder,digit);
                    }
                    else{
                        Integer[] lastDigitData = lastDigitDueHolder.get(digit);
                        lastDigitData[0]++;
                        lastDigitData[1] = 0;
                        NumberAnalyzer.incrementGamesOut(lastDigitDueHolder,digit);
                    }
                    data[1] = 0;

                    List<String> list = (LinkedList<String>)data[2];
                    String val = ((int)data[1] == 0) ? "X" : data[1] + "";
                    list.add(val);


                    NumberAnalyzer.incrementGamesOut(patternHolder, pattern);

                    patternHolder.forEach((k,v) -> {

                        if(!k.equals(pattern)){
                            ((List<String>)v[2]).add(v[1]+"");
                        }
                    });
                }
            }
        }

        patternHolder.forEach((k,v) -> {

            List<String> dd = (LinkedList<String>)v[2];
            addListToArray(dd);

            int currentGamesOut = (int)v[1];
            if(gameOutHolderMap.containsKey(currentGamesOut)){

                int hits = gameOutHolderMap.get(currentGamesOut)[0];
                int lastSeen = gameOutHolderMap.get(currentGamesOut)[1];

                v[3] = hits;
                v[4] = lastSeen;
            }
        });


    }

    private static void addListToArray(List<String> dd) {

        listHolder.add(dd);
    }

    private static void loadPatternIntoMap(int lottoDigit, List<Integer> pairingNumbers) {

        for(int i = 0; i < pairingNumbers.size(); i++)
        {
            String pattern = lottoDigit + "-" + pairingNumbers.get(i);
            patternHolder.put(pattern,new Object[]{0,0, new LinkedList<String>(),0,0});
            columnHeaders.put(i,pattern);
        }
    }

    private static void clear(){
        patternHolder.clear();
        listHolder.clear();
        columnHeaders.clear();
        lastDigitDueHolder.clear();
        getGameOutHolderMap().clear();
        gameOutFirstElemetHolder.clear();
    }
}
