package exception;

import java.io.IOException;

public class ManagerLoadException extends RuntimeException {

    public ManagerLoadException(String massage, IOException e) {
        super(massage, e);
    }

}
