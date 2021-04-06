package ServicePrint.Utilities;

import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SoapResponse<T> {
    private T body;
    private Map<String, Object> httpHeaders = new HashMap<>();

    private boolean isFault;
    private String faultCode;
    private String faultString;

    public SoapResponse(T body, MimeHeaders headers) {
        this.body = body;

        Iterator it = headers.getAllHeaders();
        while (it.hasNext()) {
            MimeHeader mimeHeader = (MimeHeader) it.next();
            this.httpHeaders.put(mimeHeader.getName(), mimeHeader.getValue());
        }
    }

    public SoapResponse(String faultCode, String faultString, MimeHeaders headers) {
        this(null, headers);
        this.isFault = true;
        this.faultCode = faultCode;
        this.faultString = faultString;
    }

    public T getBody() {
        return body;
    }

    public boolean isFault() {
        return isFault;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public Map <String, Object> getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public String toString() {
        return SoapResponse.class.getSimpleName() + '{' +
                "body=" + body +
                ", httpHeaders=" + httpHeaders +
                ", isFault=" + isFault +
                ", faultCode='" + faultCode + '\'' +
                ", faultString='" + faultString + '\'' +
                '}';
    }
}