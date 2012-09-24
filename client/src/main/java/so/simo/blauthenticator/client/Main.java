package so.simo.blauthenticator.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import sun.awt.windows.ThemeReader;

public class Main {

  public static void main(String[] args) {
    JCommander jc = new JCommander();
    jc.setProgramName("blauth-client");
    SeachParams seachParams = new SeachParams();
    jc.addCommand("search", seachParams);
    GetParams getParams = new GetParams();
    jc.addCommand("get", getParams);

    jc.parse(args);
    String command = jc.getParsedCommand();
    if (command.equals("search")) {
      new Seacher().search();
    } else if (command.equals("get")) {
      new Getter(getParams).get();
    } else {
      jc.usage();
    }
  }

  @Parameters(commandDescription = "get a OTP from a device")
  public static class GetParams {
    @Parameter(names = "-mac", required = true, description = "the device to retrieve from")
    public String mac;

    @Parameter(names = "-otp", required = true, description = "the OTP to retrieve")
    public String otpName;
  }

  @Parameters(commandDescription = "search for blauth devices")
  private static class SeachParams {
  }
}
