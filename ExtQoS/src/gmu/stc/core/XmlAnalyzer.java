/**
 * 
 */
package gmu.stc.core;


import org.jdom2.Element;

/**
 * @author kailiu
 *
 */
public class XmlAnalyzer {

	/**
	 * 
	 */
	Element e;
	String serviceType;
	public XmlAnalyzer(Element e, String serviceType) {
		// TODO Auto-generated constructor stub
		this.e = e;
		this.serviceType = serviceType;
	}

	public String getServiceId(){
		String id = null;
		try{
			Element e1 = e.getChild("fileIdentifier", MyNameSpace.NAMESPACE_GMD);
			id = e1.getChildText("CharacterString",MyNameSpace.NAMESPACE_GCO);
		}catch(NullPointerException ex){
			return null;
		}
		return id;
	}
	
	
	
	public String getServiceRecord(){
		String cleanURL = null;
		try{
			Element e1 = e.getChild("distributionInfo", MyNameSpace.NAMESPACE_GMD);
			Element e2 = e1.getChild("MD_Distribution", MyNameSpace.NAMESPACE_GMD);
			Element e3 = e2.getChild("transferOptions", MyNameSpace.NAMESPACE_GMD);
			Element e4 = e3.getChild("MD_DigitalTransferOptions",MyNameSpace.NAMESPACE_GMD);
			Element e5 = e4.getChild("onLine",MyNameSpace.NAMESPACE_GMD);
			Element e6 = e5.getChild("CI_OnlineResource",MyNameSpace.NAMESPACE_GMD);
			Element eLinkage = e6.getChild("linkage",MyNameSpace.NAMESPACE_GMD);
			Element eProtocol = e6.getChild("protocol",MyNameSpace.NAMESPACE_GMD);
			String linkageURL = eLinkage.getChildText("URL",MyNameSpace.NAMESPACE_GMD);
			String protocol = eProtocol.getChildText("CharacterString",MyNameSpace.NAMESPACE_GCO);
			
			if(linkageURL.toUpperCase().contains(serviceType.toUpperCase()) || 
					protocol.toUpperCase().contains(serviceType.toUpperCase())){
				cleanURL = cleanURL(linkageURL);
			}else{
				Element el1 = e.getChild("identificationInfo", MyNameSpace.NAMESPACE_GMD);
				Element el2 = el1.getChild("SV_ServiceIdentification", MyNameSpace.NAMESPACE_SRV);
				Element el3 = el2.getChild("serviceType",MyNameSpace.NAMESPACE_SRV);
				String st = el3.getChildText("LocalName",MyNameSpace.NAMESPACE_GCO);
				if(st.toUpperCase().contains(serviceType.toUpperCase())){
					cleanURL = cleanURL(linkageURL);
				}
					
			}
			return cleanURL;}
		catch(NullPointerException ex){
			return null;
		}
	}
	
	public String getIdentifier(){
		String id = null;
		try{
			Element e1 = e.getChild("fileIdentifier", MyNameSpace.NAMESPACE_GMD);
			id = e1.getChildText("CharacterString",MyNameSpace.NAMESPACE_GCO);
			return id;
		}
		catch(NullPointerException ex){
			return null;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	
	public static String cleanURL(String linkageURL){
		linkageURL = linkageURL.contains("?")?linkageURL.trim().substring(0,linkageURL.indexOf("?")):linkageURL;
		linkageURL = linkageURL.trim();
		if(linkageURL.endsWith("&"))
			linkageURL = linkageURL.substring(0,linkageURL.length()-1);
		return linkageURL;
	}

}
