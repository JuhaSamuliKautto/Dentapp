import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginSelection from "./pages/LoginSelection";
import Login from "./pages/Login";
import StudentFrontPage from "./pages/StudentFrontPage";
import TeacherFrontPage from "./pages/TeacherFrontPage";
import StudentCoursesView from "./pages/StudentCoursesView";
import StudentCourseSelect from "./pages/StudentCourseSelect";
import TeacherClasses from "./pages/TeacherClasses";
import StudentNavbar from "./components/StudentNavbar";
import TeacherNavbar from "./components/TeacherNavbar";
import CreateCard from "./pages/CreateCard";
import CreateCourse_1 from "./pages/CreateCourse_1";
import CreateCourse_2 from "./pages/CreateCourse_2";
import CreateCourse_3 from "./pages/CreateCourse_3";
import AddStudents from "./pages/AddStudents";
import CardSelection from "./pages/CardSelection.jsx";
import TeacherStudentLookUp from "./pages/TeacherStudentLookUp";
import TeacherStudentPerformance from "./pages/TeacherStudentPerformance";
import TeacherReadyCourses from "./pages/TeacherReadyCourses";
import StudentPastCourses from "./pages/StudentPastCourses";

// Protected route component
function ProtectedRoute({ children, role }) {
  const currentRole = localStorage.getItem("role");
  if (currentRole !== role) return <Navigate to="/" replace />;
  return children;
}

export default function App() {
  const [role, setRole] = useState(localStorage.getItem("role"));

  const handleLogout = () => {
    localStorage.removeItem("role");
    setRole(null);
  };

  const handleLogin = (loggedRole) => {
    setRole(loggedRole);
  };

  return (
    <Router>
      {/* Show the navbar if logged in */}
      {role === "student" && <StudentNavbar onLogout={handleLogout} />}
      {role === "teacher" && <TeacherNavbar onLogout={handleLogout} />}

      {/* Add padding so content is not hidden behind navbars */}
      <div
        style={{
          paddingTop: role ? "70px" : "0",
          paddingBottom: role ? "80px" : "0",
        }}
      >
        <Routes>
          {/* Step 1: Role selection */}
          <Route path="/" element={<LoginSelection />} />

          {/* Step 2: Role-specific login */}
          <Route path="/:role" element={<Login onLogin={handleLogin} />} />

          {/* Student protected routes */}
          <Route
            path="/student"
            element={
              <ProtectedRoute role="student">
                <StudentFrontPage />
              </ProtectedRoute>
            }
          />

          {/* Kategoriasta kurssien valinta */}
          <Route
            path="/student/course-select"
            element={
              <ProtectedRoute role="student">
                <StudentCourseSelect />
              </ProtectedRoute>
            }
          />

          {/* Yksittäisen kurssin suoritteet */}
          <Route
            path="/student/course-view"
            element={
              <ProtectedRoute role="student">
                <StudentCoursesView />
              </ProtectedRoute>
            }
          />
                    <Route
            path="/student/past-courses"
            element={
              <ProtectedRoute role="student">
                <StudentPastCourses />
              </ProtectedRoute>
            }
          />

          {/* Teacher protected routes */}
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

          <Route
            path="/teacher/add-students"
            element={
              <ProtectedRoute role="teacher">
                <AddStudents />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/card-selection"
            element={
              <ProtectedRoute role="teacher">
                <CardSelection />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/create-course-1"
            element={
              <ProtectedRoute role="teacher">
                <CreateCourse_1 />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/create-course-2"
            element={
              <ProtectedRoute role="teacher">
                <CreateCourse_2 />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/create-course-3"
            element={
              <ProtectedRoute role="teacher">
                <CreateCourse_3 />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/create-card"
            element={
              <ProtectedRoute role="teacher">
                <CreateCard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/student-lookup"
            element={
              <ProtectedRoute role="teacher">
                <TeacherStudentLookUp />
              </ProtectedRoute>
            }
          />

          <Route
            path="/teacher/student-performance"
            element={
              <ProtectedRoute role="teacher">
                <TeacherStudentPerformance />
              </ProtectedRoute>
            }
          />
          <Route
  path="/teacher/ready-courses"
  element={
    <ProtectedRoute role="teacher">
      <TeacherReadyCourses />
    </ProtectedRoute>
  }
/>

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

