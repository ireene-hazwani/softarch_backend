const db = require('../config/db'); 

class CourseDao {
    async getCoursesByRole(role, userId) {
        try {
            if (role === 'student') {
                const queryText = `
                    SELECT c.courseId, c.title, c.courseCode, c.description 
                    FROM courses c
                    INNER JOIN enrollment e ON c.courseId = e.courseId
                    WHERE e.studentId = ?;
                `;
                const [rows] = await db.execute(queryText, [userId]);
                return rows;
            } else if (role === 'lecturer') {
                const [rows] = await db.execute(
                    'SELECT courseId, title, courseCode, description FROM courses WHERE lecturerId = ?;',
                    [userId]
                );
                return rows;
            } else {
                const [rows] = await db.execute('SELECT courseId, title, courseCode, description FROM courses;');
                return rows;
            }
        } catch (err) {
            throw new Error(`Database error in getCoursesByRole: ${err.message}`);
        }
    }

    async createCourse(title, courseCode, description, lecturerId) {
        try {
            const queryText = 'INSERT INTO courses (title, courseCode, description, lecturerId) VALUES (?, ?, ?, ?);';
            await db.execute(queryText, [title, courseCode, description, lecturerId !== 0 ? lecturerId : null]);
        } catch (err) {
            throw new Error(`Database error in createCourse: ${err.message}`);
        }
    }

    async updateCourse(courseId, title, courseCode, description) {
        try {
            const queryText = 'UPDATE courses SET title = ?, courseCode = ?, description = ? WHERE courseId = ?;';
            await db.execute(queryText, [title, courseCode, description, courseId]);
        } catch (err) {
            throw new Error(`Database error in updateCourse: ${err.message}`);
        }
    }

    async deleteCourse(courseId) {
        try {
            await db.execute('DELETE FROM courses WHERE courseId = ?;', [courseId]);
        } catch (err) {
            throw new Error(`Database error in deleteCourse: ${err.message}`);
        }
    }

    async getCourseByCode(courseCode) {
        try {
            const [rows] = await db.execute('SELECT courseId, courseCode FROM courses WHERE courseCode = ?;', [courseCode]);
            return rows[0];
        } catch (err) {
            throw new Error(`Database error in getCourseByCode: ${err.message}`);
        }
    }

    async checkEnrollment(studentId, courseId) {
        try {
            const [rows] = await db.execute(
                'SELECT * FROM enrollment WHERE studentId = ? AND courseId = ?;', 
                [studentId, courseId]
            );
            return rows.length > 0;
        } catch (err) {
            throw new Error(`Database error in checkEnrollment: ${err.message}`);
        }
    }

    async enrollStudent(studentId, courseId) {
        try {
            await db.execute(
                'INSERT INTO enrollment (studentId, courseId) VALUES (?, ?);', 
                [studentId, courseId]
            );
        } catch (err) {
            throw new Error(`Database error in enrollStudent: ${err.message}`);
        }
    }
}

module.exports = new CourseDao();