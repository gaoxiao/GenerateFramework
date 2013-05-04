<#list objects as obj>
App.importJs.${obj.menuName?cap_first}View = [ __ctxPath + "/js/zjedu/${obj.menuName}/${obj.menuName?cap_first}View.js",
	__ctxPath + "/js/zjedu/${obj.menuName}/${obj.menuName?cap_first}Form.js" ];
</#list>