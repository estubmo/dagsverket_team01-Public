
package dagsverket.system;

import java.util.ArrayList;



public class User{
    
    private String name;
    private String password;
    
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public void setPassword(String password){
        this.password = password;
    }
}

class UserList{
    
    private ArrayList<User> users;
    
    
    public UserList(){
        users = new ArrayList<User>();
    }
    
    public boolean regNewUser(String name, String password){
        User temp = new User(name, password);
        if(users.get(0) == null){
            users.add(temp);
            return true;
        }
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getName().equals(name)){
                return false;
            }
        }
        users.add(temp);
        return true;
    }
    
    public boolean changePassword(String name, String oldPass, String newPass){
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getName().equals(name) && users.get(i).getPassword().equals(oldPass)){
                users.get(i).setPassword(newPass);
                return true;
            }
        }
        return false;
    }
}


