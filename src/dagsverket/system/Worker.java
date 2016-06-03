/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dagsverket.system;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Eirik
 */
public class Worker extends Person {

    private boolean canWorkOutside;
    private int jobsLast30Days;
    private int jobsNext30Days;
    private Date lastDateWorked;

    public Worker(int persnr, String firstname, String surname, boolean canWorkOutside) {
        super(persnr, firstname, surname);
        this.canWorkOutside = canWorkOutside;
    }

    public boolean getCanWorkOutside() {
        return canWorkOutside;
    }

    public void setCanWorkOutside(boolean canWorkOutside) {
        this.canWorkOutside = canWorkOutside;
    }

    public String getLastDateWorked() {
        if (lastDateWorked != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd. MMMM yyyy");
            String dateToString = sdf.format(lastDateWorked);
            return dateToString;
        }
        return "";

    }

    public int getJobsLast30Days() {
        return jobsLast30Days;
    }

    public int getJobsNext30Days() {
        return jobsNext30Days;
    }

    public void setLastDateWorked(Date lastDateWorked) {
        this.lastDateWorked = lastDateWorked;
    }

    public void setJobsLast30Days(int jobsLast30Days) {
        this.jobsLast30Days = jobsLast30Days;
    }

    public void setJobsNext30Days(int jobsNext30Days) {
        this.jobsNext30Days = jobsNext30Days;
    }

}
