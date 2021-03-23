package eg.edu.alexu.csd.oop.db.cs43.commandConcreteClasses;

import java.util.LinkedList;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.Database;

import eg.edu.alexu.csd.oop.db.cs43.concreteclass.CommandFactory;
import eg.edu.alexu.csd.oop.db.cs43.concreteclass.MyDatabase;

public class ClientCommand implements CommandFactory {
	private String command;
	
	private boolean dropIfExists;
	private Database database;
	private String databaseName;

	@Override
	public void setcommand(String command, boolean drop, String databasename) {
		this.dropIfExists = drop;
		this.command = command;
		this.databaseName = databasename;

	}

	@Override
	public void getFunction() throws Exception {
		String pattern = "(^\\s+)|(\\s*([(]{1})\\s*)|([\\s*,\\s*]{1,})|(([)]{1})\\s*)";
		Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		String[] strs = pat.split(command.trim()); // removed trailing and leading spaces

		if (strs[0].equalsIgnoreCase("create")) {
			if (strs[1].equalsIgnoreCase("database")) {
				
				database = MyDatabase.getInstance();
				database.createDatabase(databaseName, dropIfExists);
			} else if (strs[1].equalsIgnoreCase("table")) {
				database.executeStructureQuery(command);
			}
		}else if (strs[0].equalsIgnoreCase("drop")) {
			database.executeStructureQuery(command);
		}
		else if (strs[0].equalsIgnoreCase("delete") || strs[0].equalsIgnoreCase("insert")
				|| (strs[0].equalsIgnoreCase("update"))) {
			database.executeUpdateQuery(command);
		} else if (strs[0].equalsIgnoreCase("select")) {
		database.executeQuery(command);
		}
		

	}

}
