package com.e3mall.testFreeMarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class testFreemarker {
	/*
	 * freemarker使用语法  0.普通类型 ${变量名}
	 * 				   1.pojo ${对象.属性} 
	 * 				   2.List <#list 集合 as 每一项> <#list>
	 *                 3.list中下标   <每一项_index>
	 *                 4.if <#if 条件>
	 *                 		<#else>
	 *                 		</#if>
	 *                 5.日期  ${变量名?date}/?time/?datatime/?string('自定义格式')
	 *                 6.null值处理  1) ${变量名!"默认值"} 2)用if判断  <#if var ??>有值<#else>null值</#if>
	 *                 7.页眉页脚   <#include 模板文件名.ftl>
	 */
	@Test
	public void getFile() throws Exception{
		//	第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
		Configuration configuration = new Configuration(Configuration.getVersion());
		//	第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(new File("F:/study/e3mall/e3mall/git/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//	第三步：设置模板文件使用的字符集。一般就是utf-8.
		configuration.setDefaultEncoding("utf-8");
		//	第四步：加载一个模板，创建一个模板对象。
		Template template = configuration.getTemplate("student.ftl");
		//	第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Map dataModel = new HashMap<>();
		//向集合中添加数据
		dataModel.put("hello", "hello freemarker");
		Student student = new Student(1, "张三", 19, "火星");
		dataModel.put("student", student);
		List<Student> stuList = new ArrayList<>();  
        stuList.add(new Student(1, "小米", 20, "北京市昌平区小米科技有限公司"));  
        stuList.add(new Student(2, "小米2", 21, "北京市昌平区小米科技有限公司"));  
        stuList.add(new Student(3, "小米3", 22, "北京市昌平区小米科技有限公司"));  
        stuList.add(new Student(4, "小米4", 23, "北京市昌平区小米科技有限公司"));  
        stuList.add(new Student(5, "小米5", 24, "北京市昌平区小米科技有限公司"));  
        stuList.add(new Student(6, "小米6", 25, "北京市昌平区小米科技有限公司"));  
        dataModel.put("stuList", stuList); 
        dataModel.put("date", new Date());
       // dataModel.put("var", "不是null值了");
		//	第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer writer = new FileWriter(new File("E:/temp/freemarker/student.html"));
		//	第七步：调用模板对象的process方法输出文件。
		template.process(dataModel, writer);
		//	第八步：关闭流。
		writer.close();
	}
}
