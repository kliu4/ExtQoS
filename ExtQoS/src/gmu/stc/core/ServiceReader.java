package gmu.stc.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ServiceReader {
	
	private File sFile;
	public ServiceReader(File file){		
		this.sFile = file;
	}
	
	public ArrayList<Service> getServices(){
		ArrayList<Service> as= new ArrayList<Service>();
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = (Document) builder.build(sFile);
			Element rootNode = document.getRootElement();
			for(Element e:rootNode.getChildren()){
				if(e.getName().equals("Entry")){
					String type = e.getChildText("serviceType", MyNameSpace.NAMESPACE_GOS);
					String url = e.getChildText("serviceUrl", MyNameSpace.NAMESPACE_GOS);
					Service s = new Service(type, url);
					as.add(s);
				}
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return as;
	}

}
