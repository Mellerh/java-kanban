package exception;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String massage, IOException e) {
        super(massage, e);
    }

}
