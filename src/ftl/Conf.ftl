#添加以下两行到JFConfig.java的configRoute方法和configPlugin方法中
me.add("/jf/info", ${tableName}Controller.class);
arp.addMapping("${tableNameLowerCase}", ${tableName}.class);


#添加以下到App.import.js中
	${tableName}View : [ __ctxPath + "/js/zjedu/${tableNameLowerCase}/${tableName}View.js",
			__ctxPath + "/js/zjedu/${tableNameLowerCase}/${tableName}Form.js" ],

#添加以下到/js/menu/下对应的xml配置中
<?xml version="1.0" encoding="UTF-8"?>
<Menus id="Mod_news" text="${excelObject.functonGroup}" iconCls="mod-news">
	<Items id="MyDesktop" text="${excelObject.menuGroup}" iconCls="menu-desktop" isPublic="true"> 
		<Item id="${tableName}View" text="${excelObject.menuName}" iconCls="menu-subDiary"/>  
	</Items>
</Menus>
