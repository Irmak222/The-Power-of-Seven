import java.util.ArrayList;

public class ProfileManager {
    // Registered users
    private final ArrayList<Player> registeredUsers;

    //Constructor
    public ProfileManager() {
        this.registeredUsers = new ArrayList<>();
    }

    /// True if the registration is successful, false if the username is already taken
    public boolean register(String username, String password) throws SQLException {

        try (Connection connection = DatabaseManager.connect()) {
    
            String checkSql = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, username);
    
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                return false; 
            }
    
            String insertSql = "INSERT INTO users(username, password, totalScore) VALUES(?,?,0)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
    
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
    
            insertStmt.executeUpdate();
    
            return true;
        }
    }

    // True if the profile exists, false otherwise
    public boolean login(String username, String password) throws SQLException {

        try (Connection connection = DatabaseManager.connect()) {
    
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
    
            PreparedStatement stmt = connection.prepareStatement(sql);
    
            stmt.setString(1, username);
            stmt.setString(2, password);
    
            ResultSet rs = stmt.executeQuery();
    
            return rs.next();
        }
    }
    
    // Updates the profile owner's score
    public void saveStats(String username, int score) throws SQLException {

        try (Connection connection = DatabaseManager.connect()) {
    
            String sql = "UPDATE users SET totalScore=? WHERE username=?";
    
            PreparedStatement stmt = connection.prepareStatement(sql);
    
            stmt.setInt(1, score);
            stmt.setString(2, username);
    
            stmt.executeUpdate();
        }
    }

    
