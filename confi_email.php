<?php
// Email Configuration for SMTP
class EmailConfig {
    // SMTP Configuration
    public static $smtp_host = 'smtp.gmail.com';
    public static $smtp_port = 587;
    public static $smtp_username = 'noreply@sman1sayung.sch.id';
    public static $smtp_password = 'your-email-password';
    public static $smtp_encryption = 'tls';
    
    // Email Templates
    public static function getVerificationEmail($name, $token) {
        $verification_link = "http://localhost/sma_absensi_v2/verify-email.php?token=" . $token;
        
        return "
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #1a237e; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background: #f9f9f9; }
                    .button { display: inline-block; padding: 10px 20px; background: #1a237e; color: white; text-decoration: none; border-radius: 5px; }
                    .footer { margin-top: 20px; text-align: center; color: #666; }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h2>SMA Negeri 1 Sayung Demak</h2>
                        <h3>Verifikasi Email</h3>
                    </div>
                    <div class='content'>
                        <p>Halo <strong>$name</strong>,</p>
                        <p>Terima kasih telah mendaftar di Sistem Absensi Digital SMA Negeri 1 Sayung Demak.</p>
                        <p>Silakan klik tombol di bawah untuk verifikasi email Anda:</p>
                        <p style='text-align: center;'>
                            <a href='$verification_link' class='button'>Verifikasi Email</a>
                        </p>
                        <p>Atau copy link berikut di browser Anda:</p>
                        <p><code>$verification_link</code></p>
                        <p>Link ini akan kadaluarsa dalam 24 jam.</p>
                        <p>Jika Anda tidak mendaftar, abaikan email ini.</p>
                    </div>
                    <div class='footer'>
                        <p>&copy; " . date('Y') . " SMA Negeri 1 Sayung Demak. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
        ";
    }
    
    public static function getPasswordResetEmail($name, $token) {
        $reset_link = "http://localhost/sma_absensi_v2/reset-password.php?token=" . $token;
        
        return "
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #dc3545; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background: #f9f9f9; }
                    .button { display: inline-block; padding: 10px 20px; background: #dc3545; color: white; text-decoration: none; border-radius: 5px; }
                    .footer { margin-top: 20px; text-align: center; color: #666; }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h2>Reset Password</h2>
                        <h3>Sistem Absensi Digital</h3>
                    </div>
                    <div class='content'>
                        <p>Halo <strong>$name</strong>,</p>
                        <p>Kami menerima permintaan reset password untuk akun Anda.</p>
                        <p>Silakan klik tombol di bawah untuk reset password:</p>
                        <p style='text-align: center;'>
                            <a href='$reset_link' class='button'>Reset Password</a>
                        </p>
                        <p>Atau copy link berikut di browser Anda:</p>
                        <p><code>$reset_link</code></p>
                        <p>Link ini akan kadaluarsa dalam 1 jam.</p>
                        <p>Jika Anda tidak meminta reset password, abaikan email ini.</p>
                    </div>
                    <div class='footer'>
                        <p>&copy; " . date('Y') . " SMA Negeri 1 Sayung Demak.</p>
                    </div>
                </div>
            </body>
            </html>
        ";
    }
}

// PHPMailer Implementation
require_once 'vendor/phpmailer/phpmailer/src/PHPMailer.php';
require_once 'vendor/phpmailer/phpmailer/src/SMTP.php';
require_once 'vendor/phpmailer/phpmailer/src/Exception.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

class EmailService {
    public static function sendEmail($to, $subject, $body, $alt_body = '') {
        $mail = new PHPMailer(true);
        
        try {
            // Server settings
            $mail->isSMTP();
            $mail->Host = EmailConfig::$smtp_host;
            $mail->SMTPAuth = true;
            $mail->Username = EmailConfig::$smtp_username;
            $mail->Password = EmailConfig::$smtp_password;
            $mail->SMTPSecure = EmailConfig::$smtp_encryption;
            $mail->Port = EmailConfig::$smtp_port;
            
            // Recipients
            $mail->setFrom(EmailConfig::$smtp_username, 'SMA Negeri 1 Sayung Demak');
            $mail->addAddress($to);
            
            // Content
            $mail->isHTML(true);
            $mail->Subject = $subject;
            $mail->Body = $body;
            $mail->AltBody = $alt_body ?: strip_tags($body);
            
            $mail->send();
            return true;
        } catch (Exception $e) {
            error_log("Email could not be sent. Mailer Error: {$mail->ErrorInfo}");
            return false;
        }
    }
}
?>