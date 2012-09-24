package so.simo.blauthenticator.client;

import com.google.common.base.Throwables;
import java.util.concurrent.CountDownLatch;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/** Searches all paired devices for the blauth service and prints their mac addresses. */
public class Seacher {
  public static final UUID[] UUIDS = {new UUID("e2d76134421f41dd84c5712b94f5c8c7", false)};

  public void search() {

    try {
      System.out.println("Searching for devices with blauth...");

      RemoteDevice[] pairedDevices =
          LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);


      for (RemoteDevice pairedDevice : pairedDevices) {
        final CountDownLatch latch = new CountDownLatch(1);
        int search = LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(null, UUIDS, pairedDevice, new DiscoveryListener() {
          @Override public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {}
          @Override public void inquiryCompleted(int discType) {}

          @Override public void serviceSearchCompleted(int transID, int respCode) { latch.countDown(); }

          @Override public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            RemoteDevice rd = servRecord[0].getHostDevice();
            System.out.println(rd.getBluetoothAddress());
          }
        });

        latch.await();
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
}
