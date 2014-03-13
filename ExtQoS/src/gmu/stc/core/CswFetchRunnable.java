package gmu.stc.core;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class CswFetchRunnable implements Runnable {
	
	private WebSite website;
	protected String taskname;
	protected String url;
	protected String type;
	protected String isprivate;
	protected String saveas;
	protected String hours;
	protected String id;
	protected String atomPath;
	public CswFetchRunnable(WebSite website, String id, String taskname, String url,
			String type, String isprivate, String saveas, String hours, String atomPath) {
		super();
		this.website = website;
		this.taskname = taskname;
		this.url = url;
		this.type = type;
		this.isprivate = isprivate;
		this.saveas = saveas;
		this.id = id;
		this.atomPath = atomPath;
		this.hours = hours;
		this.url = XmlAnalyzer.cleanURL(url);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.createAtom(id, atomPath);
	}

	public void createAtom(String id, String atomPath) {
		AtomGenerator atomgenerator = new AtomGenerator();
		try {
			System.out.println(this.website.getRootPath());
			atomgenerator.createAtomXMLFromArary(this.website, id, this.taskname, getServices(), atomPath);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private ArrayList<String> getServices() throws ClientProtocolException, IllegalStateException, IOException, JDOMException{
		List<String> lswms = getServiceRecords("WMS");
		List<String> lswfs = getServiceRecords("WFS");
		List<String> lswcs = getServiceRecords("WCS");
		List<String> lscsw = getServiceRecords("CSW");
		List<String> lswps = getServiceRecords("WPS");
		List<String> lssos = getServiceRecords("SOS");
		ArrayList<String> ls = new ArrayList<String>();
		ls.addAll(lswms);
		ls.addAll(lswfs);
		ls.addAll(lswcs);
		ls.addAll(lscsw);
		ls.addAll(lswps);
		ls.addAll(lssos);
		System.out.println(ls.size());
		return ls;
	}
	
	
	private List<String> getServiceRecords(String serviceType) throws ClientProtocolException, IOException, IllegalStateException, JDOMException{
		List<String> list = new LinkedList<String>();  
		String url = this.getUrl()+"?request=GetRecords&service=CSW&version=2.0.2&namespace=xmlns%28csw%3Dhttp%3A%2F%2F"+
				"www.opengis.net%2Fcat%2Fcsw%2F2.0.2%29%2Cxmlns%28gmd%3Dhttp%3A%2F%2Fwww.isotc211.org%2F2005%2Fgmd%29&"+
				"constraint=AnyText+like+%27" + serviceType + "%27" + "&constraintLanguage=CQL_TEXT&constraint_language_version=1.1.0"+
				"&typeNames=csw%3ARecord&resultType=results&outputSchema=http://www.isotc211.org/2005/gmd&ELEMENTSETNAME=full&startPosition=";
		String initUrl = url + "1";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(initUrl);
		System.out.println("\nSending 'GET' request to URL : " + url);
		CloseableHttpResponse response = client.execute(request);		
		System.out.println("Response Code : " + 
				response.getStatusLine().getStatusCode());
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(response.getEntity().getContent());
		Element rootNode = doc.getRootElement();
		Element sr = rootNode.getChild("SearchResults", MyNameSpace.NAMESPACE_CSW);
		System.out.println(sr.getAttributeValue("numberOfRecordsMatched"));

		int total = Integer.parseInt(sr.getAttributeValue("numberOfRecordsMatched"));
		response.close();

		int i = 1;
		while(i < total){

			String newUrl = url + Integer.toString(i);
			System.out.println(i);
			request = new HttpGet(newUrl);
			response = client.execute(request);

			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + 
					response.getStatusLine().getStatusCode());
			doc = builder.build(response.getEntity().getContent());
			rootNode = doc.getRootElement();
			sr = rootNode.getChild("SearchResults", MyNameSpace.NAMESPACE_CSW);
			for(Element e:sr.getChildren()){					
					XmlAnalyzer xmlanalyzer = new XmlAnalyzer(e, serviceType);
					String cleanURL = xmlanalyzer.getServiceRecord();
					if(null != cleanURL){
						String tail = "";
						if("WMS".equals(serviceType.toUpperCase()))
							tail="?service=wms&request=GetCapabilities";
						else if("WFS".equals(serviceType.toUpperCase()))
							tail="?service=wfs&request=GetCapabilities";
						else if("WCS".equals(serviceType.toUpperCase()))
							tail="?service=wcs&request=GetCapabilities";
						else if("CSW".equals(serviceType.toUpperCase()))
							tail="?service=csw&request=GetCapabilities";
						else if("WPS".equals(serviceType.toUpperCase()))
							tail="?service=wps&request=GetCapabilities";
						else if("SOS".equals(serviceType.toUpperCase()))
							tail="?service=sos&request=GetCapabilities";
						cleanURL += tail;
						list.add(cleanURL);
					}
					
			}
			i += 10;
		}
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
		
		return list;
	}

	
	
	
	private String getUrl() {
		// TODO Auto-generated method stub
		return this.url;
	}

	private void analyzeCapabilities(String capUrl) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(capUrl);
		Element rootNode = doc.getRootElement();
		Element operMd = rootNode.getChild("OperationsMetadata", MyNameSpace.NAMESPACE_OWS);
		if(operMd==null)
			System.out.println("Missing 'ows:OperationsMetadata' element");
		else{
			for(Element e:operMd.getChildren()){
				if("Operation".equals(e.getName())){
					String name = e.getAttributeValue("name");
					if("GetRecords".equals(name)){
						//Element
						//work later
					}
				}
			}
		}
	}

	private String getCapUrl(){
		return this.getUrl() + "?service=CSW&request=GetCapabilities&version=2.0.2";
	}
	
	public static void main(String[] args) {
		ScheduledExecutorService scheduler =
			    Executors.newScheduledThreadPool(1);
		WebSite ws = new WebSite("/Users/kailiu");
		ws.setRootPath("/Users/kailiu");
		CswFetchRunnable cr = new CswFetchRunnable(ws, "3", "csr", "http://geossregistries.info/geonetwork/srv/en/csw?Request=GetCapabilities&Service=CSW&Version=2.0.2", "csw", "false", "csr.xml", "5", "csr.xml");
		ScheduledFuture<?> beeperHandle =
			      scheduler.scheduleAtFixedRate(cr, 10, 200, SECONDS);

	}
}
