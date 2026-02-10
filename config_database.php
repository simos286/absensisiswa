<?php
// Database Configuration
class Database {
    private $host = "localhost";
    private $db_name = "sma_absensi";
    private $username = "root";
    private $password = "";
    public $conn;
    
    public function getConnection() {
        $this->conn = null;
        
        try {
            $this->conn = new PDO(
                "mysql:host=" . $this->host . ";dbname=" . $this->db_name . ";charset=utf8mb4",
                $this->username, 
                $this->password
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        } catch(PDOException $exception) {
            error_log("Connection error: " . $exception->getMessage());
            die("Database connection failed. Please contact administrator.");
        }
        
        return $this->conn;
    }
    
    // Function to sanitize input
    public function sanitize($input) {
        $input = trim($input);
        $input = stripslashes($input);
        $input = htmlspecialchars($input);
        return $input;
    }

    get_required_files() {
        require_once 'config_email.php';
        require_once 'whatsapp_config.php';
    }
    get_browser_info() {
        $user_agent = $_SERVER['HTTP_USER_AGENT'];
        $browser = "Unknown Browser";
        
        if (preg_match('/MSIE/i', $user_agent) && !preg_match('/Opera/i', $user_agent)) {
            $browser = "Internet Explorer";
        } elseif (preg_match('/Firefox/i', $user_agent)) {
            $browser = "Mozilla Firefox";
        } elseif (preg_match('/Chrome/i', $user_agent)) {
            $browser = "Google Chrome";
        } elseif (preg_match('/Safari/i', $user_agent)) {
            $browser = "Apple Safari";
        } elseif (preg_match('/Opera/i', $user_agent)) {
            $browser = "Opera";
        }
        
        return $browser;
    }

    get_browser_language() {
        $accept_language = $_SERVER['HTTP_ACCEPT_LANGUAGE'];
        $languages = explode(',', $accept_language);
        return $languages[0] ?? 'en';
    }
    get_browser_timezone() {
        // This is a placeholder. In a real application, you would use JavaScript to get the user's timezone and send it to the server.
        return 'UTC';
    }
     function getDashboardUrl($userType) {
        $urls = [
            'admin' => 'dashboard_admin.html',
            'guru' => 'dashboard_guru_staf_detail.html',
            'siswa' => 'dashboard_siswa_detail.html',
            'orangtua' => 'dashboard_orangtua_detail.html'
        ];
        return $urls[$userType] ?? 'login.html';
}

// Create database instance
$database = new Database();
$db = $database->getConnection();

// School coordinates for geofencing
define('SCHOOL_LAT', -6.890123); // Latitude SMA Negeri 1 Sayung
define('SCHOOL_LNG', 110.456789); // Longitude SMA Negeri 1 Sayung
define('SCHOOL_RADIUS', 100); // Radius dalam meter
?>