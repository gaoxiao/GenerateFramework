MyNewsForm = Ext.extend(Ext.Window, {
	imagePanlbar : null,
	constructor : function(b) {
		Ext.applyIf(this, b);
		this.initUIComponents();
		MyNewsForm.superclass.constructor.call(this, {
			id : "MyNewsFormWin",
			layout : "fit",
			items : this.formPanel,
			modal : true,
			height : 550,
			width : 1030,
			maximizable : true,
			iconCls : "menu-news",
			title : "添加新闻",
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
				title : "新闻内容",
				layout : "form",
				flex : 2,
				labelWidth : 60,
				width : "60%",
				defaultType : "textfield",
				autoHeight : true,
				defaults : {
					width : "97%"
				},
				items : [ {
					name : "news.id",
					xtype : "hidden",
					value : this.id == null ? "" : this.id,
					disabled : this.id == null ? true : false
				}, {
					fieldLabel : "新闻标题",
					name : "news.title",
					allowBlank : false,
					maxLength : 128
				}, {
					fieldLabel : "作者",
					name : "news.author",
					allowBlank : false,
					maxLength : 32
				}, {
					fieldLabel : "内容",
					name : "news.content",
					allowBlank : false,
					height : 360,
					xtype : "fckeditor",
					maxLength : 65535
				} ]
			} ]
		});

		// 加载数据
		if (this.id != null && this.id != "undefined") {
			var b = this.formPanel;
			b.loadData({
				url : __ctxPath + "/jf/news/detail?id=" + this.id,
				root : "data",
				preName : "news",
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
			url : __ctxPath + "/jf/news/save",
			callback : function(e, f) {
				var d = Ext.getCmp("MyNewsGrid");
				if (d != null) {
					d.getStore().reload();
				}
				this.close();
			}
		});
	}
});