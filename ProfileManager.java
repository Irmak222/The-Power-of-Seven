import java.util.ArrayList;

public class ProfileManager {
    // Registered users
    private final ArrayList<Player> registeredUsers;

    //Constructor
    public ProfileManager() {
        this.registeredUsers = new ArrayList<>();
    }

    /// True if the registration is successful, false if the username is already taken
    public boolean register(String userName) {
        for(Player player : registeredUsers){
            if(player.getName().equals(userName)){
                return false;
            }
        }
        Player player = new Player(userName, false);
        registeredUsers.add(player);
        return true;
    }

    // True if the profile exists, false otherwise
    public boolean login(String userName) {
        for(Player player : registeredUsers){
            if(player.getName().equals(userName)){
                return true;
            }
        }
        return false;
    }
    
    // Updates the profile owner's score
    public void saveStats(String userName, int score) {

    }

    // Checks the registry to verify whether there is an account or not
    private boolean isRegistered(String userName) {
        return login(userName);
    }

    // Getters
    public ArrayList<Player> getRegisteredUsers() {
        return registeredUsers;
    }

    
}
