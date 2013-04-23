#添加以下两行到JFConfig.java的configRoute方法和configPlugin方法中
me.add("/jf/info", ${tableName}Controller.class);
arp.addMapping("${tableNameLowerCase}", ${tableName}.class);


#添加以下到App.import.js中
	${tableName}View : [ __ctxPath + "/js/zjedu/${tableNameLowerCase}/${tableName}View.js",
			__ctxPath + "/js/zjedu/${tableNameLowerCase}/${tableName}Form.js" ],

#添加以下到/js/menu/下对应的xml配置中
<Item id="${tableName}View" text="${tableName}" iconCls="menu-customer"/>