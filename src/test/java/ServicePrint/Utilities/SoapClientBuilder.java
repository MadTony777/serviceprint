package ServicePrint.Utilities;

import javax.xml.soap.SOAPException;

public class SoapClientBuilder {

    private boolean isThrowFault = false;
    private boolean useWsSecurity = false;
    private SoapClient.WsPasswordType wsPasswordType = SoapClient.WsPasswordType.PASSWORD_DIGEST;
    private String wsUsername;
    private String wsPassword;

    private SoapClientBuilder() {
    }

    public static SoapClientBuilder create() {
        return new SoapClientBuilder();
    }

    public SoapClientBuilder withThrownFault(boolean isThrowFault) {
        this.isThrowFault = isThrowFault;
        return this;
    }

    public SoapClientBuilder withDigestAuth(String wsUsername, String wsPassword) {
        this.useWsSecurity = true;
        this.wsPasswordType = SoapClient.WsPasswordType.PASSWORD_DIGEST;
        this.wsUsername = wsUsername;
        this.wsPassword = wsPassword;
        return this;
    }

    public SoapClient build() throws SOAPException {
        SoapClient soapClient = new SoapClient();
        soapClient.setThrowFault(this.isThrowFault);
        soapClient.setUseWsSecurity(this.useWsSecurity);
        soapClient.setWsPasswordType(this.wsPasswordType);
        soapClient.setWsUsername(this.wsUsername);
        soapClient.setWsPassword(this.wsPassword);
        return soapClient;
    }
}
