package guide.theta360.bigtime;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.theta360.pluginlibrary.activity.PluginActivity;
import com.theta360.pluginlibrary.callback.KeyCallback;
import com.theta360.pluginlibrary.receiver.KeyReceiver;
import com.theta360.pluginlibrary.values.LedColor;
import com.theta360.pluginlibrary.values.LedTarget;

import org.theta4j.webapi.Theta;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import guide.theta360.bigtime.ui.ButtonCallback;

public class MainActivity extends PluginActivity {
    final String TAG = "THETA";
    private ButtonCallback buttonCallback = new ButtonCallback();
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAutoClose(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setKeyCallback(buttonCallback);

        if (isApConnected()) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        notificationLedHide(LedTarget.LED3);
        setKeyCallback(null);
        executor.shutdown();
        executor.shutdownNow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public class ButtonCallback implements KeyCallback {

        private Theta theta = Theta.createForPlugin();


        private int delay = 10000;
        private int currentPicture = 0;
        private int maxPicture = 4;
        final String TAG = "THETA";

        @Override
        public void onKeyDown(int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyReceiver.KEYCODE_CAMERA) {
                notificationLed3Show(LedColor.MAGENTA);

                executor.submit(() -> {
                    try {
                        while (currentPicture < maxPicture) {
                            Log.d(TAG, "take picture");
                            notificationLedBlink(LedTarget.LED3, LedColor.MAGENTA, 1000);

                            Thread.sleep(delay - delay/4);
                            notificationLedBlink(LedTarget.LED3, LedColor.MAGENTA, 500);
                            notificationAudioSelf();
                            Thread.sleep(delay/8);
                            notificationLedBlink(LedTarget.LED3, LedColor.MAGENTA, 200);
                            Thread.sleep(delay/8);
                            theta.takePicture();
                            currentPicture = currentPicture + 1;
                        }
                        notificationLedHide(LedTarget.LED3);
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
}
