package us.awardspace.tekkno.xtrimlogy.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
