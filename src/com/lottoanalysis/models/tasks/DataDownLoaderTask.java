package com.lottoanalysis.models.tasks;

import com.lottoanalysis.controllers.LottoInfoAndGamesController;
import com.lottoanalysis.models.lottogames.data.LotteryGameDaoImpl;
import javafx.concurrent.Task;
import com.lottoanalysis.utilities.fileutilities.FileTweaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Class will be responsible for performing extensive work that requires additonal time to run
 * this will allow the UI to remain responsive
 */

public class DataDownLoaderTask extends Task<Void> {

    // Url field
    private Map<String, String> filePathsAndLotteryNames;
    private LottoInfoAndGamesController controller;

    int lineSkipCount = 1;
    BufferedReader br = null;
    BufferedWriter bw = null;

    public DataDownLoaderTask(Map<String, String> data, LottoInfoAndGamesController controller) {
        filePathsAndLotteryNames = data;
        this.controller = controller;
    }

    @Override
    protected Void call() throws Exception {

        System.out.print(Thread.currentThread().getName());
        updateMessage("Gathering Game Files From Internet...");

        int n;
        long nread = 0L;
        long fileLength = 0L;
        byte[] buf = new byte[8192];

        for (Map.Entry<String, String> data : filePathsAndLotteryNames.entrySet()) {

            URLConnection connection = new URL(data.getValue()).openConnection();
            fileLength += connection.getContentLength();
        }

        controller.makeVboxVisible();

        for (Map.Entry<String, String> data : filePathsAndLotteryNames.entrySet()) {

            // update lotto status label
            updateMessage("Updating " + data.getKey() + " Lotto Files");

            // Open url connection
            URLConnection connection = new URL(data.getValue()).openConnection();

            // use try block as it will automatically close all streams
            try (InputStream is = connection.getInputStream();
                 OutputStream os = Files.newOutputStream(Paths.get(data.getKey() + ".txt"))) {

                while ((n = is.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, fileLength);
                }

                // Remove the unwanted file heading at the top of the txt files
                updateMessage("Removing Unwanted Header From " + data.getKey() + " File");
                FileTweaker.overWriteFile(data.getKey());
            }

        }
        return null;
    }

    @Override
    protected void failed() {

    }

    @Override
    protected void succeeded() {

        Task repository = new LotteryGameDaoImpl(controller);

        controller.lotteryUpdateLabel.textProperty().bind(repository.messageProperty());

        Thread thread = new Thread(repository);
        thread.setDaemon(true);
        thread.start();


    }
}