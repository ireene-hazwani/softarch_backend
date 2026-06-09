const express = require('express');
const router = express.Router();
const courseDao = require('../dao/courseDao');

// GET: Fetch courses matching specific workspace logic roles
router.get('/', async (req, res) => {
    const { role, userId } = req.query;

    try {
        const matchingCourses = await courseDao.getCoursesByRole(role, userId);
        return res.json(matchingCourses);
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// POST: Admin creates a course module
router.post('/', async (req, res) => {
    const { title, courseCode, description, lecturerId, role } = req.body;

    if (role !== 'admin') {
        return res.status(403).json({ error: "Access Denied: Only Admins can create courses." });
    }

    try {
        await courseDao.createCourse(title, courseCode, description, lecturerId);
        return res.status(201).json({ success: true, message: "Course successfully added by Admin!" });
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// PUT: Admin edits an existing catalog subject
router.put('/:id', async (req, res) => {
    const { id } = req.params;
    const { title, courseCode, description, role } = req.body;

    if (role !== 'admin') {
        return res.status(403).json({ error: "Access Denied: Only Admins can modify courses." });
    }

    try {
        await courseDao.updateCourse(id, title, courseCode, description);
        return res.json({ success: true, message: "Course updated successfully!" });
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// DELETE: Admin purges a course entry record
router.delete('/:id', async (req, res) => {
    const { id } = req.params;
    const { role } = req.body;

    if (role !== 'admin') {
        return res.status(403).json({ error: "Access Denied: Only Admins can remove courses." });
    }

    try {
        await courseDao.deleteCourse(id);
        return res.json({ success: true, message: "Course deleted successfully!" });
    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// POST: Student logs enrollment request using text sequence code string
router.post('/enroll', async (req, res) => {
    const { courseCode, studentId } = req.body;

    try {
        // Step 1: Look up the alphanumeric key sequence 
        const course = await courseDao.getCourseByCode(courseCode);
        if (!course) {
            return res.status(404).json({ error: "Course code not found in academic systems." });
        }

        // Step 2: Safe extract of real numerical primary keys to avoid parameter binding issues
        const realCourseId = course.courseId; 

        // Step 3: Prevent duplicate map creation rows inside your ledger data structure
        const alreadyEnrolled = await courseDao.checkEnrollment(studentId, realCourseId);
        if (alreadyEnrolled) {
            return res.status(400).json({ error: "You are already enrolled in this module." });
        }

        // Step 4: Add student mapping pair row item
        await courseDao.enrollStudent(studentId, realCourseId);
        return res.json({ success: true, message: "Enrolled successfully!" });

    } catch (err) {
        return res.status(500).json({ error: err.message });
    }
});

// ⚡ CRITICAL EXPORT: Keeps server.js running smoothly without Undefined errors
module.exports = router;