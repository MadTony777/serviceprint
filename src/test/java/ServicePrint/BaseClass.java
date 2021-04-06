package ServicePrint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseClass {
    public static final String url_stg_Print3Lan = "http://192.168.65.233:8505/proxy/print";
    public static final String url_tst_Print3Lan = "http://192.168.65.190:8505/proxy/print";

    public static final String url_stg_Print3DMZ = "http://192.168.79.183:8505/proxy/print";
    public static final String url_tst_Print3DMZ = "http://esbext-test:8505/proxy/print";

    public static final String url_stg_Print2Lan = "http://esb-stage:8501/cxf/print2";
    public static final String url_tst_Print2Lan = "http://esb-test01:8181/cxf/print2";

    public static final String path = "src/test/java/ServicePrint/Examples/";

    public static final String soapActionPrintStream = "http://tempuri.org/IStreamService/PrintStream";
    public static final String soapActionPrintCode = "http://www.vsk.ru/IPrintService/PrintCode";
    public static final String soapActionPrintXML = "http://www.vsk.ru/IPrintService/PrintXml";


    private String arg = System.getProperty("arg", "stage");
    public String environment = arg;
    public static Logger log = LoggerFactory.getLogger(UnitTests.class);



    public static String getURL(String environment, String address) {
        String url = null;
        switch (environment) {
            case "stage":
                switch (address) {
                    case "print2":
                        url = url_stg_Print2Lan;
                        break;
                    case "print3":
                        url = url_stg_Print3Lan;
                        break;
                    case "proxy":
                        url = url_stg_Print3DMZ;
                        break;
                }
                break;
            case "test":
                switch (address) {
                    case "print2":
                        url = url_tst_Print2Lan;
                        break;
                    case "print3":
                        url = url_tst_Print3Lan;
                        break;
                    case "proxy":
                        url = url_tst_Print3DMZ;
                }
        }
        return url;
    }
    public static String getSOAPACTION(String fileName) {
        String SoapAction = "";
        switch (fileName) {
            case "PrintXML.xml":
                SoapAction = soapActionPrintXML;
                break;
            case "PrintCode.xml":
                SoapAction = soapActionPrintCode;
                break;
            case "PrintStream.xml":
                SoapAction = soapActionPrintStream;
        }
        return SoapAction;
    }

}
