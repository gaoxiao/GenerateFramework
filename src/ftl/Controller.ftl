package ${javaPackageName}.${tableNameLowerCase};

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

@SuppressWarnings("unchecked")
public class ${tableName}Controller extends Controller {

	public void all() {
		int limit = getParaToInt("limit");
		int start = getParaToInt("start");

		// ext3中和ext4不一样，没有page参数
		int page = start / limit + 1;
		// 按照日期倒序排列
		renderJson(${tableName}.dao.paginate(page, limit, "select *",
				"from ${tableNameLowerCase} order by date desc"));
	}

	public void search() {
		int limit = getParaToInt("limit");
		int start = getParaToInt("start");

		int page = start / limit;
		String search = getPara("search");
		// 按照日期倒序排列
		String searchSql = "from ${tableNameLowerCase} where title like '%" + search
				+ "%' or content like '%" + search + "%' order by date desc";
		renderJson(${tableName}.dao.paginate(page, limit, "select *", searchSql));
	}

	public void detail() {
		String id = getPara("id");
		${tableName} ${tableNameLowerCase} = ${tableName}.dao.findById(id);
		Map m = new HashMap();
		m.put("data", ${tableNameLowerCase});
		m.put("success", true);
		renderJson(m);
	}

	public void save() {
		String id = getPara("${tableNameLowerCase}.id");
		if (id == null) {
			${tableName} ${tableNameLowerCase} = getModel(${tableName}.class);
			String uuid = UUID.randomUUID().toString();
			${tableNameLowerCase}.set("id", uuid);
			${tableNameLowerCase}.save();
			renderText("{success:true}");
		} else {
			getModel(${tableName}.class).update();
			renderText("{success:true}");
		}
	}

	public void detailContent() {
		String id = getPara("id");
		${tableName} ${tableNameLowerCase} = ${tableName}.dao.findById(id);
		renderText(${tableNameLowerCase}.getStr("content"));
	}

	public void delete() {
		String[] ids = getParaValues("ids");
		boolean success = false;
		for (String id : ids) {
			success = ${tableName}.dao.deleteById(id);
		}

		if (success) {
			renderText("{success:true}");
		} else {
			renderText("{success:false}");
		}

	}

	public void uploadJson() {
		// 上传文件
		UploadFile uploadFile = getFile();
		// 返回json对象
		// JSONObject obj = new JSONObject();
		Map m = new HashMap();
		m.put("error", 0);
		m.put("url", "../../upload/" + uploadFile.getFileName());
		renderJson(m);
	}
}
