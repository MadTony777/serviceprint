package ServicePrint;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

public class UnitTests extends Requests{
    private Template template = new Template();

    @BeforeEach
    public void executedBeforeEach(TestInfo testInfo) {
        log.info("Starting test: "+ testInfo.getDisplayName());
    }

    @AfterEach
    public void executedAfterEach() {
        log.info("End test\n");
    }

    @Test
    public void PrintService_Proxy_Code_COMPLETED() throws Exception {
        switch (environment){
            case "test":
                log.info("does not exist on test");
                assertThat("does not exist on test", containsString( "does not exist on test"));
                break;
            case "stage":
                String ismsResponce = template.CaseTemplate("PrintCode.xml", environment, "proxy");
                assertThat(ismsResponce, containsString( "Access denied or invalid request format"));
        }
    }


    @Test
    public void PrintService_Proxy_Stream_COMPLETED() throws Exception {
        switch (environment){
            case "test":
                log.info("does not exist on test");
                assertThat("does not exist on test", containsString( "does not exist on test"));
                break;
            case "stage":
                String ismsResponce = template.CaseTemplate("PrintStream.xml", environment, "proxy");
                assertThat(ismsResponce, containsString( "Message>ServiceUtilities.Response.Error</Message"));
                assertThat(ismsResponce, containsString( "FileByteStream>AA==</FileByteStream"));
        }
    }


    @Test
    public void PrintService_Proxy_XML_COMPLETED() throws Exception {
        switch (environment){
            case "test":
                log.info("does not exist on test");
                assertThat("does not exist on test", containsString( "does not exist on test"));
                break;
            case "stage":
                String ismsResponce = template.CaseTemplate("PrintXML.xml", environment, "proxy");
                assertThat(ismsResponce, containsString( "Status>Success</Status"));
                assertThat(ismsResponce, containsString( "LogId>-1</LogId"));
        }
    }


    @Test
    public void PrintService_Print3_Code_COMPLETED() throws Exception {
        String ismsResponce = template.CaseTemplate("PrintCode.xml", environment, "print3");
        assertThat(ismsResponce, containsString( "Access denied or invalid request format"));
    }


    @Test
    public void PrintService_Print3_Stream_COMPLETED() throws Exception {
        String ismsResponce = template.CaseTemplate("PrintStream.xml", environment, "print3");
        assertThat(ismsResponce, containsString( "Message>ServiceUtilities.Response.Error</Message"));
        assertThat(ismsResponce, containsString( "FileByteStream>AA==</FileByteStream"));
    }


    @Test
    public void PrintService_Print3_XML_COMPLETED() throws Exception {
        String ismsResponce = template.CaseTemplate("PrintXML.xml", environment, "print3");
        assertThat(ismsResponce, containsString( "Status>Success</Status"));
        assertThat(ismsResponce, containsString( "LogId>-1</LogId"));
    }


    @Test
    public void PrintService_Print2_Stream_COMPLETED() throws Exception {
        String ismsResponce = template.CaseTemplate("PrintStream.xml", environment, "print2");
        assertThat(ismsResponce, containsString( "Message>ServiceUtilities.Response.Error</Message"));
        assertThat(ismsResponce, containsString( "FileByteStream>AA==</FileByteStream"));
    }


    @Test
    public void PrintService_Print2_XML_COMPLETED() throws Exception {
        String ismsResponce = template.CaseTemplate("PrintXML.xml", environment, "print2");
        assertThat(ismsResponce, containsString( "Status>Success</Status"));
        assertThat(ismsResponce, containsString( "LogId>-1</LogId"));
    }

}
