package edu.najah.cap.Delete;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

public class DeletContext {
    private IDelete deleteContext;

    public void setContext(IDelete option){
        this.deleteContext=option;
    }
    public void getContext(String userid) throws SystemBusyException, BadRequestException, NotFoundException {
        deleteContext.delete(userid);
    }
}
