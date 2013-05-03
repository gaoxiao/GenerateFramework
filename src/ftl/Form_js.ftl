My${tableName}Form = Ext.extend(Ext.Window, {
	imagePanlbar : null,
	constructor : function(b) {
		Ext.applyIf(this, b);
		this.initUIComponents();
		My${tableName}Form.superclass.constructor.call(this, {
			id : "My${tableName}FormWin",
			layout : "fit",
			items : this.formPanel,
			modal : true,
			height : 550,
			width : 1030,
			maximizable : true,
			iconCls : "menu-${tableNameLowerCase}",
			title : "编辑${tableName}",
			buttonAlign : "center",
			buttons : [ {
				text : "保存",
				iconCls : "btn-save",
				scope : this,
				handler : this.save
			}, {
				text : "重置",
				iconCls : "btn-reset",
				scope : this,
				handler : this.reset
			}, {
				text : "取消",
				iconCls : "btn-cancel",
				scope : this,
				handler : this.cancel
			} ]
		});
	},
	initUIComponents : function() {
		this.formPanel = new Ext.FormPanel({
			layout : "hbox",
			frame : false,
			layoutConfig : {
				padding : "5",
				pack : "start",
				align : "middle"
			},
			defaults : {
				margins : "0 5 0 0"
			},
			border : false,
			items : [ {
				xtype : "fieldset",
				title : "${tableName}",
				layout : "form",
				flex : 2,
				labelWidth : 60,
				width : "60%",
				defaultType : "textfield",
				autoHeight : true,
				defaults : {
					width : "97%"
				},
				items : [
				<#list metas as meta>
					<#if meta.colName == "id" > 
					{  	
						name : "${tableNameLowerCase}.id",
						xtype : "hidden",
						value : this.id == null ? "" : this.id,
						disabled : this.id == null ? true : false
					},
					<#elseif meta.colName == "content">
					{  	
						fieldLabel : "${meta.colName}",
						name : "${tableNameLowerCase}.${meta.colName}",
						allowBlank : false,
						height : 360,
						xtype : "fckeditor",
						maxLength : 65535
					},
					<#elseif meta.colName == "date">
					<#-- 不能更改日期 -->
					<#else>
					{  	
						fieldLabel : "${meta.colName}",
						name : "${tableNameLowerCase}.${meta.colName}",
						allowBlank : false,
						maxLength : 128
					},
					</#if>    
				</#list>
				]
			} ]
		});

		// 加载数据
		if (this.id != null && this.id != "undefined") {
			var b = this.formPanel;
			b.loadData({
				url : __ctxPath + "/jf/${tableNameLowerCase}/detail?id=" + this.id,
				root : "data",
				preName : "${tableNameLowerCase}",
				success : function(a, h) {
					var g = Ext.util.JSON.decode(a.responseText).data;
				},
				failure : function(a, d) {
					Ext.ux.Toast.msg("编辑", "载入失败");
				}
			});
		}
	},
	reset : function() {
		this.formPanel.getForm().reset();
	},
	cancel : function() {
		this.close();
	},
	save : function() {
		$postForm({
			formPanel : this.formPanel,
			scope : this,
			url : __ctxPath + "/jf/${tableNameLowerCase}/save",
			callback : function(e, f) {
				var d = Ext.getCmp("My${tableName}Grid");
				if (d != null) {
					d.getStore().reload();
				}
				this.close();
			}
		});
	}
});