package us.awardspace.tekkno.xtrimlogy.uploads.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import us.awardspace.tekkno.xtrimlogy.uploads.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {
    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(Long id);

    void removeById(Long id);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;
    }
}
