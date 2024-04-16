package framework;
import java.util.List;

public interface Mapper<IK, IV, OK, OV> {
	List<Pair<OK, OV>> map(IK key, IV value);
}
