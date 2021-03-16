package edu.bbte.projectbluebook.datacatalog.assets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AssetServiceException extends RuntimeException {
    public AssetServiceException(String message) {
        super(message);
    }
}
