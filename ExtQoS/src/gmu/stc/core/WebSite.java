package gmu.stc.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletContext;



public class WebSite {

	protected ServletContext sc;
	protected String rootPath;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public WebSite(String rootPath){
		this.rootPath = rootPath;
		System.out.println(this.rootPath);
		System.out.println("Initialize website");
	}

	public String getUsersPath(){
		new File(rootPath+"users").mkdir();
		return this.rootPath + "users";
	}

	public boolean storeToXML(Properties info, String path) {
		
		System.out.println(path);
		try {
			File xmlFile = new File(path);
			if(!xmlFile.exists()) {
				xmlFile.createNewFile();
			} 
	    	//where to store?
	    	OutputStream os = new FileOutputStream(path, false);	 
	    	//store the properties detail into a pre-defined XML file
	    	info.storeToXML(os, "info");
	 
	    	
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		} 

		return false;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
