// This middleware can be expanded later to verify JWT tokens or session cookies
exports.verifyAccess = (req, res, next) => {
    console.log(`[SECURITY LOG]: Incoming request to ${req.path}`);
    
    // For now, it cleanly passes the request forward to the controllers
    next();
};