package com.zjedu.dao;

public class ExcelObject {
	public String functionGroup;// 功能组中文
	public String functionGroupName;// 功能组英文

	public String menuGroup;// 分组中文
	public String menuGroupName;// 分组英文

	public String menu;// 菜单中文
	public String menuName;// 菜单英文

	public String viewType;
	public String model;
	public String controller;

	@Override
	public String toString() {
		return "ExcelObject [functionGroup=" + functionGroup
				+ ", functionName=" + functionGroupName + ", menuGroup="
				+ menuGroup + ", menuGroupName=" + menuGroupName + ", menu="
				+ menu + ", menuName=" + menuName + ", viewType=" + viewType
				+ ", model=" + model + ", controller=" + controller + "]";
	}

	public ExcelObject(String functionGroup, String functionName,
			String menuGroup, String menuGroupName, String menu,
			String menuName, String viewType, String model, String controller) {
		super();
		this.functionGroup = functionGroup;
		this.functionGroupName = functionName;
		this.menuGroup = menuGroup;
		this.menuGroupName = menuGroupName;
		this.menu = menu;
		this.menuName = menuName;
		this.viewType = viewType;
		this.model = model;
		this.controller = controller;
	}

	public String getFunctionGroup() {
		return functionGroup;
	}

	public void setFunctionGroup(String functionGroup) {
		this.functionGroup = functionGroup;
	}

	public String getFunctionName() {
		return functionGroupName;
	}

	public void setFunctionName(String functionName) {
		this.functionGroupName = functionName;
	}

	public String getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(String menuGroup) {
		this.menuGroup = menuGroup;
	}

	public String getMenuGroupName() {
		return menuGroupName;
	}

	public void setMenuGroupName(String menuGroupName) {
		this.menuGroupName = menuGroupName;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}
}
