const db = require('../config/db'); // Points to your backend/config/db.js file

/**
 * Finds a user by their unique email address.
 */
exports.getUserByEmail = async (email) => {
    try {
        // Matches your exact phpMyAdmin columns
        const [rows] = await db.execute('SELECT * FROM users WHERE email = ?', [email]);
        return rows[0]; 
    } catch (err) {
        throw new Error(`Database error in getUserByEmail: ${err.message}`);
    }
};

/**
 * Finds a student user by matching both their email and matric number.
 */
exports.getStudentByMatric = async (email, matricNo) => {
    try {
        // 👇 FIXED: Changed 'matric_no' to match your table column header 'matricno'
        const [rows] = await db.execute(
            'SELECT * FROM users WHERE email = ? AND matricno = ?', 
            [email, matricNo]
        );
        return rows[0];
    } catch (err) {
        throw new Error(`Database error in getStudentByMatric: ${err.message}`);
    }
};

/**
 * Creates a brand new user row record inside your phpMyAdmin users table.
 */
exports.createUser = async (name, email, password, role, matricNo) => {
    try {
        // 👇 FIXED: Updated target columns to match 'userId' auto-increment schema and 'matricno'
        const [result] = await db.execute(
            'INSERT INTO users (name, email, password, role, matricno) VALUES (?, ?, ?, ?, ?)',
            [name, email, password, role, matricNo]
        );
        return result;
    } catch (err) {
        throw new Error(`Database error in createUser: ${err.message}`);
    }
};