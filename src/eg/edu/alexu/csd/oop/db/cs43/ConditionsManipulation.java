package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class ConditionsManipulation {
	private String[] allcolumns;
	private String[] conditions;
	private Object[][] values;
	private String[] allTypes;

	public ConditionsManipulation(Object[][] values, String[] conditions, String[] allcolumns, String[] allTypes) {
		this.values = values;
		this.conditions = conditions;
		this.allcolumns = allcolumns;
		this.allTypes = allTypes;
	}

	public Object[][] getArrayAfterCondiotions() throws Exception {
		List<List<Object>> objects = new LinkedList<>();
		for (int i = 0; i < values.length; i++) {
			Map<String, Object> map = new HashMap<>();
			for (int j = 0; j < values[0].length; j++) {
				map.put(allcolumns[j], values[i][j]);
			}
			//
			boolean addThisRowOrnot = false;
			int FoundNot = 0;
			int foundOR = 0;
			int foundAnd = 0;
			boolean secondOPerandINlogic = false;
			boolean foundOperation = false;
			for (int j = 0; j < conditions.length; j++) {
				
				String operand = conditions[j];
				String operation = conditions[j + 1];
				String value = conditions[j + 2];
			//	 System.out.println(operand);
			//	 System.out.println(operation);
			//	 System.out.println(value);
				if (operation.equals("=") || operation.equals("<") || operation.equals(">")) {
					
					foundOperation = true;
					// check if there is a column with this name or not if not throw exception
					int index = getIndexOfcolumn(operand);
					if (index == -1) {
						throw new Exception();
					}
					// CHECKING type if not throw exception

					boolean ValueMatchedWithType = false;
					String ComparedString = "";
					int comparedint = 0;
					String currenttype = allTypes[index];
					if (currenttype.equals("integer")) {
						if (value.contains("'")) {
							throw new Exception();
						} else {
							comparedint = Integer.valueOf(value); // if it cant parse it throws exception
							ValueMatchedWithType = true;
						}
					} else if (currenttype.equals("string")) {
						if (value.startsWith("'") && value.endsWith("'")) {
							ValueMatchedWithType = true;
							ComparedString = String.valueOf(value);
						} else {
							throw new Exception();
						}
					}
					// value Matched With Type
					// now comparing the values

					if (operation.equals("=")) {
						
						if (currenttype.equals("string")) {
							if (ComparedString.equalsIgnoreCase(String.valueOf(map.get(allcolumns[index])))) {
								addThisRowOrnot = true;
								
							} else {
								addThisRowOrnot = false;
							}
						} else if (currenttype.equals("integer")) {
							if (comparedint == Integer.valueOf(String.valueOf(map.get(allcolumns[index])))) {
								addThisRowOrnot = true;
							} else {
								addThisRowOrnot = false;
							}
						}
					} else if (operation.equals("<")) {

						if (currenttype.equals("string")) {
							if (ComparedString.compareTo(String.valueOf(map.get(allcolumns[index]))) > 0) {
								addThisRowOrnot = true;
							} else {
								addThisRowOrnot = false;
							}
						} else if (currenttype.equals("integer")) {
							if (comparedint > Integer.valueOf(String.valueOf(map.get(allcolumns[index])))) {
								addThisRowOrnot = true;
							} else {
								addThisRowOrnot = false;
							}
						}
					} else if (operation.equals(">")) {

						if (currenttype.equals("string")) {
							if (ComparedString.compareTo(String.valueOf(map.get(allcolumns[index]))) < 0) {
								addThisRowOrnot = true;
							} else {
								addThisRowOrnot = false;
							}
						} else if (currenttype.equals("integer")) {
							if (comparedint < Integer.valueOf(String.valueOf(map.get(allcolumns[index])))) {
								addThisRowOrnot = true;
							} else {
								addThisRowOrnot = false;
							}
						}
					}
					j = j + 2;
					
					
				}
				if (operand.equalsIgnoreCase("not") || FoundNot == 1) {
					// System.out.println("not");
					if (FoundNot == 1) {
						FoundNot = 0;
						addThisRowOrnot = !addThisRowOrnot;
						foundOperation = false;
					} else {
						FoundNot = 1;
					}
				} else if (operand.equalsIgnoreCase("or") || foundOR == 1) {
					foundOperation = false;
					// System.out.println("or");
					if (foundOR == 1) {
						foundOR = 0;
						addThisRowOrnot = secondOPerandINlogic || addThisRowOrnot;

					} else {
						foundOR = 1;
						secondOPerandINlogic = addThisRowOrnot;
					}

				} else if (operand.equalsIgnoreCase("and") || foundAnd == 1) {
					foundOperation = false;
					// System.out.println("and");
					if (foundAnd == 1) {
						foundAnd = 0;
						addThisRowOrnot = secondOPerandINlogic && addThisRowOrnot;

					} else {
						foundAnd = 1;
						secondOPerandINlogic = addThisRowOrnot;
					}
				} else if (!foundOperation) {
					throw new Exception();
				}
				// System.out.println(addThisRowOrnot);
			}
			if (foundAnd == 1) {
				addThisRowOrnot = secondOPerandINlogic && addThisRowOrnot;
				foundAnd = 0;
			} else if (foundOR == 1) {
				addThisRowOrnot = secondOPerandINlogic || addThisRowOrnot;
				foundAnd = 0;
			}
			if (addThisRowOrnot) {
				List<Object> s = new LinkedList<>();
				s = Arrays.asList(values[i]);
				objects.add(s);
			}
			// System.out.println(addThisRowOrnot);
		}
		
		if (objects.size() != 0) {
			Object[][] returnedrows = new Object[objects.size()][objects.get(0).size()];
			for (int i = 0; i < objects.size(); i++) {
				returnedrows[i] = objects.get(i).toArray(returnedrows[i]);
			}
			return returnedrows;
		}	
		return null;
	}

	private int getIndexOfcolumn(String s) {
		for (int i = 0; i < allcolumns.length; i++) {
			if (allcolumns[i].equalsIgnoreCase(s)) {
				
				return i;
			}
		}
		return -1;
	}
}
