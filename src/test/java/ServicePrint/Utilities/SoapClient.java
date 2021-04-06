package ServicePrint.Utilities;

import org.apache.wss4j.common.WSS4JConstants;
import org.apache.wss4j.dom.WSConstants;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.Closeable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;


public class SoapClient implements Closeable {

    private static final QName SECURITY_HEADER = new QName(WSConstants.WSSE_NS, WSConstants.WSSE_LN);
    private static final QName SECURITY_USERNAME_TOKEN = new QName(WSConstants.WSSE_NS, WSConstants.USERNAME_TOKEN_LN);
    private static final QName SECURITY_USERNAME = new QName(WSConstants.WSSE_NS, WSConstants.USERNAME_LN);
    private static final QName SECURITY_PASSWORD = new QName(WSConstants.WSSE_NS, WSConstants.PASSWORD_LN);
    private static final QName SECURITY_NONCE = new QName(WSConstants.WSSE_NS, WSConstants.NONCE_LN);
    private static final QName SECURITY_PASSWORD_TYPE_ATTR = new QName(WSConstants.PASSWORD_TYPE_ATTR);
    private static final QName SECURITY_CREATED = new QName(WSConstants.WSU_NS, WSConstants.CREATED_LN);


    private SOAPConnection soapConnection;

    private boolean throwFault = false;

    private boolean useWsSecurity = false;
    private WsPasswordType wsPasswordType = WsPasswordType.PASSWORD_DIGEST;
    private String wsUsername;
    private String wsPassword;


    public SoapClient() throws SOAPException {
        this.soapConnection = SOAPConnectionFactory.newInstance().createConnection();
    }

    public void setThrowFault(boolean throwFault) {
        this.throwFault = throwFault;
    }

    public void setUseWsSecurity(boolean useWsSecurity) {
        this.useWsSecurity = useWsSecurity;
    }

    public void setWsPasswordType(WsPasswordType wsPasswordType) {
        this.wsPasswordType = wsPasswordType;
    }

    public void setWsUsername(String wsUsername) {
        this.wsUsername = wsUsername;
    }

    public void setWsPassword(String wsPassword) {
        this.wsPassword = wsPassword;
    }


    public SoapResponse<String> call(SoapRequest soapRequest) throws Exception {
        String url = Objects.requireNonNull(soapRequest.getUrl(), "url must be not null");
        String soapBody = Objects.requireNonNull(soapRequest.getEntity(), "soapBody must be not null");
        SOAPMessage soapRequestMessage = createEnvelopeSoapMessage(convertStringToDocument(soapBody),
                soapRequest.getHttpHeaders(), soapRequest.getSoapHeaders());
        return call(url, soapRequestMessage);
    }

    public SoapResponse<String> call(String url, SOAPMessage soapRequestMessage) throws Exception {
        SOAPMessage soapMessageResponse = this.soapConnection.call(soapRequestMessage, url);
        MimeHeaders mimeHeaders = soapMessageResponse.getMimeHeaders();
        if (soapMessageResponse.getSOAPBody().hasFault()) {
            if (this.throwFault) {
                throw new SOAPFaultException(soapMessageResponse.getSOAPBody().getFault());
            }
            SOAPFault soapFault = soapMessageResponse.getSOAPBody().getFault();
            return new SoapResponse<>(soapFault.getFaultCode(), soapFault.getFaultString(), mimeHeaders);
        } else {
            Document responseBody = soapMessageResponse.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            return new SoapResponse<>(convertDocumentToString(responseBody), mimeHeaders);
        }
    }

    public static Document convertStringToDocument(String xmlStr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlStr)));
    }


    public static String convertDocumentToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc),
                new StreamResult(writer));    return writer.getBuffer().toString();}

    private SOAPMessage createEnvelopeSoapMessage(Document body, Map<String, String> httpHeaders,
                                                  Map<String, String> soapHeaders) throws SOAPException {

        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();

        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        soapEnvelope.getBody().addDocument(body);

        setSoapHeaders(soapEnvelope, soapHeaders);
        setHttpHeaders(soapMessage, httpHeaders);

        if (this.useWsSecurity) {
            if (this.wsPasswordType.equals(WsPasswordType.PASSWORD_DIGEST)) {
                setPasswordDigestWSSecurity(soapEnvelope);
            } else {
                throw new IllegalArgumentException("UnsupportedPasswordType");
            }
        }
        soapMessage.saveChanges();
        return soapMessage;
    }

    private void setHttpHeaders(SOAPMessage soapMessage, Map<String, String> headers) {
        MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
        headers.forEach(mimeHeaders::addHeader);
    }

    private void setSoapHeaders(SOAPEnvelope soapEnvelope, Map<String, String> headers) throws SOAPException {
        SOAPHeader soapHeader = soapEnvelope.getHeader();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            QName headerName = new QName(WSS4JConstants.URI_SOAP11_ENV, header.getKey());
            soapHeader.addHeaderElement(headerName).addTextNode(header.getValue());
        }
    }

    private void setPasswordDigestWSSecurity(SOAPEnvelope soapEnvelope) throws SOAPException {
        AuthDigest authDigest = new AuthDigest(this.wsPassword);
        SOAPElement securityHeader = soapEnvelope.getHeader().addHeaderElement(SECURITY_HEADER);
        SOAPElement securityHeaderUsernameToken = securityHeader.addChildElement(SECURITY_USERNAME_TOKEN);

        securityHeaderUsernameToken.addChildElement(SECURITY_USERNAME)
                .addTextNode(this.wsUsername);
        securityHeaderUsernameToken.addChildElement(SECURITY_PASSWORD)
                .addTextNode(authDigest.getPasswordDigest())
                .addAttribute(SECURITY_PASSWORD_TYPE_ATTR, WSConstants.PASSWORD_DIGEST);
        securityHeaderUsernameToken.addChildElement(SECURITY_NONCE)
                .addTextNode(authDigest.getNonce())
                .addAttribute(new QName("EncodingType"), WSConstants.BASE64_ENCODING);
        securityHeaderUsernameToken.addChildElement(SECURITY_CREATED)
                .addTextNode(authDigest.getCreatedDate());
    }

    @Override
    public void close() {
        try {
            soapConnection.close();
        } catch (SOAPException ignored) {
        }
    }

    public enum WsPasswordType {
        PASSWORD_DIGEST
    }
}