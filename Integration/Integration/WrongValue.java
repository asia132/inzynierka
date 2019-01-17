package Integration;

public class WrongValue extends Exception{
	private String message;
	public WrongValue(String message){
		this.message = message;
	}
	public WrongValue(){
		this.message = "GSL error.";
	}
	public String getErrorMessage(){
		return message;
	}
}