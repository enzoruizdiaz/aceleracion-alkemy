package ProjectAlkemy.exception;

public class CommentNotAuthorizatedException extends Exception{

	private static final long serialVersionUID = 4648485082088399393L;

	public CommentNotAuthorizatedException(String s) {
        super(s);
    }
}
