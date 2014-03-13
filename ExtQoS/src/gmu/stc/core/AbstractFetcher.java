package gmu.stc.core;

public abstract class AbstractFetcher {
	
	protected String taskname;
	protected String url;
	protected String type;
	protected String isprivate;
	protected String saveas;
	
	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsprivate() {
		return isprivate;
	}

	public void setIsprivate(String isprivate) {
		this.isprivate = isprivate;
	}
	
	public AbstractFetcher(String taskname, String url, String type, String isprivate, String saveas) {
		// TODO Auto-generated constructor stub
		this.taskname = taskname;
		this.url = url;
		this.type = type;
		this.isprivate = isprivate;
		this.saveas = saveas;
	}

	abstract void runFetch();

	abstract void fetch();

	
}
