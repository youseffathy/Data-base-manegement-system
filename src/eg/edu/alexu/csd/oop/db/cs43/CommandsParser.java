package eg.edu.alexu.csd.oop.db.cs43;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.management.Query;

public class CommandsParser {
	private String tableName;
	private List<String> columns;
	private String[] conditons;
	private List<String> values;
	private List<String> datatypes;
	private int Queryno;

	public String[] ReturnString() {
		return null;
	}

	public String[] validateCommand(String command) {
		command = command.replaceAll("(\\s*([=]{1,})\\s*)", "=");
		String pattern = "((\\s*([(]{1})\\s*)|([\\s,\\s]{1,})|(([)]{1})\\s*)|([\\s=\\s]{1,}))(?=([^']*'[^']*')*[^']*$)";
		// String pattern =
		// "(^\\s+)|(\\s*([(]{1})\\s*)|([\\s,\\s]{1,})|(([)]{1})\\s*)|(\\s*([=]{1,})\\s*)";
		Pattern pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		String[] strs = pat.split(command.trim()); // removed trailing and leading spaces

		if (strs[0].equalsIgnoreCase("create")) {
			if (strs[1].equalsIgnoreCase("database")) {
				if (strs.length == 3) {
					tableName = strs[2];
					Queryno = 4;
					return new String[] { "create", "database", strs[2] };
				}
			} else if (strs[1].equalsIgnoreCase("table")) {
				tableName = strs[2];
				if (strs.length < 4) {
					Queryno = 0;
					columns = null;
					datatypes = null;
					return null;
				}
				columns = new LinkedList<String>();
				for (int i = 3; i < strs.length; i = i + 2) {
					columns.add(strs[i]);
				}
				datatypes = new LinkedList<String>();
				for (int i = 4; i < strs.length; i = i + 2) {
					if (strs[i].equalsIgnoreCase("varchar") || strs[i].equalsIgnoreCase("int")) {
						datatypes.add(strs[i]);
					} else {
						columns = null;
						datatypes = null;
						Queryno = 0;
						return null;
					}

				}
				Queryno = 5;
				if (columns.size() != datatypes.size()) {

					columns = null;
					datatypes = null;
					Queryno = 0;
				}
				return strs;
			}
		} else if (strs[0].equalsIgnoreCase("drop")) {
			if (strs[1].equalsIgnoreCase("database")) {
				if (strs.length == 3) {
					tableName = strs[2];
					Queryno = 6;
					return new String[] { "drop", "database", strs[2] };

				}
			} else if (strs[1].equalsIgnoreCase("table")) {
				if (strs.length == 3) {
					tableName = strs[2];
					Queryno = 7;
					return new String[] { "drop", "database", strs[2] };
				}

			}

		} else if (strs[0].equalsIgnoreCase("insert")) {

			if (strs[1].equalsIgnoreCase("into")) {
				tableName = strs[2];
				columns = new LinkedList<>();
				values = new LinkedList<>();
				if (strs[3].equalsIgnoreCase("values")) {
					for (int i = 4; i < strs.length; i++) {
						values.add(strs[i]);
					}
				} else {
					for (int i = 3; i < strs.length && !strs[i].equalsIgnoreCase("values"); i++) {
						columns.add(strs[i]);

					}
					if (columns.size() + 3 < strs.length && strs[columns.size() + 3].equalsIgnoreCase("values")) {
						for (int i = columns.size() + 4; i < strs.length; i++) {
							values.add(strs[i]);
						}
					}
				}
				Queryno = 1;
				if ((values.size() != 0 && columns.size() != 0 && values.size() == columns.size())) {
					
				}else if(values.size() != 0 && columns.size() == 0){
					
				}else {
					columns = null;
					values = null;
					Queryno = 0;
					return null;
				}

			}

		} else if (strs[0].equalsIgnoreCase("update")) {
			tableName = strs[1];
			if (strs[2].equalsIgnoreCase("set")) {
				//
				columns = new LinkedList<String>();
				values = new LinkedList<String>();
				boolean where = true;
				for (int i = 3; i < strs.length && !strs[i].equalsIgnoreCase("where"); i = i + 2) {
					if (strs[i].equalsIgnoreCase("where") || strs[i + 1].equalsIgnoreCase("where")) {
						where = false;
						columns = null;
						values = null;
						break;
					}
					columns.add(strs[i]);
					values.add(strs[i + 1]);
					Queryno = 2;

				}
				conditons = null;
				if (3 + 2 * columns.size() < strs.length && strs[3 + 2 * columns.size()].equalsIgnoreCase("where")) {

					String allConditions = command.split("(?i)where")[1].trim();
					allConditions = allConditions.replaceAll("(\\s*([<]{1,})\\s*)", " < ");
					allConditions = allConditions.replaceAll("(\\s*([=]{1,})\\s*)", " = ");
					allConditions = allConditions.replaceAll("(\\s*([>]{1,})\\s*)", " > ");
					conditons = allConditions.split("\\s+");
					Queryno = 2;
				}
				if (!where || columns.size() != values.size()) {
					conditons = null;
					columns = null;
					Queryno = 0;
					values = null;
				}

			}

		} else if (strs[0].equalsIgnoreCase("delete")) {
			if (strs[1].equalsIgnoreCase("from")) {
				tableName = strs[2];
				if (strs.length == 3) {
					conditons = null;
					Queryno = 3;
				} else if (3 < strs.length && strs[3].equalsIgnoreCase("where")) {
					String allConditions = command.split("(?i)where")[1].trim();
					allConditions = allConditions.replaceAll("(\\s*([<]{1,})\\s*)", " < ");
					allConditions = allConditions.replaceAll("(\\s*([=]{1,})\\s*)", " = ");
					allConditions = allConditions.replaceAll("(\\s*([>]{1,})\\s*)", " > ");
					conditons = allConditions.split("\\s+");
					Queryno = 3;
				}
			}
		} else if (strs[0].equalsIgnoreCase("select")) { // string splitting problem with spaces
			if (strs[0].equalsIgnoreCase("select")) {
				if (strs[1].equalsIgnoreCase("*")) {
					if (strs[2].equalsIgnoreCase("from")) {
						tableName = strs[3];
						if (strs.length == 4) {
							columns = null;
							conditons = null;
							Queryno = 15;
						} else if (strs[4].equalsIgnoreCase("where")) {
							columns = null;
							String allConditions = command.split("(?i)where")[1].trim();
							allConditions = allConditions.replaceAll("(\\s*([<]{1,})\\s*)", " < ");
							allConditions = allConditions.replaceAll("(\\s*([=]{1,})\\s*)", " = ");
							allConditions = allConditions.replaceAll("(\\s*([>]{1,})\\s*)", " > ");
							conditons = allConditions.split("\\s+");
							Queryno = 15;
						}
					}
				} else {
					columns = new LinkedList<String>();
					int i;
					for (i = 1; i < strs.length && !strs[i].equalsIgnoreCase("from"); i++) {

						columns.add(strs[i]);
					}

					try {
						if (strs[i].equalsIgnoreCase("from")) {
							tableName = strs[i + 1];
							if (strs.length == i + 2) {
								conditons = null;
								Queryno = 15;
								// return columns in array
							} else if (strs[i + 2].equalsIgnoreCase("where")) {
								String allConditions = command.split("(?i)where")[1].trim();
								allConditions = allConditions.replaceAll("(\\s*([<]{1,})\\s*)", " < ");
								allConditions = allConditions.replaceAll("(\\s*([=]{1,})\\s*)", " = ");
								allConditions = allConditions.replaceAll("(\\s*([>]{1,})\\s*)", " > ");
								conditons = allConditions.split("\\s+");
								Queryno = 15;

							}
						}
					} catch (Exception e) {
						Queryno = 0;

					}
				}
			}
			return strs;
		}

		return null;
	}

	public String getTableNameOrDatabase() {
		return tableName;
	}

	public String[] getColumns() {
		try {
			String[] s = new String[columns.size()];
			s = columns.toArray(s);
			return s;
		} catch (Exception e) {
			return null;
		}
	}

	public String[] getconditions() {
		return conditons;
	}

	public String[] getValues() {
		try {
			String[] s = new String[values.size()];
			s = values.toArray(s);
			return s;
		} catch (Exception e) {
			return null;
		}
	}

	public String[] getTypes() {
		try {
			String[] s = new String[datatypes.size()];
			s = datatypes.toArray(s);
			return s;
		} catch (Exception e) {
			return null;
		}
	}

	public int getQueryNo() {
		return Queryno;

	}
}
