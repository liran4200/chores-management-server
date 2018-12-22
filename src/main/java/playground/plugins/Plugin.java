package playground.plugins;

import playground.logic.EntityComponents.ActivityEntity;

public interface Plugin {
	public Object execute (ActivityEntity command) throws Exception;
}
