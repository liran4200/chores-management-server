package playground.aop.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {

	private Log log = LogFactory.getLog(LoggerAspect.class);

	@Around("@annotation(playground.aop.logger.MyLog)")
	public Object log (ProceedingJoinPoint jp) throws Throwable {
		String targetClassName = jp.getTarget().getClass().getSimpleName();
		String methodName = jp.getSignature().getName();
		String output = "**************** " + targetClassName + "." + methodName + "()";
		log.info(output + " - begin");
		
		try {
			Object rv = jp.proceed();
			log.info(output + " - ended successfully");
			return rv;
		} catch (Throwable e) {
			log.info(output + " - ended with errors: " + e.getClass().getName());
			
			throw e;
		}
	}
	
}
