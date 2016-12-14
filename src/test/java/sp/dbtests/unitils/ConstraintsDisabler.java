package sp.dbtests.unitils;

import org.unitils.database.DatabaseUnitils;


public class ConstraintsDisabler {

    public enum ConstraintType{
        VALUE, REFERENTIAL, ALL
    }

    private static ConstraintType constraintType = ConstraintType.ALL;

    public static ConstraintType getConstraintType() {
        return constraintType;
    }

    public static void setConstraintType(ConstraintType constraintType) {
        ConstraintsDisabler.constraintType = constraintType;
    }

    public static void disableValueConstraints(){
        constraintType = ConstraintType.VALUE;
        DatabaseUnitils.disableConstraints();
        constraintType = ConstraintType.ALL;
    }

    public static void disableReferentialConstraints(){
        constraintType = ConstraintType.REFERENTIAL;
        DatabaseUnitils.disableConstraints();
        constraintType = ConstraintType.ALL;
    }

    public static void disableAllConstraints(){
        DatabaseUnitils.disableConstraints();
    }

}
