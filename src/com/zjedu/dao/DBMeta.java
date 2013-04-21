package com.zjedu.dao;

public class DBMeta {
	public String colName;
	public String colType;

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public String toString() {
		return colName + ":" + colType;
	}
}
