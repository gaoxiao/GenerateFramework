package ${javaPackageName};

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

import ${javaPackageName}.dao.*;
import ${javaPackageName}.controller.*;

public class JFConfig extends JFinalConfig {

	public void configRoute(Routes me) {
	<#list controllerMap?keys as key>
		me.add("/jf/${key}", ${key}Controller.class);
	</#list>
	}

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("jf_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
	<#list controllerMap?keys as key>
		<#assign objects=controllerMap[key]>
		<#list objects as obj>
		arp.addMapping("${obj.model}", ${obj.model?cap_first}.class); 
		</#list>
	</#list>
	}

	public void configInterceptor(Interceptors me) {
	}

	public void configHandler(Handlers me) {
	}

	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}