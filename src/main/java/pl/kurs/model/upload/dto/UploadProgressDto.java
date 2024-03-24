package pl.kurs.model.upload.dto;

import lombok.Data;
import pl.kurs.model.upload.UploadStatus;

import java.time.LocalDateTime;

@Data
public class UploadProgressDto {

    private int linesToBeProcessed;
    private int linesProcessed;
    private UploadStatus status;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}
