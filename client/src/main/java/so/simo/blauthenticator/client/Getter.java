package so.simo.blauthenticator.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/** Gets an OTP from device and prints it to the screen. */
public class Getter {
  private Main.GetParams getParams;

  public Getter(Main.GetParams getParams) {
    this.getParams = getParams;
  }

  public void get() {
    try {
      // find device with the mac
      RemoteDevice targetDevice = null;
      for (RemoteDevice pairedDevice : LocalDevice.getLocalDevice()
          .getDiscoveryAgent()
          .retrieveDevices(DiscoveryAgent.PREKNOWN)) {
        if (pairedDevice.getBluetoothAddress().equals(getParams.mac)) {
          targetDevice = pairedDevice;
          break;
        }
      }

      if (targetDevice == null) {
        System.out.println("Device " + getParams.mac + " was not paired.");
        System.exit(1);
      }

      // get service url. TODO: look into launching app via bluetooth
      final CountDownLatch latch = new CountDownLatch(1);
      final AtomicReference<String> serviceUri = new AtomicReference<String>();
      LocalDevice.getLocalDevice()
          .getDiscoveryAgent()
          .searchServices(null, Seacher.UUIDS, targetDevice, new DiscoveryListener() {
            @Override public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            @Override public void inquiryCompleted(int discType) {
            }

            @Override public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
              serviceUri.set(
                  servRecord[0].getConnectionURL(ServiceRecord.AUTHENTICATE_ENCRYPT, true));
            }

            @Override public void serviceSearchCompleted(int transID, int respCode) {
              latch.countDown();
            }
          });

      // wait to get info
      latch.await();

      if (serviceUri.get() == null) {
        System.out.println("Device is not running blauth!");
        System.exit(1);
      }

      // open socket
      System.out.println("serviceUri = " + serviceUri.get());
      StreamConnection connection = (StreamConnection) Connector.open(serviceUri.get());
      DataOutputStream outputStream = connection.openDataOutputStream();
      DataInputStream inputStream = connection.openDataInputStream();

      // write some stuff to it
      outputStream.writeBytes("Hello world");
      outputStream.write(0);

      // read from socket
      int c = inputStream.read();
      while (c > 0) {
        System.out.print((char) c);
        c = inputStream.read();
      }
      System.out.println();
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
