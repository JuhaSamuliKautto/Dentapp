// src/pages/TeacherStudentPerformance.jsx
import { useState } from "react";
import { useSearchParams } from "react-router-dom";

// Mock data for courses grouped by category
const mockStudentData = {
  name: "Maija Meikäläinen",
  studentNumber: "112233",
  categories: [
    {
      id: 1,
      name: "Taitopaja",
      courses: [
        { id: 1, name: "Kurssin nimi", code: "Kurssin tunnus" },
        { id: 2, name: "Kurssin nimi 2", code: "Kurssin tunnus 2" },
      ],
    },
    {
      id: 2,
      name: "Kariologia",
      courses: [{ id: 3, name: "Kurssin nimi", code: "Kurssin tunnus" }],
    },
  ],
};

export default function TeacherStudentPerformance() {
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get("search") || "";
  const [expandedCourses, setExpandedCourses] = useState({});

  const toggleCourse = (courseKey) => {
    setExpandedCourses((prev) => ({
      ...prev,
      [courseKey]: !prev[courseKey],
    }));
  };

  return (
    <div style={styles.container}>
      {/* Student Header */}
      <div style={styles.header}>
        <h2 style={styles.title}>Oppilas</h2>
        <h1 style={styles.studentName}>{mockStudentData.name}</h1>
        <p style={styles.studentNumber}>{mockStudentData.studentNumber}</p>
        {searchQuery && (
          <p style={styles.searchInfo}>Haettu hakusanalla: {searchQuery}</p>
        )}
      </div>

      {/* Categories List */}
      <div style={styles.categoriesList}>
        {mockStudentData.categories.map((category) => (
          <div key={category.id} style={styles.categoryContainer}>
            {/* Category Header */}
            <div style={styles.categoryHeader}>
              <span style={styles.categoryName}>{category.name}</span>
            </div>

            {/* Courses in Category */}
            {category.courses.map((course) => {
              const key = `${category.id}-${course.id}`;
              const open = !!expandedCourses[key];

              return (
                <div
                  key={key}
                  style={styles.courseBlock}
                  onClick={() => toggleCourse(key)}
                >
                  <div style={styles.courseItem}>
                    <span style={styles.chevron}>{open ? "▲" : "▼"}</span>
                    <div style={styles.courseInfo}>
                      <span style={styles.courseName}>[{course.name}]</span>
                      <span style={styles.courseCode}>[{course.code}]</span>
                    </div>
                  </div>

                  {open && (
                    <div style={styles.courseDetails}>
                      <p style={styles.detailText}>
                        Tänne myöhemmin kurssin suoritteet yms. dataa.
                      </p>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        ))}
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    minHeight: "calc(100vh - 150px)",
    backgroundColor: "#fff",
    padding: "1.5rem",
  },
  header: {
    textAlign: "center",
    marginBottom: "2rem",
  },
  title: {
    fontSize: "1rem",
    fontWeight: 400,
    margin: 0,
    color: "#555",
  },
  studentName: {
    fontSize: "1.5rem",
    fontWeight: 600,
    margin: "0.25rem 0",
    color: "#1A1A1A",
  },
  studentNumber: {
    fontSize: "0.9rem",
    color: "#555",
    margin: 0,
  },
  searchInfo: {
    marginTop: "0.5rem",
    fontSize: "0.85rem",
    color: "#777",
  },
  categoriesList: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  categoryContainer: {
    border: "1px solid #e0e0e0",
    borderRadius: 6,
    backgroundColor: "#fff",
  },
  categoryHeader: {
    backgroundColor: "#f5f5f5",
    padding: "0.75rem 1rem",
    borderBottom: "1px solid #e0e0e0",
  },
  categoryName: {
    fontSize: "1rem",
    fontWeight: 600,
    color: "#1A1A1A",
  },
  courseBlock: {
    borderTop: "1px solid #e0e0e0",
    cursor: "pointer",
  },
  courseItem: {
    display: "flex",
    alignItems: "center",
    padding: "0.75rem 1rem",
    gap: "0.5rem",
  },
  chevron: {
    fontSize: "0.9rem",
    width: "1.5rem",
    textAlign: "center",
    color: "#1A1A1A",
  },
  courseInfo: {
    display: "flex",
    flexDirection: "column",
  },
  courseName: {
    fontSize: "0.95rem",
    color: "#1A1A1A",
  },
  courseCode: {
    fontSize: "0.85rem",
    color: "#555",
  },
  courseDetails: {
    padding: "0 1rem 0.75rem 2.5rem",
  },
  detailText: {
    margin: 0,
    fontSize: "0.85rem",
    color: "#555",
  },
};