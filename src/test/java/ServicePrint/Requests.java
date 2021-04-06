package ServicePrint;

import ServicePrint.Utilities.SoapClient;
import ServicePrint.Utilities.SoapClientBuilder;
import ServicePrint.Utilities.SoapRequest;
import ServicePrint.Utilities.SoapResponse;
import net.shibboleth.utilities.java.support.service.ServiceException;

import javax.xml.soap.SOAPException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;

public class Requests extends BaseClass{
    public static SoapClient soapClient;


    public String RequestMethod(String fileName, String environment, String address) throws Exception {

        String SoapAction = getSOAPACTION(fileName);
        String URL = getURL(environment, address);
        try {
            switch (address) {
                case "print2":
                    soapClient = SoapClientBuilder.create().build();
                    break;
                case "print3":
                case "proxy":
                    soapClient = SoapClientBuilder.create().withDigestAuth("prttest", "1q2w3e4r").build();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rightBody = new String(Files.readAllBytes(Paths.get(path + fileName)), StandardCharsets.UTF_8);
        log.info("Requests: " + rightBody);
        String result1="";
        try{
            SoapResponse<String> response = soapClient.call(new SoapRequest(URL, SoapAction, rightBody));
            String result = response.getFaultCode() + " " + response.getFaultString() + " " + response.getBody();
            System.out.println(response);
            soapClient.close();
            result1 = result;
        } catch (ServiceException | RemoteException | SOAPException | MalformedURLException e) {
            e.printStackTrace();
        }
        return result1;
    }
}
