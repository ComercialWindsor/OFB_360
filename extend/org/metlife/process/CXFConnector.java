package org.metlife.process;


import java.io.StringReader;
import java.util.List;
 
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
 
/**
 *
 * @author Charles Souillard
 *
 */
public class CXFConnector {
 
  private String targetNS;
  private String serviceName;
  private String portName;
  private String request;
  private String endpointAddress;
  private String binding;
  private String soapAction;
 
  private Source response;
 
  
  protected void executeConnector() throws Exception {
    final QName serviceQName = new QName(targetNS, serviceName);
    final QName portQName = new QName(targetNS, portName);
    final Service service = Service.create(serviceQName);
    service.addPort(portQName, binding, endpointAddress);
    final Dispatch<Source> dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);
   
    if (soapAction != null) {
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
    }
   
    this.response = dispatch.invoke(new StreamSource(new StringReader(request)));
  }
 
  public Source getResponse() {
    return response;
  }
 
  public void setEndpointAddress(String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }
 
  public void setBinding(String binding) {
    this.binding = binding;
  }
 
  public void setTargetNS(String targetNS) {
    this.targetNS = targetNS;
  }
 
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
 
  public void setPortName(String portName) {
    this.portName = portName;
  }
 
  public void setRequest(String request) {
    this.request = request;
  }
 
  public void setSoapAction(String soapAction) {
    this.soapAction = soapAction;
  }
}