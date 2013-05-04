package com.zjedu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.zjedu.dao.DBMeta;
import com.zjedu.dao.ExcelObject;
import com.zjedu.util.ExcelReader;
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
	private final static String JFConfigFtl = PropertyUtil
			.getProperty("JFConfigFtl");
	private final static String ModelFtl = PropertyUtil.getProperty("ModelFtl");
	private final static String ViewFtl = PropertyUtil.getProperty("ViewFtl");
	private final static String FormFtl = PropertyUtil.getProperty("FormFtl");
	private final static String ConfFtl = PropertyUtil.getProperty("ConfFtl");
	private final static String MenuFtl = PropertyUtil.getProperty("MenuFtl");
	private final static String ZjeduImportFtl = PropertyUtil
			.getProperty("ZjeduImportFtl");

	private static boolean inited = false;

	public static void main(String[] args) {
		// String tableName;
		// if (args.length == 0) {
		// tableName = Defalut_TABLENAME;
		// } else {
		// tableName = args[0];
		// }
		// tableName = normalize(tableName);
		// init();
		// geneAll(tableName);
		geneAllFromExcel("test.xls");
	}

	public static void geneAllFromExcel(String fileName) {
		init();
		File file = new File(fileName);
		String[][] result = null;
		try {
			result = ExcelReader.getData(file, 2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 生成cnof
		geneAllConf(result);

		// 生成controller
		geneAllController(result);

		// TODO 生成js文件
		// List<ExcelObject> excelObjects = ExcelReader.geneExcelObject(result);
		// for (ExcelObject excelObject : excelObjects) {
		// geneFromObject(excelObject);
		// }
	}

	private static void geneAllController(String[][] result) {
		Map<String, List<ExcelObject>> controllerMap = ExcelReader
				.geneControllerMap(result);
		for (Entry<String, List<ExcelObject>> entry : controllerMap.entrySet()) {
			String controller = entry.getKey();
			List<ExcelObject> objects = entry.getValue();
			String packageName = objects.get(0).menuGroupName;

			Map<String, Object> vo = new HashMap<String, Object>();
			vo.put("controller", controller);
			vo.put("packageName", packageName);
			vo.put("objects", objects);
			vo.put("javaPackageName",
					PropertyUtil.getProperty("javaPackageName"));
			String filePath = PropertyUtil.getProperty("javaFloder")
					+ "controller/" + controller + "Controller.java";
			gene(vo, ControllerFtl, filePath);

			// 对每一个object，生成单独的dao
			for (ExcelObject excelObject : objects) {
				geneModel(excelObject);
			}
		}
		// 生成JFConfig.java
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("controllerMap", controllerMap);
		vo.put("javaPackageName", PropertyUtil.getProperty("javaPackageName"));
		String filePath = PropertyUtil.getProperty("javaFloder")
				+ "JFConfig.java";
		gene(vo, JFConfigFtl, filePath);
	}

	private static void geneAllConf(String[][] result) {
		// 生成配置文件
		Map<String, Map<String, List<ExcelObject>>> map = ExcelReader
				.geneExcelObjectMap(result);
		for (Entry<String, Map<String, List<ExcelObject>>> out : map.entrySet()) {
			geneMenuConf(out.getKey(), out.getValue());
			// TODO 针对不同文件生成配置

			// for (Entry<String, List<ExcelObject>> in : out.getValue()
			// .entrySet()) {
			//
			// }
		}

		// 生成import文件
		List<ExcelObject> objets = ExcelReader.geneExcelObject(result);
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("objects", objets);
		String filePath = PropertyUtil.getProperty("jsFloder")
				+ "Zjedu.import.js";
		gene(vo, ZjeduImportFtl, filePath);
	}

	private static void geneMenuConf(String funcGroupName,
			Map<String, List<ExcelObject>> funcGroupMap) {
		Map<String, Object> vo = new HashMap<String, Object>();
		String funcGroup = null;
		for (Entry<String, List<ExcelObject>> entry : funcGroupMap.entrySet()) {
			funcGroup = entry.getValue().get(0).functionGroup;
		}
		vo.put("funcGroupName", funcGroupName);
		vo.put("funcGroup", funcGroup);
		vo.put("funcGroupMap", funcGroupMap);
		String filePath = PropertyUtil.getProperty("confFolder") + "menu-"
				+ funcGroupName + ".xml";
		gene(vo, MenuFtl, filePath);
	}

	private static void geneFromObject(ExcelObject excelObject) {
		init();
		System.out.println("=====================================");
		System.out.println(excelObject);
		System.out.println("=====================================");
		geneModel(excelObject);
		geneController(excelObject);
		geneView(excelObject);
		geneForm(excelObject);
		geneConf(excelObject);
		System.out.println("end:" + "=========================\n\n");
	}

	private static void geneAll(String tableName) {
		// init();
		// geneModel(tableName);
		// geneController(tableName);
		// geneView(tableName);
		// geneForm(tableName);
		// geneConf(tableName);
	}

	private static void geneConf(ExcelObject excelObject) {
		String tableName = normalize(excelObject.model);
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("excelObject", excelObject);
		String filePath = PropertyUtil.getProperty("confFolder")
				+ tableName.toLowerCase() + "/" + tableName + "Config.txt";
		gene(vo, ConfFtl, filePath);

	}

	private static void geneForm(ExcelObject excelObject) {
		String tableName = normalize(excelObject.model);
		List<DBMeta> metas = SqlUtil.getMetadata(tableName);
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("excelObject", excelObject);
		vo.put("metas", metas);
		String filePath = PropertyUtil.getProperty("jsFloder")
				+ tableName.toLowerCase() + "/" + tableName + "Form.js";
		gene(vo, FormFtl, filePath);
	}

	private static void geneView(ExcelObject excelObject) {
		String tableName = normalize(excelObject.model);
		List<DBMeta> metas = SqlUtil.getMetadata(tableName);
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("excelObject", excelObject);
		vo.put("metas", metas);
		String filePath = PropertyUtil.getProperty("jsFloder")
				+ tableName.toLowerCase() + "/" + tableName + "View.js";
		gene(vo, ViewFtl, filePath);
	}

	private static void geneController(ExcelObject excelObject) {
		Map<String, Object> vo = new HashMap<String, Object>();
		String tableName = normalize(excelObject.model);
		vo.put("tableName", tableName);
		vo.put("tableNameLowerCase", tableName.toLowerCase());
		vo.put("excelObject", excelObject);
		vo.put("javaPackageName", PropertyUtil.getProperty("javaPackageName"));
		String filePath = PropertyUtil.getProperty("javaFloder")
				+ tableName.toLowerCase() + "/" + tableName + "Controller.java";
		gene(vo, ControllerFtl, filePath);
	}

	private static void geneModel(ExcelObject excelObject) {
		Map<String, Object> vo = new HashMap<String, Object>();
		String tableName = normalize(excelObject.model);
		vo.put("tableName", tableName);
		vo.put("javaPackageName", PropertyUtil.getProperty("javaPackageName"));
		String filePath = PropertyUtil.getProperty("javaFloder") + "dao" + "/"
				+ tableName + ".java";
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
		if (inited == false) {
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
			inited = true;
		}
	}
}
