/**
 * 
 */
package gmu.stc.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;


/**
 * @author kailiu
 *
 */
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	protected Properties info;
	public Properties getInfo() {
		return info;
	}

	public void setInfo(Properties info) {
		this.info = info;
	}

	protected static final String USERNAME = "P_USN";
	protected static final String PASSWORD = "P_PWS";
	protected static final String HOME_PATH = "P_HPT";
	protected static final String PERMISSIONS = "P_PRM";
	protected static final String E_MAIL = "P_EML";
	protected static final String LANGUAGE = "P_LNG";

	protected static final String SALT = "LV";

	private String rootPath;
	private int permission;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		System.out.println(rootPath);
	}

	public UserInfo() {
		// TODO Auto-generated constructor stub
		this.rootPath = "/";
		System.out.println("Initialize UserInfo");
	}

	public boolean resetPassword(WebSite website, String username, String newPassword){

		String salt = getSalt();
		info = new Properties();
		info.setProperty(USERNAME, "admin");

		info.setProperty(PASSWORD, cryptPassword(newPassword, salt));
		info.setProperty(HOME_PATH, "");
		//info.setProperty(PERMISSIONS, Integer.toHexString(ADMIN));
		info.setProperty(LANGUAGE, "en_US");
		store(website);
		//this.global = globalUser;
		return true;

	}
	public boolean load(WebSite website, String username, String password) throws IOException  {
		File f = new File(getUserPath(website, username));

		if(f.exists()){
			Properties pro = new Properties();
			FileInputStream fis = new FileInputStream(f);
			pro.loadFromXML(fis);			
			if(this.cryptPassword(password, getSalt()).equals(pro.getProperty(PASSWORD))){
				this.permission = 1;
				return true;
			}
			else{
				this.permission = 0;
				return false;
			}
		} else if (username.equals("admin") && password.equals("gci123?")) {
			String salt = getSalt();
			info = new Properties();
			info.setProperty(USERNAME, "admin");

			info.setProperty(PASSWORD, cryptPassword("gci123?", salt));
			info.setProperty(HOME_PATH, "");
			//info.setProperty(PERMISSIONS, Integer.toHexString(ADMIN));
			info.setProperty(LANGUAGE, "en_US");
			store(website);
			//this.global = globalUser;
			this.permission = 1;
			return true;
		}
		else{
			this.permission = 0;
			return false;
		}

	}

	public boolean store(WebSite webSite) {
		return webSite.storeToXML(info, getUserPath(webSite, getUsername()));
	}

	private String getUsername(){
		return  info.getProperty(USERNAME);
	}

	private String getUserPath(WebSite webSite, String username) {
		System.out.println(webSite.getUsersPath());
		return webSite.getUsersPath()+ "/" + username + ".xml";
	}
	//Add salt
	private static String getSalt() 
	{
		//Always use a SecureRandom generator
		//		SecureRandom sr = null;
		//		try {
		//			sr = SecureRandom.getInstance("SHA1PRNG");
		//		} catch (NoSuchAlgorithmException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		//Create array for salt
		//		byte[] salt = new byte[16];
		//		//Get a random salt
		//		sr.nextBytes(salt);
		//		//return salt
		//		return salt.toString();
		return "This is & * long salt";
	}

	private String cryptPassword(String password, String salt) {
		String generatedPassword = null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(salt.getBytes());
			//Get the hash's bytes 
			byte[] bytes = md.digest(password.getBytes());
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			generatedPassword = sb.toString();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		System.out.println(generatedPassword);
		return generatedPassword;

	} 

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getPermission() {
		return this.permission;
	}

	public static void main(String[] args) {

	}


}
