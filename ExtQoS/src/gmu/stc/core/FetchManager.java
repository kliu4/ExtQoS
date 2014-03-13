package gmu.stc.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FetchManager {
	private WebSite website;
	protected String taskname;
	protected String url;
	protected String type;
	protected String isprivate;
	protected String saveas;
	protected String hours;
	protected String id;
	protected String atomPath;
	public FetchManager(WebSite website, String id, String taskname, String url,
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
	
	public void start(){
		ScheduledExecutorService scheduler =
			    Executors.newScheduledThreadPool(1);
		CswFetchRunnable cr = new CswFetchRunnable(website, id, taskname, url, type, isprivate, saveas, hours, atomPath);
		ScheduledFuture<?> fetchHandler =
			      scheduler.scheduleAtFixedRate(cr, 0, Integer.parseInt(hours), TimeUnit.HOURS);
	}
}
