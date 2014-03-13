/**
 * 
 */
package gmu.stc.core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @author kailiu
 *
 */
public class TaskManager {

	/**
	 * @param args
	 */

	private String taskFilePath;


	public TaskManager(String taskFilePath){
		this.taskFilePath = taskFilePath;
		System.out.println("TaskFile Path: " + taskFilePath);
	}

	public void remove(String userName, String id){
		try {
			this.removeRecord(userName, id);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void removeRecord(String userName, String id) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(taskFilePath);
		Element rootNode = document.getRootElement();
		for(Element e:rootNode.getChildren()){					
			if(e.getAttributeValue("username").equals(userName)){
				for(Element el: e.getChildren()){
					if(el.getAttributeValue("id").equals(id)){
						e.removeContent(el);
						break;
					}
				}
				XMLOutputter xmlOutputter = new XMLOutputter();
				xmlOutputter.setFormat(Format.getPrettyFormat());
				xmlOutputter.output(document, new FileWriter(taskFilePath));
				break;
			}
		}
	}

	public void update(String userName, String id, String name, String url,
			String method, String isPrivate, String saveAs){
		try {
			updateRecord(userName, id, name, url, method, isPrivate, "", saveAs);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException e){
			e.printStackTrace();
		}	
	}

	public void updateLastRun(String userName, String id, String lastRun){
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(taskFilePath);
			Element rootNode = document.getRootElement();
			for(Element e:rootNode.getChildren()){					
				if(e.getAttributeValue("username").equals(userName)){
					for(Element el: e.getChildren()){
						if(el.getAttributeValue("id").equals(id)){
							el.getChild("updated").setText(lastRun);
						}
					}
					XMLOutputter xmlOutputter = new XMLOutputter();
					xmlOutputter.setFormat(Format.getPrettyFormat());
					xmlOutputter.output(document, new FileWriter(taskFilePath));
					break;
				}
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException e){
			e.printStackTrace();
		}	
	}
	private void updateRecord(String userName, String id, String name, String url,
			String method, String isPrivate, String lastRun, String saveAs) throws JDOMException, IOException, NullPointerException{
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(taskFilePath);
		Element rootNode = document.getRootElement();
		for(Element e:rootNode.getChildren()){					
			if(e.getAttributeValue("username").equals(userName)){
				for(Element el: e.getChildren()){
					if(el.getAttributeValue("id").equals(id)){
						el.setAttribute("name", name);
						el.setAttribute("method", method);
						el.getChild("url").setText(url);
						el.getChild("private").setText(isPrivate);
						el.getChild("saveas").setText(saveAs);
						el.getChild("updated").setText(lastRun);
					}
				}
				XMLOutputter xmlOutputter = new XMLOutputter();
				xmlOutputter.setFormat(Format.getPrettyFormat());
				xmlOutputter.output(document, new FileWriter(taskFilePath));
				break;
			}
		}
	}

	public String getMaxID(String userName){
		try{
			
			List<String> ls =  getIDs(userName);
			String maxID = ls!=null?ls.get(ls.size() -1 ):"0";		
			System.out.println(maxID);
			return maxID;
		}catch(ArrayIndexOutOfBoundsException e){
			return "0";
		}
	}

	private List<String> getIDs(String userName){
		List<String> IDs = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = (Document) builder.build(taskFilePath);
			Element rootNode = document.getRootElement();
			for(Element e:rootNode.getChildren()){					
				if(e.getAttributeValue("username").equals(userName)){
					for(Element el: e.getChildren()){
						IDs.add(el.getAttributeValue("id"));
					}
					if(e.getChildren()==null || e.getChildren().size()==0)
						return null;
					break;
				}
				
			}
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			return null;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		return IDs;
	}
	public void Add(String userName, String id, String name, String url,
			String method, String isPrivate, String saveAs){
		try {
			this.AddRecord(userName, id, name, url, method, isPrivate, saveAs);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	private void AddRecord(String userName, String id, String name, String url,
			String method, String isPrivate, String saveAs) throws JDOMException, IOException, NullPointerException{
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(taskFilePath);
		Element rootNode = document.getRootElement();
		for(Element e:rootNode.getChildren()){					
			if(e.getAttributeValue("username").equals(userName)){
				Element el = new Element("task");
				el.setAttribute("id", id);
				el.setAttribute("name", name);
				el.setAttribute("method", method);
				el.addContent(new Element("url").setText(url));
				el.addContent(new Element("updated").setText(""));
				el.addContent(new Element("private").setText(isPrivate));
				el.addContent(new Element("saveas").setText(saveAs));
				e.addContent(el);

				XMLOutputter xmlOutputter = new XMLOutputter();
				xmlOutputter.setFormat(Format.getPrettyFormat());
				xmlOutputter.output(document, new FileWriter(taskFilePath));
				break;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaskManager tm = new TaskManager("/Users/kailiu/worksapces/qosextension/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ExtQoS/Entry.xml");
		System.out.println(tm.getMaxID("admin"));
		tm.Add("admin", "4", "test4", "http://test", "csw", "true", "atom4.xml");
		System.out.println(tm.getMaxID("admin"));
	}


}
