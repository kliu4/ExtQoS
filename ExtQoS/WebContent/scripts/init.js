Ext.Loader.setPath('Ext.ux', 'ux');
Ext.require('Ext.ux.layout.Center');

Ext.require(['*', 'Ext.grid.*',]);

Ext.Loader.setConfig({
    enabled : true,
    paths : {
        'Ext.ux' : 'extjs/src/ux'
    }
});

Ext.define('ExtQoS.Catalog',{
	extend: 'Ext.data.Model',
	fields:[{
		name: 'id', type:'string', mapping: '@id'
	},{
		name: 'name', type:'string', mapping: '@name'
	},{
		name: 'url', type: 'string'
	},{
		name: 'method', type:'string', mapping: '@method'
	},
//	{
//		name:'isprivate', type:'bool', mapping:'private'
//	}, 
	{
		name:'updated', type:'string'
	},{
		name:'saveas', type:'string'
	}],
	proxy:{
		type: 'ajax',
		reader: 'xml'
	}
});

var catalogStore = Ext.create('Ext.data.Store',{
	model: 'ExtQoS.Catalog',
	autoLoad: true,
	proxy: {
		type: 'ajax',
		url: 'entry.xml',
		reader:{
			type:'xml',
			record: 'task'			
		}
	}
});

var newURL = '';

var catalogGrid = Ext.create('Ext.grid.Panel',{
	title: 'Feed Catalogs',
	region: 'north',
	collapsible: true,
	height: 300,
	store: catalogStore,
	dockedItems: [{
		   weight: 1,
		   xtype: 'toolbar',
		   dock: 'bottom',
		   layout:{pack:'center'},
		   ui: 'footer',
		   items: [ {
			   iconCls: 'icon-feeds',
			   text: 'Get Feeds',
			   itemId: 'getfeeds',
			   handler: function(){
			    	var win = window.open(newURL);	    	
			    },
			   disabled: true
		   }]}],
	columns: [{header: 'ID',
		width: 50,
		sortable: true,
		dataIndex: 'id'
	},{
		header: 'Name',
		width: 200,
		sortable: true,
		dataIndex: 'name'
	}, {
		header: 'Url',
		flex: 1,
		sortable: true,
		dataIndex: 'url'
	},{
		header: 'Type',
		width: 50,
		sortable: true,
		dataIndex: 'method'
	}, 
//	{
//		header: 'Private?',
//		width: 100,
//		xtype: 'checkcolumn',
//		dataIndex: 'isprivate'
//	},
	{
		header: 'Last Run',
		width: 100,
		sortable: true,
		dataIndex: 'updated'
	},{
		header: 'Atom File',
		name: 'saveas',
		width: 100,
		sortable: true,
		dataIndex: 'saveas'
	}],
	listeners: {
        selectionchange: function(model, records) {
            if (records[0]) {
            	this.down('#getfeeds').setDisabled(records.length === 0);
        		var feedsFile = records[0].get('saveas');
        		newURL = feedsFile;
        		feedStore = Ext.create('Ext.data.Store',{
        			model: 'Entry',
        			autoLoad: true,
        			proxy: {
        				type: 'ajax',
        				url: feedsFile,
        				reader:{
        					type:'xml',
        					record: 'entry'			
        				}
        			}
        		});
        		feedGrid.reconfigure(feedStore);
            }
        }
    }
});

Ext.define('Entry',{
	extend: 'Ext.data.Model',
	fields:[{
		name: 'serviceType', type:'string'
	},{
		name: 'serviceUrl', type: 'string'
	},{
		name: 'updated', type: 'string'
	}],
	proxy:{
		type: 'ajax',
		reader: 'xml'
	}
});

var feedStore = Ext.create('Ext.data.Store',{
	model: 'Entry',
	autoLoad: true,
	proxy: {
		type: 'ajax',
		url: 'atom.xml',
		reader:{
			type:'xml',
			record: 'entry'			
		}
	}
});


var feedGrid = Ext.create('Ext.grid.Panel',{
	title: 'Feed Entries',
	region: 'center',
	name: 'feedentries',
	collapsible: true,
	store: feedStore,
	disableSelection: true,
	loadMask: true,
	autoScroll: true,
	Layout: "FitLayout",
	columns:[{
		text: "serviceType", width: 100, dataIndex:'serviceType'
	},{
		text: "serviceUrl", flex: 0.7, dataIndex: 'serviceUrl'
	},{
		text: "updated", width: 200, dataIndex: 'updated'
	}]
});

var fetchTypes = Ext.create('Ext.data.Store',{
	fields:['abbr', 'name'],
	data:[{
		'abbr': 'csw',
		'name': 'CSW GetCapabilities URL, e.g., http://IP/PATH?service=CSW&request=GetCapabilities&version=2.0.2'
	}
//	,{
//		'abbr': 'waf',
//		'name': 'Web Access Folder, e.g, http://IP/PATH'
//	}
	]
});

var fetchComboBox=Ext.create('Ext.form.ComboBox',{
	id: 'newtaskcombobox',
	anchor: '50%',
	fieldLabel: 'Fetch Type',
	store: fetchTypes,
	displayField: 'name',
	valueField: 'abbr',
	allowBlank: false,
	forceSelection: true
});

Ext.define('ExtQoS.addTasks.Form',{
	extend: 'Ext.form.Panel',
	alias: 'widget.writerform',
	requires: ['Ext.form.field.Text'],

	initComponent: function(){
		this.addEvents('create');
		Ext.apply(this, {
			activeRecord: null,
			//iconCls: 'icon-user',
			frame: true,
			title: 'Add Fetch Tasks -- All fields are required',
			defaultType: 'textfield',
			bodyPadding: 5,
			fieldDefaults: {
				anchor: '100%',
				labelAlign: 'right'
			},
			items: [ {
				fieldLabel: 'Task Name',
				name: 'name',
				allowBlank: false,
				tooltip: 'Input the task name, e.g., GEOSS Clearinghouse CSW catalog',
				listeners : {
					render: function(p) {
						p.getEl().down('input').set({'data-qtip': p.tooltip});
					}
				}
			}, {
				fieldLabel: 'Url',
				name: 'url',
				vtype:'url',
				allowBlank: false,
				tooltip: 'Input the task url, e.g., http://clearinghouse.cisc.gmu.edu/geonetwork/srv/en/csw',
				listeners : {
					render: function(p) {
						p.getEl().down('input').set({'data-qtip': p.tooltip});
					}
				}
			},
			fetchComboBox,
//			{
//				id: 'newtaskfrom',
//				xtype: 'checkboxfield',
//				name: 'privilege',
//				fieldLabel: 'Privilege',
//				boxLabel: 'private'
//			},
//			{
//				anchor: '30%',
//				xtype: 'numberfield',
//				name: 'runEvery',
//				fieldLabel: 'Run Every',
//				value: 24,
//				step: 6,
//				minValue: 6,
//				maxValue: 72
//			},
			{
				anchor: '30%',
				id: 'saveas',
				fieldLabel: 'Save As',
				name: 'saveas',
				allowBlank: false,
				tooltip: 'Input the feed name which the results will be saved as, e.g., geossclh.xml',
				listeners : {
					render: function(p) {
						p.getEl().down('input').set({'data-qtip': p.tooltip});
					}
				}
			}],
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				items: ['->', {
					iconCls: 'icon-add',
					text: 'Create',
					scope: this,
					handler: this.onCreate
				}, {
					iconCls: 'icon-reset',
					text: 'Reset',
					scope: this,
					handler: this.onReset
				}]
			}]
		});
		this.callParent();
	},

	setActiveRecord: function(record){
		this.activeRecord = record;
		if (record) {
			this.down('#save').enable();
			this.getForm().loadRecord(record);
		} else {
			this.down('#save').disable();
			this.getForm().reset();
		}
	},

	onSave: function(){
		var active = this.activeRecord,
		form = this.getForm();

		if (!active) {
			return;
		}
		if (form.isValid()) {
			form.updateRecord(active);
			this.onReset();
		}
	},

	onCreate: function(){
		var form = this.getForm();

		if (form.isValid()) {
			var maxID = "0";
			Ext.Ajax.request({
				url: 'task.jsp',
				method: 'GET',
				params:{
					service: 'GetMaxID'
				},
				success: function(response, opts){
					maxID = response.responseText;	
					maxID = (parseInt(maxID) + 1).toString();
					catalog = {
							id: maxID,
							name: form.findField('name').getValue(),
							url: form.findField('url').getValue(),
							method: Ext.getCmp('newtaskcombobox').getValue(),
							//isprivate: Ext.getCmp('newtaskfrom').getValue(),
							saveas: Ext.getCmp('saveas').getValue()
					};
					catalogStore.insert(0, catalog);
					Ext.Ajax.request({
						url: 'task.jsp',
						method: 'GET',
						params:{
							service: 'Add',
							name: form.findField('name').getValue(),
							url: form.findField('url').getValue(),
							method: Ext.getCmp('newtaskcombobox').getValue(),
							//isprivate: Ext.getCmp('newtaskfrom').getValue(),
							saveas: Ext.getCmp('saveas').getValue()
						},
						success:function(response, opts){form.reset();}
					});					
				}
			});
			
			
			
			
		}

	},

	onReset: function(){
		this.getForm().reset();
	}

});

Ext.define('ExtQoS.listTasks.Grid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.liststasksgrid',
	id: 'listTasks',
	requires: [
	           'Ext.grid.plugin.RowEditing',
	           'Ext.form.field.Text',
	           'Ext.toolbar.TextItem'
	           ],

	           initComponent: function(){
	        	   this.rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	        		   clicksToEdit: 1,
	        		   autoCancel: false,
	        		   listeners: {
	        			   edit: function(editor, context, eOpts) {
	        				   //var record = e.record;
	        				   //do your processing here, e.g.:
	        				   Ext.Msg.alert(context.record.data.name);
	        			   }
	        		   }
	        	   });
	        	   Ext.apply(this, {
	        		   iconCls: 'icon-grid',
	        		   frame: true,
	        		   store: catalogStore,
	        		   dockedItems: [{
	        			   xtype: 'toolbar',
	        			   items: [{
	        				   iconCls: 'icon-delete',
	        				   text: 'Delete',
	        				   disabled: true,
	        				   itemId: 'delete',
	        				   scope: this,
	        				   handler: this.onDeleteClick
	        			   }]
	        		   },{
	        			   weight: 1,
	        			   xtype: 'toolbar',
	        			   dock: 'bottom',
	        			   layout:{pack:'center'},
	        			   ui: 'footer',
	        			   items: [ {
	        				   iconCls: 'icon-run',
	        				   text: 'Run',
	        				   itemId: 'run',
	        				   scope: this,
	        				   handler: this.onRun,
	        				   disabled: true
	        			   }]}],
	        			   columns: [{header: 'ID',
	        				   width: 50,
	        				   sortable: true,
	        				   dataIndex: 'id'
	        			   },{
	        				   header: 'Name',
	        				   width: 200,
	        				   sortable: true,
	        				   dataIndex: 'name',
	        				   editor:{xtype: 'textfield', allowBlank: false}
	        			   }, {
	        				   header: 'Url',
	        				   flex: 1,
	        				   sortable: true,
	        				   dataIndex: 'url',
	        				   editor:{xtype: 'textfield', allowBlank: false}
	        			   },{
	        				   header: 'Type',
	        				   width: 50,
	        				   sortable: true,
	        				   dataIndex: 'method',
	        				   editor:{xtype: 'textfield', allowBlank: false}
	        			   }, 
//	        			   {
//	        				   header: 'Private?',
//	        				   width: 100,
//	        				   xtype: 'checkcolumn',
//	        				   sortable: true,
//	        				   dataIndex: 'isprivate',
//	        				   editor:{xtype: 'checkbox',allowBlank: false}
//	        			   },
	        			   {
	        				   header: 'Last Run',
	        				   width: 100,
	        				   xtype: 'datecolumn',
	        				   format: 'Y-m-d H:i:s',
	        				   sortable: true,
	        				   dataIndex: 'updated'
	        			   },{
	        				   header: 'Save As',
	        				   width: 100,
	        				   sortable: true,
	        				   dataIndex: 'saveas',
	        				   editor:{xtype: 'textfield', allowBlank: false}
	        			   }]
	        	   });
	        	   this.callParent();
	        	   this.getSelectionModel().on('selectionchange', this.onSelectChange, this);
	           },

	           onRun: function(){
	        	   var selection = this.getView().getSelectionModel().getSelection()[0];
	        	   if (selection) {
	        		   Ext.Ajax.request({
	        			   url: 'fetch.jsp',
	        			   method: 'GET',
	        			   params:{
	        				   id: selection.get('id'),
	        				   taskName: selection.get('name'),
	        				   url: selection.get('url'),
	        				   method: selection.get('method'),
	        				   //isprivate:selection.get('isprivate'),
	        				   saveas: selection.get('saveas')
	        			   }
	        		   });
	        	   }
	           },
	           onSelectChange: function(selModel, selections){
	        	   
	        	   this.down('#delete').setDisabled(selections.length === 0);
	        	   this.down('#run').setDisabled(selections.length === 0);
	           },
	           onDeleteClick: function(){
	        	   var selection = this.getView().getSelectionModel().getSelection()[0];
	        	   Ext.Ajax.request({
						url: 'task.jsp',
						method: 'GET',
						params:{
							service: 'Remove',
							id: selection.get('id')
						}						
					});
	        	   
	        	   if (selection) {
	        		   this.store.remove(selection);
	        	   }
	           }

});

Ext.require([
             'Ext.data.*',
             'Ext.tip.QuickTipManager',
             'Ext.window.MessageBox'
             ]);


var TaskHarvester = Ext.create('Ext.Panel',{
	region: 'center',
	layout: {type: 'vbox', align: 'stretch'},
	items:[{
		xtype:'writerform',
		collapsible: true,
		listeners: {
			create: function(form, data){
				catalogStore.insert(0, data);
			}
		}
	},{
		xtype:'liststasksgrid',
		title: 'Task List',
		region: 'south',
		collapsible: true,
		flex: 1
	}]
});

var userManagementForm = Ext.create('Ext.form.Panel',{
	width: 300,
	height: 200,
	frame: true,
	layout:'form',
	    border: false,
        bodyPadding: 10,
	items:[{
		xtype: 'textfield',
		fieldLabel: 'Current Password',
		name: 'currentPassword',
		allowBlank: false,
		inputType: 'password'
	},{
		xtype: 'textfield',
		fieldLabel: 'New Password',
		name: 'newPassword',
		allowBlank: false,
		inputType: 'password'
	},{
		xtype: 'textfield',
		fieldLabel: 'Retype New Password',
		name: 'reNewPassword',
		allowBlank: false,
		inputType: 'password'
	}],
	buttons:[{text: 'Reset',
		handler: function(){
			userManagementForm.getForm().submit( {
    			method:'POST',
    			url: 'resetPassword.jsp',
    			waitTitle:'Connecting',
    			waitMsg:'Sending data...',
    			submitEmptyText: false,
    			success:function(){
    				userManagementForm.getForm().reset();
    				Ext.Msg.alert('success', action.response.responseText);
    			},
    			failure:function(form, action){
    				userManagementForm.getForm().reset();
    				Ext.Msg.alert('failure', action.response.responseText);
    			}
    		});
		}}]
});

var userManagementPanel = Ext.create('Ext.Panel',{
	center:'region',
	width:300,	
	items: userManagementForm
});

Ext.onReady(function() {

	Ext.QuickTips.init();

	// NOTE: This is an example showing simple state management. During development,
	// it is generally best to disable state management as dynamically-generated ids
	// can change across page loads, leading to unpredictable results.  The developer
	// should ensure that stable state ids are set for stateful components in real apps.
	Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
	
	var loginForm = Ext.create('Ext.form.Panel', {
		region: 'north',
		height: 25, // give north and south regions a height
		layout: 'border',
		items:[{
			xtype: 'panel',
			region: 'east', 
			layout: 'border',
			border: false,
			width: 300,
			items: [
			        { 
			        	xtype: 'textfield',
			        	region: 'west',
			        	fieldLabel:'username',                         
			        	allowBlank:false,
			        	labelWidth: 55,
			        	emptyText: 'admin',
			        	value: 'admin',
			        	name: 'username',
			        	disabled: true,
			        	flex: 1
			        },{ 
			        	xtype: 'textfield',
			        	region: 'center',
			        	fieldLabel: 'password', 
			        	inputType: 'password',
			        	name: 'password',
			        	allowBlank:false,
			        	labelWidth: 55,
			        	flex: 1
			        },
			        {
			        	xtype: 'button',
			        	region: 'east',
			        	text: 'login',
			        	height: 25,
			        	handler: function() {
			        		loginForm.getForm().submit( {
			        			method:'POST',
			        			url: 'login.jsp',
			        			waitTitle:'Connecting',
			        			waitMsg:'Sending data...',
			        			submitEmptyText: false,
			        			success:function(){
			        				var tabs = Ext.getCmp('tabs');
			        				tabs.insert(1, {
			        					title: 'Administration',
			        					layout: 'border',
			        					items:[TaskHarvester]
			        				});	
			        				tabs.insert(2, {
			        					title: 'User Management',
			        					layout:'ux.center',
			        					items:[userManagementPanel]
			        				});
			        				loginForm.hide();
			        			},
			        			failure:function(form, action){
			        				Ext.Msg.alert('failure', action.response.responseText);
			        			}
			        		});
			        	}
			        }]
		}]
	});
	
	

	var viewport = Ext.create('Ext.Viewport', {
		id: 'border-main',
		layout: 'border',
		items: [loginForm,
		        // create instance immediately

		        // in this instance the TabPanel is not wrapped by another panel
		        // since no title is needed, this Panel is added directly
		        // as a Container
		        Ext.create('Ext.tab.Panel', {
		        	region: 'center', // a center region is ALWAYS required for border layout
		        	deferredRender: false,
		        	activeTab: 0,     // first tab initially active
		        	id: 'tabs',
		        	items: [{
		        		title: 'Home',
		        		layout: 'border',
		        		items:[{
		        			region: 'center',
		        			items:[catalogGrid,feedGrid]
		        		}]
		        	}, {
		        		contentEl: 'links',
		        		title: 'Links',
		        		autoScroll: true
		        	}, {
		        		contentEl: 'about',
		        		title: 'About',
		        		autoScroll: true
		        	}]
		        })]
	});

});
