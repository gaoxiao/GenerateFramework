package ${javaPackageName}.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import ${javaPackageName}.dao.*;

@SuppressWarnings("unchecked")
public class ${controller}Controller extends Controller {

<#list objects as obj>
	public void ${obj.menuName}() {
		int limit = getParaToInt("limit");
		int start = getParaToInt("start");
		int page = start / limit + 1;
		// 按照日期倒序排列
		renderJson(${obj.model?cap_first}.dao.paginate(page, limit, "select *",
				"from ${obj.model} order by date desc"));
	}
	
</#list>
}
