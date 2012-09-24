package so.simo.bluetest;

import com.beust.jcommander.JCommander;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;

public class BlueTest {
  public static final UUID SERVICE_UUID = new UUID("e2d76134421f41dd84c5712b94f5c8c7", false);

  public static void main(String[] args) throws InterruptedException, IOException {

    final Semaphore semaphore = new Semaphore(1);
    final List<RemoteDevice> remoteDevices = Lists.newArrayList();

    DiscoveryListener listener = new DiscoveryListener() {
      @Override public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        try {
          System.out
              .println(
                  "remoteDevice.getFriendlyName(true) = " + remoteDevice.getFriendlyName(true));
        } catch (IOException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("remoteDevice = " + remoteDevice);
        System.out.println("deviceClass = " + deviceClass);
        remoteDevices.add(remoteDevice);
      }

      @Override public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
        System.out.println("i = " + i);
        for (ServiceRecord sr : serviceRecords) {
          System.out.println("serviceRecords = " + sr);
        }
      }

      @Override public void serviceSearchCompleted(int i, int i1) {
        System.out.println("service search completed");
        semaphore.release();
      }

      @Override public void inquiryCompleted(int i) {
        System.out.println("inquiry completed");
        semaphore.release();
      }
    };

    //UUID[] uuids = {SERVICE_UUID};
    //
    //RemoteDevice[] remoteDevices1 =
    //    LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);
    //for (int i = 0; i < remoteDevices1.length; i++) {
    //  System.out.println("remoteDevices1 = " + remoteDevices1[i]);
    //  LocalDevice.getLocalDevice()
    //      .getDiscoveryAgent()
    //      .searchServices(null, uuids, remoteDevices1[i], listener);
    //}

    String foo = LocalDevice.getLocalDevice().getDiscoveryAgent().selectService(SERVICE_UUID, ServiceRecord.AUTHENTICATE_ENCRYPT, false);
    System.out.println("foo = " + foo);

    //semaphore.acquire();
    //LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
    //semaphore.acquire();
    //semaphore.release();
    //
    //UUID[] uuids = {new UUID(0x1106)};
    //for (RemoteDevice rd : remoteDevices) {
    //  semaphore.acquire();
    //  LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(null, uuids, rd, listener);
    //  semaphore.acquire();
    //  semaphore.release();
    //}
  }
}
