package pl.kurs.mapper;

import org.mapstruct.Mapper;
import pl.kurs.model.upload.UploadProgress;
import pl.kurs.model.upload.dto.UploadProgressDto;

@Mapper(componentModel = "spring")
public interface UploadStatusMapper {

    UploadProgressDto fromEntityToDto(UploadProgress uploadProgress);


}
