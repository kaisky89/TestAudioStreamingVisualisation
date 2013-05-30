/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testaudiostreaming;

import ddf.minim.*;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *
 * @author kaisky89
 */
public class PlayerController implements Initializable {

    @FXML
    private Text status;
    @FXML
    private Text properties;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField field_url;
    @FXML
    private Slider slider;
    @FXML
    private AreaChart chart;
    AudioPlayer audioPlayer;
    Minim minim;
    String stringStatus;
    XYChart.Series amplitudes;

    @FXML
    private void drawChart() {
        // TODO?
    }

    @FXML
    private void handleButtonLeft(ActionEvent event) {
//        Duration currentDuration = mediaView.getMediaPlayer().getCurrentTime();
//        //Duration newDuration = currentDuration.add(Duration.seconds(-10.0));
//        Duration newDuration = new Duration(-1000.0);
//        mediaView.getMediaPlayer().seek(newDuration);

        audioPlayer.skip(-10 * 1000);
    }

    @FXML
    private void handleButtonRight(ActionEvent event) {
//        Duration currentDuration = mediaView.getMediaPlayer().getCurrentTime();
//        Duration newDuration = currentDuration.add(Duration.seconds(10.0));
//        mediaView.getMediaPlayer().seek(newDuration);
        audioPlayer.skip(10 * 1000);
    }

    @FXML
    private void handleButtonProperties(ActionEvent event) {
//        Object object = mediaView.getMediaPlayer().getMedia().durationProperty();
//        
//        String string = "media.keySet: " + (object == null ? "null" : object.toString());
//        System.out.println(string);
//        properties.setText(string);
    }

    @FXML
    private void handleButtonLaden(ActionEvent event) {
        // clean up current players
        if (audioPlayer != null) {
            audioPlayer.close();
        }
        if (minim != null) {
            minim.stop();
        }

        audioPlayer = null;
        minim = null;



        // create new player from given url

        minim = new Minim(new Object() {
            public String sketchPath(String fileName) {
                return System.getProperty("user.dir") + "/" + fileName;
            }

            public InputStream createInput(String fileName) {
                System.out.println("createIntput(" + fileName + ");");
                return null;
            }
        });



        audioPlayer = minim.loadFile(field_url.getText(), 2050);

        stringStatus = "Datei geladen.";

        reloadStatus();

        amplitudes = new XYChart.Series();

        // initialize Listener for visualization
        audioPlayer.addListener(new AudioListenerImpl());
        // initialize chart for visualization
        chart.getData().add(amplitudes);


    }

    public void addChartValue(final int value) {
        final int size = amplitudes.getData().size();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                amplitudes.getData().add(new XYChart.Data<>(size, value));
            }
        });
    }

    @FXML
    private void handleButtonAktualisiereChart(ActionEvent event) {
    }

    @FXML
    private void handleButtonAbspielen(ActionEvent event) {
        audioPlayer.play();
    }

    @FXML
    private void handleButtonStop(ActionEvent event) {
        audioPlayer.pause();
        audioPlayer.rewind();
    }

    @FXML
    private void handleButtonShowStatus(ActionEvent event) {
        reloadStatus();
    }

    @FXML
    private void handleButtonPause(ActionEvent event) {
        audioPlayer.pause();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    private void reloadStatus() {
        //status.setText(mediaView.getMediaPlayer().getStatus().toString());
        status.setText(stringStatus);
    }

    private class AudioListenerImpl implements AudioListener {

        @Override
        public void samples(float[] samp) {
            float sum = 0;
            for (float f : samp) {
                sum += Math.abs(f);
            }
            int returnInt = Math.round(sum);
            addChartValue(returnInt);
        }

        @Override
        public void samples(float[] sampL, float[] sampR) {
            float sum = 0;
            for (float f : sampL) {
                sum += Math.abs(f);
            }
            int returnInt = Math.round(sum);
            addChartValue(returnInt);
        }
    }
}

class Inte {

    public int i;

    public Inte(int i) {
        this.i = i;
    }
}