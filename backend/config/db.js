const mysql = require('mysql2/promise');

// Creates a connection pool matching your phpMyAdmin local server
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',      // Default XAMPP/phpMyAdmin username
    password: '',      // Default XAMPP/phpMyAdmin password is empty
    database: 'softwarearchitecture',
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

module.exports = pool;