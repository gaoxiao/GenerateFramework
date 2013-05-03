<?xml version="1.0" encoding="UTF-8"?>
<Menus id="Mod_${funcGroupName}" text="${funcGroup}" iconCls="mod-${funcGroupName}">
<#list funcGroupMap?keys as key>
	<#assign menuList=funcGroupMap[key]>
	<#-- 获取中文名称start -->
	<#list menuList as menu>
		<#assign menuStr=menu.menu>
	</#list>
	<#-- 获取中文名称end -->
	<Items id="${key}" text="${menuStr}" iconCls="menu-${key}" isPublic="true">
		<#list menuList as menu>
		<Item id="${menu.menuName}View" text="${menu.menu}" iconCls="menu-${menu.menuName}"/>  
		</#list>
	</Items>
</#list>
</Menus>
