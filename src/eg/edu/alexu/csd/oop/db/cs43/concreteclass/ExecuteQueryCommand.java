package eg.edu.alexu.csd.oop.db.cs43.concreteclass;

import java.sql.SQLException;

public interface ExecuteQueryCommand {
	public Object[][] execute() throws SQLException;
}
