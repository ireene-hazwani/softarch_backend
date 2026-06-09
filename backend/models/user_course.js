// This defines the structure of your data models for the application
const UserSchema = {
    user_id: Number,
    name: String,
    email: String,
    role: 'student' | 'lecturer' | 'admin',
    matric_no: String || null
};

const CourseSchema = {
    course_id: Number,
    title: String,
    course_code: String,
    description: String,
    lecturer_id: Number || null
};

module.exports = { UserSchema, CourseSchema };