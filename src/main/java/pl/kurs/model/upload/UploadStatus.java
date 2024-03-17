package pl.kurs.model.upload;

import lombok.Getter;

@Getter
public enum UploadStatus {

    IN_PROGRESS("In progress."),
    FINISHED("Finished."),
    FAILED("Failed to upload.");

    private final String status;

    UploadStatus(String status) {
        this.status = status;
    }
}
