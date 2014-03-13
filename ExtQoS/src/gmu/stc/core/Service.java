package gmu.stc.core;

public class Service {
	private String type;
	private String url;
	public Service(String type, String url){
		this.type = type;
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
