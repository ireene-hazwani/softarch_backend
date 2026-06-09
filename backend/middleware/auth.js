exports.verifyAccess = (req, res, next) => {
    console.log(`[SECURITY LOG]: Incoming request to ${req.path}`);
    next();
};