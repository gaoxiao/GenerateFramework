My${viewName}View = Ext.extend(Ext.Panel, {
	constructor : function(b) {
		Ext.applyIf(this, b);
		this.initUIComponents();
		My${viewName}View.superclass.constructor.call(this, {
			id : "My${viewName}View",
			title : "${obj.menu}管理",
			iconCls : "menu-${obj.menuName}",
			layout : "border",
			items : [ /* this.searchPanel , */this.gridPanel ]
		});
	},
	initUIComponents : function() {
		this.topbar = new Ext.Toolbar({
			items : [ {
				iconCls : "btn-add",
				text : "添加${obj.menu}",
				xtype : "button",
				scope : this,
				handler : this.createRs
			}, {
				iconCls : "btn-del",
				text : "删除${obj.menu}",
				xtype : "button",
				scope : this,
				handler : this.removeSelRs
			} ]
		});
		this.gridPanel = new HT.GridPanel({
			region : "center",
			tbar : this.topbar,
			rowActions : true,
			id : "My${viewName}Grid",
			store : new Ext.data.Store({
				baseParams : {
					limit : 20,
					start : 0
				},
				proxy : new Ext.data.HttpProxy({
					url : __ctxPath + "/jf/${obj.controller}/${obj.menuName}"
				}),
				reader : new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalRow",
					id : "id",
					//gaoxiao 遍历
					fields : [ {
						name : "id",
						type : "string"
						}
						<#list metas as meta>
						<#if meta.colName != "id"> 
						,"${meta.colName}"
						</#if>
						</#list>
					]
				}),
				autoLoad : true
			}),
			
			columns : [
				<#list metas as meta>
					<#if meta.colName != "content" > 
				{  	
				  	text : "${meta.colName}",
					width : 100,
					dataIndex : '${meta.colName}',
					sortable : false
				},
					</#if>    
				</#list>
				new Ext.ux.grid.RowActions({
					header : "管理",
					width : 100,
					actions : [ {
						iconCls : "btn-del",
						qtip : "删除",
						style : "margin:0 3px 0 3px"
					}, {
						iconCls : "btn-edit",
						qtip : "编辑",
						style : "margin:0 3px 0 3px"
					} ],
					listeners : {
						scope : this,
						"action" : this.onRowAction
					}
				}) 
			]
		});
		// this.gridPanel = grid;
		this.gridPanel.addListener("rowdblclick", this.rowClick);
	},
	reset : function() {
		this.searchPanel.getForm().reset();
	},
	search : function() {
		$search({
			searchPanel : this.searchPanel,
			gridPanel : this.gridPanel
		});
	},
	rowClick : function(d, e, f) {
		d.getSelectionModel().each(function(a) {
			new My${viewName}Form({
				id : a.data.id
			}).show();
		});
	},
	createRs : function() {
		new My${viewName}Form().show();
	},
	removeRs : function(b) {
		$postDel({
			url : __ctxPath + "/jf/${obj.controller}/${obj.menuName}/delete",
			ids : b,
			grid : this.gridPanel
		});
	},
	removeSelRs : function() {
		$delGridRs({
			url : __ctxPath + "/jf/${obj.controller}/${obj.menuName}/delete",
			grid : this.gridPanel,
			idName : "id"
		});
	},
	editRs : function(b) {
		new My${viewName}Form({
			id : b.data.id
		}).show();
	},
	onRowAction : function(j, g, i, h, f) {
		switch (i) {
		case "btn-del":
			this.removeRs.call(this, g.data.id);
			break;
		case "btn-edit":
			this.editRs.call(this, g);
			break;
		default:
			break;
		}
	}
});
