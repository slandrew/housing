package healthcare.housing.models.data;

import healthcare.housing.models.Posting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PostingDao extends CrudRepository<Posting, Integer> {
}
