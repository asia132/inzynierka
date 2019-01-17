package least_squares_fitting;

public class GSLErrors extends Exception{
	private String message;
	public GSLErrors(String message){
		this.message = message;
	}
	public GSLErrors(){
		this.message = "Value error.";
	}
	public String getErrorMessage(){
		return message;
	}
}