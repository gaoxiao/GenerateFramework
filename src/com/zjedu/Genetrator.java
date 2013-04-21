package com.zjedu;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zjedu.dao.DBMeta;
import com.zjedu.util.PropertyUtil;
import com.zjedu.util.SqlUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Genetrator {

	static Configuration cfg = null;
	private final static String Defalut_TABLENAME = "news";
	private final static String ControllerFtl = PropertyUtil
			.getProperty("ControllerFtl");
	private final static String ModelFtl = PropertyUtil.getProperty("ModelFtl");
	private final static String ViewFtl = PropertyUtil.getProperty("ViewFtl");
	private final static String FormFtl = PropertyUtil.getProperty("FormFtl");

	public static void main(String[] args) {
		String tableName;
		if (args.length == 0) {
			tableName = Defalut_TABLENAME;
		} else {
			tableName = args[0];
		}
		tableName = normalize(tableName);

		init();

		geneModel(tableName);
		geneController(tableName);
		geneView(tableName);
		geneForm(tableName);
	}

	private static void geneForm(String tableName) {
		// TODO Auto-generated method stub

	}

	private static void geneView(String tableName) {
		List<DBMeta> metas = SqlUtil.getMetadata(tableName);
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("metas", metas);
		String filePath = PropertyUtil.getProperty("jsFloder")
				+ tableName.toLowerCase() + "/My" + tableName + "View.js";
		gene(vo, ViewFtl, filePath);
	}

	private static void geneController(String tableName) {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("javaPackageName", PropertyUtil.getProperty("javaPackageName"));
		String filePath = PropertyUtil.getProperty("javaFloder")
				+ tableName.toLowerCase() + "/" + tableName + "Controller.java";
		gene(vo, ControllerFtl, filePath);
	}

	private static void geneModel(String tableName) {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		// 在配置文件中配置包名
		vo.put("javaPackageName", PropertyUtil.getProperty("javaPackageName"));
		String filePath = PropertyUtil.getProperty("javaFloder") + tableName
				+ "/" + tableName + ".java";
		gene(vo, ModelFtl, filePath);
	}

	private static String normalize(String tableName2) {
		String tablename = Character.toUpperCase(tableName2.charAt(0))
				+ tableName2.substring(1);
		return tablename;
	}

	public static void gene(Map<String, Object> vo, String templateName,
			String fileName) {
		try {
			Template template = cfg.getTemplate(templateName);
			File distFile = new File(fileName);

			// 检查父级目录
			File parent = distFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}

			Writer writer = new PrintWriter(distFile, "utf-8");
			template.process(vo, writer);
			System.out.println(fileName + "生成成功！");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

	}

	public static void init() {
		System.out.println("初始化freemarker...");
		URL url = Genetrator.class.getResource("/ftl");

		cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(url.toURI()));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setEncoding(Locale.getDefault(), "utf-8");
			System.out.println("初始化完成");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
