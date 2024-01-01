package edu.najah.cap.Delete;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

public interface IDelete {

    public void delete(String userId)throws SystemBusyException, BadRequestException, NotFoundException;
}
