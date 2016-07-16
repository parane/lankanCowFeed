package cow.atuts.com.cowfeed.dto;

import java.io.Serializable;

/**
 * Created by Parani on 16/07/2016.
 */
public class Result implements Serializable {
    private String bulkForage;
    private String supplementaryForage;
    private String Concentrate;

    public String getBulkForage() {
        return bulkForage;
    }

    public void setBulkForage(String bulkForage) {
        this.bulkForage = bulkForage;
    }

    public String getSupplementaryForage() {
        return supplementaryForage;
    }

    public void setSupplementaryForage(String supplementaryForage) {
        this.supplementaryForage = supplementaryForage;
    }

    public String getConcentrate() {
        return Concentrate;
    }

    public void setConcentrate(String concentrate) {
        Concentrate = concentrate;
    }
}
