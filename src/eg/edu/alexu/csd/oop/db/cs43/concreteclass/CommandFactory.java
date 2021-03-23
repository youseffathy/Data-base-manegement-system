package eg.edu.alexu.csd.oop.db.cs43.concreteclass;

public interface CommandFactory {
	public void  setcommand(String command,boolean dropIfExists ,String databaseName);
	public void getFunction() throws Exception;
}
