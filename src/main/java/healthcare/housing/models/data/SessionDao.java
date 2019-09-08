package healthcare.housing.models.data;

import healthcare.housing.models.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SessionDao extends CrudRepository<Session, Integer> {

}
