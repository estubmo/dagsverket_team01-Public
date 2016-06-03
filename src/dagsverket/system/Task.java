
package dagsverket.system;


import dagsverket.gui.mainWindow.TaskListPanel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task {
    
    private Employer employer;
    private Supervisor supervisor;
    private String address;
    private String post;
    private String zipcode;
    private boolean isOutside;
    private String description;
    private String befaring;
    private String taskName;
    private double cost;
    GregorianCalendar startDate = new GregorianCalendar();
    GregorianCalendar regDate = new GregorianCalendar();
    private boolean completed;
    private boolean paid;
    private int taskNumber;
    private String username;
    private ArrayList<Worker> workers;
    private ArrayList<Equipment> equipment;

    public Task(int taskNumber, String taskName, Employer employer, String address, String post, String zipcode, boolean isOutside, String description, String befaring, double cost, String startDate, String regDate, boolean completed) {
        setTaskNumber(taskNumber);
        setTaskName(taskName);
        setEmployer(employer);
        setAddress(address);
        setPost(post);
        setZipcode(zipcode);
        setIsOutside(isOutside);
        setDescription(description);
        setBefaring(befaring);
        setCost(cost);
        setStartDate(startDate);
        setRegDate(regDate);
        setCompleted(completed);
        workers = new ArrayList<Worker>();
        equipment = new ArrayList<Equipment>();
    }

    public Task(){
        address = "";
        post = "";
        zipcode = "";
        description = "";
        befaring = "";
        taskName = "";

    }
    //<editor-fold> Getters
    public Employer getEmployer() {
        return employer;
    }

    public String getAddress() {
        return address;
    }

    public String getPost() {
        return post;
    }

    public String getZipcode() {
        return zipcode;
    }
    
    public ArrayList<Worker> getWorkerList(){
        return workers;
    }

    public boolean isIsOutside() {
        return isOutside;
    }

    public String getDescription() {
        return description;
    }
    
    public String getBefaring() {
        return befaring;
    }

    public double getCost() {
        return cost;
    }

    public GregorianCalendar getStartDate(){
        return startDate;
    }
    
    public GregorianCalendar getRegDate(){
        return regDate;
    }

    public boolean getCompleted(){
        return completed;
    }

    public String getTaskName(){
        return taskName;
    }

    public int getTaskNumber(){
        return taskNumber;
    }

    public String getUsername(){
        return username;
    }

    public ArrayList<Equipment> getEquipment(){
        return equipment;
    }

    public Supervisor getSupervisor(){
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor){
        this.supervisor = supervisor;
    }

    public String getRegDateString(){
        return regDate.get(Calendar.DATE) + ". " + TaskListPanel.getMonthName(regDate);
    }
    
    
   public boolean isPaid(){
        return paid;
    }

    //</editor-fold> Getters
    
    public void setWorker(ArrayList<Worker> workers){
        this.workers = workers;
    }

    public void setPaid(boolean b){
        paid = b;
    }

    public void setEmployer(Employer employer) {

            this.employer = employer;

    }
    public void setAddress(String address) {
           if (address == null){
               this.address = "";
        } else {
            this.address = address;
        }
    }
    public void setPost(String poststed) {
           if (poststed == null){
               this.post = "";
        } else {
            this.post = poststed;
        }
    }
    public void setZipcode(String postnummer) {
           if (postnummer.matches("[0-9]+") && postnummer.length() == 4){
               this.zipcode = postnummer;
        } else {

        }
    }

    public void setIsOutside(boolean isOutside) {
        this.isOutside = isOutside;
    }

    public void setDescription(String description) {
        if (description == null){
            this.description = "";
        } else {
            this.description = description;
        }
    }
    
    public void setBefaring(String befaring) {
        if (befaring == null){
            this.befaring = "";
        } else {
            this.befaring = befaring;
        }
    }

    public void setCost(double cost) {
        if (cost >= 0){
            this.cost = cost;
        } else {
            cost = 0;
        }
    }

    public void setTaskName(String taskName){
        if (taskName == null){
            this.taskName = "";
        } else {
            this.taskName = taskName;
        }
    }

    public void setTaskNumber(int t){
        taskNumber = t;
    }

    public void setStartDate(String d){
        
        if (d.matches("[0-9]+") && d.length() != 8){
            throw new IllegalArgumentException("Dato må være på formatet yyyyMMdd");
        } else {
            int year = Integer.parseInt(d.substring(0,4));
            int month = Integer.parseInt(d.substring(4,6));
            int day = Integer.parseInt(d.substring(6,8));
            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, month-1);
            startDate.set(Calendar.DAY_OF_MONTH, day);
        }
        
    }
    
    
    public void setRegDate(String d){
        
        if (d.matches("[0-9]+") && d.length() != 8){
            throw new IllegalArgumentException("Dato må være på formatet yyyyMMdd");
        } else {
            int year = Integer.parseInt(d.substring(0,4));
            int month = Integer.parseInt(d.substring(4,6));
            int day = Integer.parseInt(d.substring(6,8));
            regDate.set(Calendar.YEAR, year);
            regDate.set(Calendar.MONTH, month-1);
            regDate.set(Calendar.DAY_OF_MONTH, day);
        }
        
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setUsername(String username){
        if (username != null && !username.trim().equals("")){
            this.username = username;
        }
    }

    public static String getDato(Task task) {

        GregorianCalendar start = task.getStartDate();
        return String.format("%02d",start.get(Calendar.DATE)) + ". " + TaskListPanel.getMonthName(start);
    }


    public void setEquipment(ArrayList<Equipment> equipment) {
        this.equipment = equipment;
    }

    public void removeWorkers(int[] selectedRows) {
        for (int i = 0; i < selectedRows.length; i++){
            workers.remove(selectedRows[i]);
            
        }
    }
}


