package com.zjedu.dao;

import com.jfinal.plugin.activerecord.Model;

/**
 * Blog model.
 */
@SuppressWarnings("serial")
public class News extends Model<News> {
	public static final News dao = new News();
}