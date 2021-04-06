package ServicePrint;


public class Template {
    public String CaseTemplate (String fileName, String  environment, String address) throws Exception {
        Requests ismsRequest = new Requests();
        return ismsRequest.RequestMethod(fileName, environment, address);
    }
}
