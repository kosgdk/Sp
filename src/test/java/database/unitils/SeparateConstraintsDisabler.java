package database.unitils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.core.dbsupport.DbSupport;
import org.unitils.dbmaintainer.structure.impl.DefaultConstraintsDisabler;
import org.unitils.dbmaintainer.util.BaseDatabaseAccessor;

import java.util.Iterator;


public class SeparateConstraintsDisabler extends BaseDatabaseAccessor implements org.unitils.dbmaintainer.structure.ConstraintsDisabler {
    private static Log logger = LogFactory.getLog(DefaultConstraintsDisabler.class);


    public SeparateConstraintsDisabler() {}

    public void disableConstraints() {

        Iterator i$ = this.dbSupports.iterator();

        while(i$.hasNext()) {
            DbSupport dbSupport = (DbSupport) i$.next();
            logger.info("Disabling constraints in database schema " + dbSupport.getSchemaName());
            switch (ConstraintsDisabler.getConstraintType()){
                case VALUE:
                    this.disableValueConstraints(dbSupport);
                    break;
                case REFERENTIAL:
                    this.disableReferentialConstraints(dbSupport);
                    break;
                default:
                    this.disableValueConstraints(dbSupport);
                    this.disableReferentialConstraints(dbSupport);
            }
        }
    }

    protected void disableReferentialConstraints(DbSupport dbSupport) {
        try {
            logger.info("Disabling referential constraints");
            dbSupport.disableReferentialConstraints();
        } catch (Throwable var3) {
            logger.error("Unable to remove referential constraints.", var3);
        }

    }

    protected void disableValueConstraints(DbSupport dbSupport) {
        try {
            logger.info("Disabling value constraints");
            dbSupport.disableValueConstraints();
        } catch (Throwable var3) {
            logger.error("Unable to remove value constraints.", var3);
        }

    }
}
