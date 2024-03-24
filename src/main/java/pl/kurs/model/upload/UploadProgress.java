package pl.kurs.model.upload;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static pl.kurs.model.upload.UploadStatus.*;

@Entity
@Getter
@Setter
public class UploadProgress {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int linesToBeProcessed;
    private int linesProcessed;

    @Enumerated(EnumType.STRING)
    private UploadStatus status;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public UploadProgress() {
        this.linesToBeProcessed = 0;
        this.linesProcessed = 0;
        this.status = IN_PROGRESS;
        this.startTime = LocalDateTime.now();
        this.finishTime = null;
    }
}
