package so.simo.blauthenticator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MyActivity extends Activity {
  public static final UUID SERVICE_UUID = UUID.fromString("e2d76134-421f-41dd-84c5-712b94f5c8c7");

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Thread thread = new Thread(new Runnable() {
      @Override public void run() {
        try {
          BluetoothServerSocket serverSocket = BluetoothAdapter.getDefaultAdapter()
              .listenUsingRfcommWithServiceRecord("blauth", SERVICE_UUID);

          while (true) {
            BluetoothSocket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            int c = inputStream.read();
            while (c > 0) {
              outputStream.write(c);
              c = inputStream.read();
            }
            outputStream.write(0);
            socket.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    thread.run();

    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
