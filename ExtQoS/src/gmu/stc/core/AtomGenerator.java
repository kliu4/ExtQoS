package gmu.stc.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author cisc2
 *
 */
public class AtomGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AtomGenerator ag =new AtomGenerator();
		try {
			ag.createAtomXML("D:/EclipseWorkspace/FgdcCheckerWithCLH/src/cisc/list.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	public void createAtomXML(String fileName) throws IOException{
		String xmlstring = this.buildAtomXML("geoclearinghouse", this.readServiceList(fileName));
		try {
			File newTextFile = new File("atom.xml");
			FileWriter fw = new FileWriter(newTextFile);
			fw.write(xmlstring);
			fw.close();

		} catch (IOException iox) {
			//do stuff with exception
			iox.printStackTrace();
		}
		System.out.println(xmlstring);
	}

	public void createAtomXMLFromArary(WebSite website, String id, String name, ArrayList<String> serviceList, String atomPathName) throws IOException{
		
		TaskManager tm = new TaskManager(website.getRootPath()+"entry.xml");
		Format formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime=formatter.format(date);
		tm.updateLastRun("admin", id, updateTime);

		String xmlstring = this.buildAtomXML(name, this.readServiceListFromArray(serviceList));
		try {
			File newTextFile = new File(atomPathName);
			FileWriter fw = new FileWriter(newTextFile);
			fw.write(xmlstring);
			fw.close();

		} catch (IOException iox) {
			//do stuff with exception
			iox.printStackTrace();
		}
		//System.out.println(xmlstring);
	}


	private String buildAtomXML(String serviceListName, ArrayList<ServiceEntry> list){
		//Date date = new Date();
		//String updateTime = date.toGMTString();
		Format formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateTime=formatter.format(date);

		String atom="<?xml version='1.0' encoding='UTF-8'?>"
				+"<feed xmlns='http://www.w3.org/2005/Atom' xmlns:gos='http://www.geodata.gov/gos_atom'>"
				+"<title>"+serviceListName+"</title>"
				+"<updated>"+updateTime+"</updated>";

		for(ServiceEntry entry:list){
			String entryXml= this.buildEntryXML(entry);
			atom = atom+entryXml;
		}
		atom = atom+"</feed>";

		return atom;

	}

	private ArrayList<ServiceEntry> readServiceList(String fileName) throws IOException{
		ArrayList<ServiceEntry> list = new ArrayList<ServiceEntry>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		while((line = br.readLine()) != null) {
			ServiceEntry entry = this.extractEntryFromUrl(line);
			if(entry.getServiceType()!=null)
				list.add(entry);
		}
		return list;
	}

	private ArrayList<ServiceEntry> readServiceListFromArray(ArrayList<String> serviceList) throws IOException{
		ArrayList<ServiceEntry> list = new ArrayList<ServiceEntry>();

		for(int i=0;i<serviceList.size();i++){
			ServiceEntry entry = this.extractEntryFromUrl(serviceList.get(i));
			if(entry.getServiceType()!=null)
				list.add(entry);
		}

		return list;
	}

	private ServiceEntry extractEntryFromUrl(String url){
		url = url.replace("&", "&amp;");
		ServiceEntry entry = new ServiceEntry();
		entry.setId(url);
		entry.setServiceUrl(url);
		entry.setTitle(url.split("\\?")[0]);

		Format formatter;
		Date date = new Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		entry.setUpdated(formatter.format(date));

		//http://geoserver.webservice-energy.org/geoserver/australia/australia_january/wms?service=WMS&version=1.1.0&request=GetCapabilities
		if(url.contains("WMS")||url.contains("wms")){
			entry.setServiceType("wms");
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=wms");

		}else if(url.contains("WCS")||url.contains("wcs")){
			entry.setServiceType("wcs");
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=wcs");

		}else if(url.contains("CSW")||url.contains("csw")){
			entry.setServiceType("csw");
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=csw");

		}else if(url.contains("SOS")||url.contains("sos")){
			entry.setServiceType("sos");
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=sos");

		}else if(url.contains("service=WFS")||url.contains("service=wfs")){
			entry.setServiceType("wfs");	
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=wfs");
		}
		else if(url.contains("service=WPS")||url.contains("service=wps")){
			entry.setServiceType("wps");	
			//entry.setServiceUrl(url+"?request=GetCapabilities&amp;service=wps");
		}
		// System.out.println(url);


		return entry;
	}

	private String buildEntryXML(ServiceEntry entry){
		String xml = "<entry>"
				+"<id>"+entry.getId()+"</id>"
				+"<title>"+entry.getTitle()+"</title>"
				+"<updated>"+entry.getUpdated()+"</updated>"
				+"<gos:serviceType>"+entry.getServiceType()+"</gos:serviceType>"
				+"<gos:serviceUrl>"+entry.getServiceUrl()+"</gos:serviceUrl>"
				+"</entry>";
		return xml;

	}

}

