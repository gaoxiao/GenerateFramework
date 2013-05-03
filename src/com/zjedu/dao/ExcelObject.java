package com.zjedu.dao;

import java.util.Arrays;

public class ExcelObject {
	public String functonGroup;
	public String menuGroup;
	public String menuName;
	public String viewType;
	public String[] functions;
	public String modelName;
	public String controller;

	public ExcelObject(String functonGroup, String menuGroup, String menuName,
			String viewType, String[] functions, String modelName,
			String controller) {
		super();
		this.functonGroup = functonGroup;
		this.menuGroup = menuGroup;
		this.menuName = menuName;
		this.viewType = viewType;
		this.functions = functions;
		this.modelName = modelName;
		this.controller = controller;
	}

	@Override
	public String toString() {
		return "ExcelObject [functonGroup=" + functonGroup + ", menuGroup="
				+ menuGroup + ", menuName=" + menuName + ", viewType="
				+ viewType + ", functions=" + Arrays.toString(functions)
				+ ", modelName=" + modelName + ", controller=" + controller
				+ "]";
	}
}
