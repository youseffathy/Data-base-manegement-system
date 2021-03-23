package eg.edu.alexu.csd.oop.db.cs43;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.core.IsInstanceOf;

import eg.edu.alexu.csd.oop.db.Database;

public class TEst {

	

	public static void main(String[] args) {

		
		String s ="INSERT INTO table_name3 VALUES ('value1', 3,'value3')";;
		String s1 ="CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)";
		String s2 = "column_name1='11111111' COLUMN_NAME2=10  column_name3='333333333' ";
				String s3 = "UPDATE table_name1 SET column_name1='111    11111'   ,    COLUMN_NAME2=22222222, column_name3='3333       33333' WHERE coLUmn_NAME3='VALUE       3'";
		/*	String s3 ="coluMN_NAME2<5 OR coluMN_NAME1=3 AND NOT coluMN_NAME3>5 ";
		String s1 ="CREATE   TABLE   table_name1   (     column_name1 varchar , column_name2    int,  column_name3 varchar)       ";
		String s2 = "create database " ;*/
		String pattern ;
		Pattern pat ;
		s = s.replaceAll("(\\s*([=]{1,})\\s*)", "=");
	    pattern ="((\\s*([(]{1})\\s*)|([\\s,\\s]{1,})|(([)]{1})\\s*)|([\\s=\\s]{1,}))(?=([^']*'[^']*')*[^']*$)"; 
	    
	     pat = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
		  	
	
		  		
		  		
		  		
		  	    
		  		for(String sa : pat.split(s)) {
		  		  	System.out.println(sa);
		  		    }
		  	}
		 //     pattern = "(^\\s+)|(\\s*([(]{1})\\s*)|([\\s*,\\s*]{1,})|(([)]{1})\\s*)";
		 
	}

