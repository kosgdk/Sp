package database.unitils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.database.DatabaseUnitils;
import org.unitils.dbmaintainer.structure.impl.DefaultConstraintsDisabler;

public class XsdGenerationService {

	private static Log logger = LogFactory.getLog(DefaultConstraintsDisabler.class);
	private static boolean IS_GENERATED = false;

	public static void generateXsd(){
		if (!IS_GENERATED){
			logger.info("Generating XSD for database schema");
			DatabaseUnitils.generateDatasetDefinition();
			IS_GENERATED = true;
		}
	}

}
