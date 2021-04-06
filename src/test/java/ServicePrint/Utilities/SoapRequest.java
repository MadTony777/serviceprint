package ServicePrint.Utilities;

import org.opensaml.soap.wsaddressing.SoapAction;

import java.util.HashMap;
import java.util.Map;

public class SoapRequest {
    private String url;
    private String soapAction;
    private String entity;
    private Map<String, String> httpHeaders = new HashMap<>();
    private Map<String, String> soapHeaders = new HashMap<>();

    public SoapRequest(String url, String soapAction, String entity) {
        this.url = url;
        this.soapAction = soapAction;
        this.entity = entity;
        this.httpHeaders.put(SoapAction.ELEMENT_LOCAL_NAME, soapAction);
    }

    public SoapRequest(String url, String soapAction, String entity, Map<String, String> httpHeaders) {
        this.url = url;
        this.soapAction = soapAction;
        this.entity = entity;
        this.httpHeaders.putAll(httpHeaders);
        this.httpHeaders.put(SoapAction.ELEMENT_LOCAL_NAME, soapAction);
    }

    public SoapRequest(String url, String soapAction, String entity, Map<String, String> httpHeaders, Map<String, String> soapHeaders) {
        this.url = url;
        this.soapAction = soapAction;
        this.entity = entity;
        this.httpHeaders.putAll(httpHeaders);
        this.httpHeaders.put(SoapAction.ELEMENT_LOCAL_NAME, soapAction);
        this.soapHeaders.putAll(soapHeaders);
    }

    public String getUrl() {
        return url;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public String getEntity() {
        return entity;
    }

    public Map <String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map <String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public Map <String, String> getSoapHeaders() {
        return soapHeaders;
    }

    public void setSoapHeaders(Map <String, String> soapHeaders) {
        this.soapHeaders = soapHeaders;
    }

    @Override
    public String toString() {
        return SoapRequest.class.getSimpleName() + '{' +
                "url='" + url + '\'' +
                ", soapAction='" + soapAction + '\'' +
                ", entity='" + entity + '\'' +
                ", httpHeaders=" + httpHeaders +
                ", soapHeaders=" + soapHeaders +
                '}';
    }
}
