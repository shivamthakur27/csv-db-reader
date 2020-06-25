import java.sql.Timestamp;

public class CSVObject
{
    private Timestamp resultTime;
    private double granularityPeriod;
    private String objectName;
    private double cellID;
    private double callAttempts;

    public CSVObject() {
    }

    public CSVObject(Timestamp resultTime, double granularityPeriod, String objectName, double cellID, double callAttempts) {
        this.resultTime = resultTime;
        this.granularityPeriod = granularityPeriod;
        this.objectName = objectName;
        this.cellID = cellID;
        this.callAttempts = callAttempts;
    }

    public Timestamp getResultTime() {
        return resultTime;
    }

    public void setResultTime(Timestamp resultTime) {
        this.resultTime = resultTime;
    }

    public double getGranularityPeriod() {
        return granularityPeriod;
    }

    public void setGranularityPeriod(double granularityPeriod) {
        this.granularityPeriod = granularityPeriod;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public double getCellID() {
        return cellID;
    }

    public void setCellID(double cellID) {
        this.cellID = cellID;
    }

    public double getCallAttempts() {
        return callAttempts;
    }

    public void setCallAttempts(double callAttempts) {
        this.callAttempts = callAttempts;
    }
}
