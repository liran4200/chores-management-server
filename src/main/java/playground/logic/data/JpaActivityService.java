package playground.logic.data;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.dal.ActivityDao;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ActivityInvokeFailedException;
import playground.logic.services.ActivityService;
import playground.plugins.Plugin;

@Service
public class JpaActivityService implements ActivityService {
	
	private ActivityDao activitiesDal;
	private ConfigurableApplicationContext spring;
	private ElementEntity activityHistoryBoard;
	
	private ObjectMapper jackson;

	@Autowired
	public JpaActivityService(ActivityDao activitiesDal, ConfigurableApplicationContext spring) {
		this.activitiesDal = activitiesDal;
		this.spring = spring;
		this.jackson = new ObjectMapper();
		this.activityHistoryBoard = new ElementEntity();
		activityHistoryBoard.setAttributes(new HashMap<String,Object>());
	}
	
	@Override
	@Transactional
	public Object invokeActivity(ActivityEntity activity) throws ActivityInvokeFailedException {
		Object content;
		try {
			String type = activity.getType();
			Plugin plugin = (Plugin) spring.getBean(Class.forName("playground.plugins." + type + "Plugin"));
			content = plugin.execute(activity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.activitiesDal.save(activity);
		return content;
	}
}
