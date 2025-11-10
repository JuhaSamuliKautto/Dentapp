import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import StudentFrontPage from "./pages/StudentFrontPage";
import TeacherFrontPage from "./pages/TeacherFrontPage";
import StudentCoursesView from "./pages/StudentCoursesView";
import TeacherClasses from "./pages/TeacherClasses";
import StudentNavbar from "./components/StudentNavbar";
import TeacherNavbar from "./components/TeacherNavbar";

// Protected route component
function ProtectedRoute({ children, role }) {
  const currentRole = localStorage.getItem("role");
  if (currentRole !== role) return <Navigate to="/" replace />;
  return children;
}

export default function App() {
  const [role, setRole] = useState(localStorage.getItem("role"));

  return (
    <Router>
      {/* Show the navbar if logged in */}
      {role === "student" && <StudentNavbar onLogout={() => setRole(null)} />}
      {role === "teacher" && <TeacherNavbar onLogout={() => setRole(null)} />}

      <div style={{ paddingTop: "70px" }}> {/* prevent content overlapping navbar */}
        <Routes>
          <Route path="/" element={<Login onLogin={setRole} />} />
          
          <Route
            path="/student"
            element={
              <ProtectedRoute role="student">
                <StudentFrontPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/student/courses"
            element={
              <ProtectedRoute role="student">
                <StudentCoursesView />
              </ProtectedRoute>
            }
          />
          <Route
            path="/teacher"
            element={
              <ProtectedRoute role="teacher">
                <TeacherFrontPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/teacher/classes"
            element={
              <ProtectedRoute role="teacher">
                <TeacherClasses />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}





