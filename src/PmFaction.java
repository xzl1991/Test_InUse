import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class PmFaction {
	
	/*
	 * 这个一个工具类
	 * 自动生成dao和service和action
	 */
	static String srcpath = "D:\\java笔记\\HDEFrameBH\\src";
	static String groupName = "executive";

	static String entityPackageName = "net.houder.entity." + groupName;
	static String daoPackageName = "net.houder.dao." + groupName;
	static String bizPackageName = "net.houder.biz." + groupName;
	static String actionPackageName = "net.houder.action." + groupName;

	/**
	 * @param args
	 */
	static String sDao = srcpath + "\\net\\houder\\dao\\" + groupName + "\\";// dao接口文件路径
	static String sDaoImpl = srcpath + "\\net\\houder\\dao\\" + groupName
			+ "\\impl" + "\\";// dao接口实现类文件路径
	static String sBiz = srcpath + "\\net\\houder\\biz\\" + groupName + "\\";// biz接口文件路径
	static String sBizImpl = srcpath + "\\net\\houder\\biz\\" + groupName
			+ "\\impl" + "\\";// biz接口实现类文件路径
	static String sAction = srcpath + "\\net\\houder\\action\\" + groupName
			+ "\\";// action文件路径

	public static void main(String[] args) {
		String[] entityNames = { "PostType","PostAuthorization"};

		for (String name : entityNames) {
			faction(name);// 实体类名
			
		}
	}

	public static void faction(String className) {
		try {
			Class.forName(entityPackageName + "." + className);

			// dao接口
			File fileDao = new File(sDao + className + "Dao.java");
			if (!fileDao.exists()) {// 文件不存在
				File fileDir = new File(sDao);
				if (!fileDir.exists())
					fileDir.mkdirs();
				FileWriter fw = new FileWriter(sDao + className + "Dao.java");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + daoPackageName + ";");
				bw.newLine();
				bw.newLine();
				bw.write("import net.houder.dao.AbstractDao;");
				bw.newLine();
				bw.write("import " + entityPackageName + "." + className + ";");
				bw.newLine();
				bw.newLine();
				bw.write("public interface " + className
						+ "Dao extends AbstractDao<" + className + ",Long>{");
				bw.newLine();
				bw.newLine();
				bw.write("}");
				bw.close();
			}

			// dao接口实现类
			File fileDaoImpl = new File(sDaoImpl + className + "DaoImpl.java");
			if (!fileDaoImpl.exists()) {// 文件不存在
				File fileDir = new File(sDaoImpl);
				if (!fileDir.exists())
					fileDir.mkdirs();
				FileWriter fw = new FileWriter(sDaoImpl + className
						+ "DaoImpl.java");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + daoPackageName + ".impl;");
				bw.newLine();
				bw.newLine();
				bw.write("import org.springframework.stereotype.Component;");
				bw.newLine();
				bw.newLine();
				bw.write("import net.houder.dao.BaseDao;");
				bw.newLine();
				bw.write("import " + daoPackageName + "." + className + "Dao;");
				bw.newLine();
				bw.write("import " + entityPackageName + "." + className + ";");
				bw.newLine();
				bw.newLine();
				bw.write("@Component(\"" + toLower(className) + "Dao\")");
				bw.newLine();
				bw.write("public class " + className
						+ "DaoImpl extends BaseDao<" + className
						+ "> implements " + className + "Dao {");
				bw.newLine();
				bw.newLine();
				bw.write("}");
				bw.close();
			}

			// biz接口
			File fileBiz = new File(sBiz + className + "Biz.java");
			if (!fileBiz.exists()) {// 文件不存在
				File fileDir = new File(sBiz);
				if (!fileDir.exists())
					fileDir.mkdirs();
				FileWriter fw = new FileWriter(sBiz + className + "Biz.java");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + bizPackageName + ";");
				bw.newLine();
				bw.newLine();
				bw.write("import net.houder.biz.AbstractBiz;");
				bw.newLine();
				bw.write("import " + entityPackageName + "." + className + ";");
				bw.newLine();
				bw.newLine();
				bw.write("public interface " + className
						+ "Biz extends AbstractBiz<" + className + ",Long>{");
				bw.newLine();
				bw.newLine();
				bw.write("}");
				bw.close();
			}

			// biz接口实现类
			File fileBizImpl = new File(sBizImpl + className + "BizImpl.java");
			if (!fileBizImpl.exists()) {// 文件不存在
				File fileDir = new File(sBizImpl);
				if (!fileDir.exists())
					fileDir.mkdirs();
				FileWriter fw = new FileWriter(sBizImpl + className
						+ "BizImpl.java");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + bizPackageName + ".impl;");
				bw.newLine();
				bw.newLine();
				bw.write("import javax.annotation.Resource;");
				bw.newLine();
				bw.newLine();
				bw.write("import org.springframework.stereotype.Component;");
				bw.newLine();
				bw.newLine();
				bw.write("import net.houder.dao.AbstractDao;");
				bw.newLine();
				bw.write("import net.houder.biz.BaseBiz;");
				bw.newLine();
				bw.write("import " + bizPackageName + "." + className + "Biz;");
				bw.newLine();
				bw.write("import " + entityPackageName + "." + className + ";");
				bw.newLine();
				bw.newLine();
				bw.write("@Component(\"" + toLower(className) + "Biz\")");
				bw.newLine();
				bw.write("public class " + className
						+ "BizImpl extends BaseBiz<" + className
						+ "> implements " + className + "Biz {");
				bw.newLine();
				bw.newLine();
				bw
						.write("	@Resource(name = \"" + toLower(className)
								+ "Dao\")");
				bw.newLine();
				bw.write("	public void setDao(AbstractDao<" + className
						+ ",Long> dao) {");
				bw.newLine();
				bw.write("		super.dao = dao;");
				bw.newLine();
				bw.write("	}");
				bw.newLine();
				bw.newLine();
				bw.write("}");
				bw.close();
			}

			// action文件
			File fileAction = new File(sAction + className + "Action.java");
			if (!fileAction.exists()) {// 文件不存在
				File fileDir = new File(sAction);
				if (!fileDir.exists())
					fileDir.mkdirs();
				FileWriter fw = new FileWriter(sAction + className
						+ "Action.java");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("package " + actionPackageName + ";");
				bw.newLine();
				bw.newLine();
				bw.write("import javax.annotation.Resource;");
				bw.newLine();
				bw.newLine();
				bw
						.write("import org.springframework.context.annotation.Scope;");
				bw.newLine();
				bw.write("import org.springframework.stereotype.Component;");
				bw.newLine();
				bw.newLine();
				bw.write("import net.houder.action.BaseAction;");
				bw.newLine();
				bw.write("import " + bizPackageName + "." + className + "Biz;");
				bw.newLine();
				bw.write("import " + entityPackageName + "." + className + ";");
				bw.newLine();
				bw.newLine();
				bw.write("@Component(\"" + toLower(className) + "Action\")");
				bw.newLine();
				bw.write("@Scope(\"prototype\")");
				bw.newLine();
				bw.write("public class " + className
						+ "Action extends BaseAction {");
				bw.newLine();
				bw.newLine();
				bw.write("	private " + className + " " + toLower(className)
						+ ";");
				bw.newLine();
				bw.newLine();
				bw.write("	public " + className + " get" + className + "() {");
				bw.newLine();
				bw.write("		return " + toLower(className) + ";");
				bw.newLine();
				bw.write("	}");
				bw.newLine();
				bw.newLine();
				bw.write("	public void set" + className + "(" + className + " "
						+ toLower(className) + ") {");
				bw.newLine();
				bw.write("		this." + toLower(className) + " = "
						+ toLower(className) + ";");
				bw.newLine();
				bw.write("	}");
				bw.newLine();
				bw.newLine();
				bw
						.write("	@Resource(name = \"" + toLower(className)
								+ "Biz\")");
				bw.newLine();
				bw.write("	private " + className + "Biz " + toLower(className)
						+ "Biz;");
				bw.newLine();
				bw.newLine();
				bw.write("	@Override");
				bw.newLine();
				bw.write("	public String execute() throws Exception {");
				bw.newLine();
				bw.newLine();
				bw.write("		return SUCCESS;");
				bw.newLine();
				bw.write("	}");
				bw.newLine();
				bw.newLine();
				bw.write("}");
				bw.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 转小写（第一个字符转小写）
	 * 
	 * @param str
	 * @return
	 */
	public static String toLower(String str) {
		String sNum = "";
		sNum = str.substring(0, 1).toLowerCase();
		sNum = sNum + str.substring(1);
		return sNum;
	}

}
