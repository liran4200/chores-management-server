package playground.logic.data;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.dal.ActivityDao;
import playground.aop.logger.MyLog;
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
	
	@Autowired
	public JpaActivityService(ActivityDao activitiesDal, ConfigurableApplicationContext spring) {
		this.activitiesDal = activitiesDal;
		this.spring = spring;
		
	}
	
	@PostConstruct
	public void init() {
		this.activityHistoryBoard = new ElementEntity();
		activityHistoryBoard.setAttributes(new HashMap<String,Object>());
	}
	
	@Override
	@Transactional
	@MyLog
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
