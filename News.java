package com.zjedu.jf.News;

import com.jfinal.plugin.activerecord.Model;

/**
 * News model.
 */
@SuppressWarnings("serial")
public class News extends Model<News> {
	public static final News dao = new News();
}