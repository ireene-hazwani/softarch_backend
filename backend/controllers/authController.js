const bcrypt = require('bcryptjs');
const userDao = require('../dao/userDao');

exports.register = async (req, res) => {
    try {
        const { role, name, email, password, matricNo } = req.body;

        if (!role || !name || !email || !password) {
            return res.status(400).json({ error: "Missing mandatory registration fields." });
        }
        if (role === 'student' && !matricNo) {
            return res.status(400).json({ error: "Matric number is required for student registration." });
        }

        const userExists = await userDao.getUserByEmail(email);
        if (userExists) {
            return res.status(400).json({ error: "An account with this email already exists." });
        }

        const hashedPassword = await bcrypt.hash(password, 10);
        await userDao.createUser(name, email, hashedPassword, role, role === 'student' ? matricNo : null);

        return res.status(201).json({ message: "User registered successfully!" });
    } catch (err) {
        return res.status(500).json({ error: `Server registration error: ${err.message}` });
    }
};

exports.login = async (req, res) => {
    try {
        const { role, email, password, matricNo } = req.body;

        if (!role || !email || !password) {
            return res.status(400).json({ error: "Missing input data fields." });
        }

        let user;
        if (role === 'student') {
            if (!matricNo) return res.status(400).json({ error: "Matric number required for student login." });
            user = await userDao.getStudentByMatric(email, matricNo);
        } else {
            user = await userDao.getUserByEmail(email);
        }

        if (user && await bcrypt.compare(password, user.password)) {
            // 👇 FIXED: Maps 'user.userId' (matching your database) into both frontend variable variations
            return res.status(200).json({
                userId: user.userId,
                user_id: user.userId,
                name: user.name,
                user_name: user.name,
                role: user.role,
                user_role: user.role
            });
        }
        return res.status(401).json({ error: "Invalid login credentials." });
    } catch (err) {
        return res.status(500).json({ error: `Server authentication error: ${err.message}` });
    }
};