package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



import org.junit.Assert;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.cs43.concreteclass.MyDatabase;

public  class testingXml {

	public static void main(String[] args) throws Exception {

		Object[][] values = new Object[][] {{15,10,2},{4,3,6},{2,8,9}};
		String[]allcolumns = new String[] {"column1","column2","column3"};
		String[]allTypes = {"Column1"};
		ColumnsManipulation columnsManipulation = new ColumnsManipulation(values, allcolumns, allTypes);
		values  =columnsManipulation.getSelectedColumns();
		for(int i = 0 ;i<values.length;i++) {
			for(int j = 0 ;j<values[0].length;j++) {
				System.out.println(values[i][j]);
			}
			
		}
		

	}

}
