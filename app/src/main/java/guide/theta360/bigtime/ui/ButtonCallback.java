package guide.theta360.bigtime.ui;

import android.util.Log;
import android.view.KeyEvent;

import com.theta360.pluginlibrary.callback.KeyCallback;
import com.theta360.pluginlibrary.receiver.KeyReceiver;

import org.theta4j.webapi.Theta;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ButtonCallback implements KeyCallback {

    private Theta theta = Theta.createForPlugin();
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private int delay = 10000;
    private int currentPicture = 0;
    private int maxPicture = 4;
    final String TAG = "THETA";

    @Override
    public void onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyReceiver.KEYCODE_CAMERA) {
            executor.submit(() -> {
                try {
                    while (currentPicture < maxPicture) {
                        Log.d(TAG, "take picture");
                        theta.takePicture();
                        Thread.sleep(delay);
                        currentPicture = currentPicture + 1;
                    }
                    currentPicture = 0;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    @Override
    public void onKeyUp(int keyCode, KeyEvent keyEvent) {

    }

    @Override
    public void onKeyLongPress(int keyCode, KeyEvent keyEvent) {

    }
}
