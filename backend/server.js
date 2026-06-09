const express = require('express');
const cors = require('cors');
const authController = require('./controllers/authController');
const courseController = require('./controllers/courseController'); 

const app = express();

// Middleware Configuration
app.use(cors());
app.use(express.json());

// 🔐 Authentication System Endpoints
app.post('/api/register', authController.register);
app.post('/api/login', authController.login);

// 📚 Course System Endpoints Middleware Mounting
app.use('/api/courses', courseController);

// Server Boot Listener
const PORT = 5001;
app.listen(PORT, () => {
    console.log(`\n🚀 ===================================================`);
    console.log(`   SmartLearn Backend Server Stack Active on Port: ${PORT}`);
    console.log(`====================================================== 🚀\n`);
});