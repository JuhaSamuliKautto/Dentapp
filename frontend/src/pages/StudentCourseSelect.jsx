// src/pages/StudentCourseSelect.jsx
import { useNavigate, useSearchParams } from "react-router-dom";

const mockCategoryData = {
  name: "Taitopaja",
  activeCourse: {
    id: 1,
    name: "[Kurssin nimi]",
    code: "[Kurssin tunnus]",
    completionPercent: 50,
    teacherApproved: 25,
    totalPerformances: 4,
  },
  upcomingCourses: [
    { id: 2, name: "[Kurssin nimi]", code: "[Kurssin tunnus]" },
    { id: 3, name: "[Kurssin nimi]", code: "[Kurssin tunnus]" },
  ],
};

export default function StudentCourseSelect() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  return (
    <>
      <div style={styles.page}>
        <h1 style={styles.title}>{mockCategoryData.name}</h1>

        {/* Aktiivinen kurssi */}
        <div style={styles.section}>
          <h2 style={styles.sectionTitle}>Aktiivinen kurssi</h2>

          <div
            style={styles.activeCourseCard}
            onClick={() =>
              navigate(
                `/student/course-view?course=${mockCategoryData.activeCourse.id}`
              )
            }
          >
            <div style={styles.courseHeader}>
              <span style={styles.courseName}>
                {mockCategoryData.activeCourse.name}
              </span>
              <span style={styles.courseCode}>
                {mockCategoryData.activeCourse.code}
              </span>
            </div>

            <div style={styles.courseStats}>
              <div style={styles.statRow}>
                <span style={styles.statLabel}>Kurssista suoritettu</span>
                <span style={styles.statValue}>
                  {mockCategoryData.activeCourse.completionPercent}%
                </span>
              </div>

              <div style={styles.statRow}>
                <span style={styles.statLabel}>Opettaja hyväksynyt</span>
                <span style={styles.statValue}>
                  {mockCategoryData.activeCourse.teacherApproved}%
                </span>
              </div>

              <div style={styles.statRow}>
                <span style={styles.statLabel}>Suoritteet yhteensä</span>
                <span style={styles.statValue}>
                  {mockCategoryData.activeCourse.totalPerformances}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Seuraavat kurssit */}
        <div style={styles.section}>
          <h2 style={styles.sectionTitle}>Seuraavat kurssit</h2>

          {mockCategoryData.upcomingCourses.map((course) => (
            <div
              key={course.id}
              style={styles.upcomingCourseCard}
              onClick={() =>
                navigate(`/student/course-view?course=${course.id}`)
              }
            >
              <span style={styles.courseName}>{course.name}</span>
              <span style={styles.courseCode}>{course.code}</span>
            </div>
          ))}
        </div>
      </div>

      {/* UUSI Footer: vasen = secondary, oikea = primary */}
      <div style={styles.footerBar}>
        <div style={styles.footerInner}>

          <ds-button
            ds-variant="secondary"
                        onClick={() => navigate("/student")}
          >
            Takaisin

          </ds-button>

          <ds-button
            ds-variant="primary"
              onClick={() =>  navigate("/student/past-courses")
          
          }
          >
            Menneet kurssit
          </ds-button>

        </div>
      </div>
    </>
  );
}

const styles = {
  page: {
    maxWidth: 960,
    width: "100%",
    margin: "0 auto",
    padding: "16px 16px 120px",
    boxSizing: "border-box",
    backgroundColor: "#fff",
    minHeight: "100vh",
  },

  title: {
    fontSize: "1.25rem",
    fontWeight: 600,
    color: "#1A1A1A",
    marginBottom: "1.5rem",
    textAlign: "center",
  },

  section: {
    width: "100%",
    maxWidth: 400,
    margin: "0 auto 1.5rem",
  },

  sectionTitle: {
    fontSize: "0.9rem",
    fontWeight: 400,
    color: "#1A1A1A",
    marginBottom: "0.5rem",
    textAlign: "center",
  },

  activeCourseCard: {
    border: "1px solid #1A1A1A",
    borderRadius: 8,
    backgroundColor: "#fff",
    cursor: "pointer",
  },

  courseHeader: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "0.75rem",
    borderBottom: "1px solid #e0e0e0",
  },

  courseName: {
    fontSize: "0.9rem",
    color: "#1A1A1A",
  },

  courseCode: {
    fontSize: "0.9rem",
    color: "#1A1A1A",
  },

  courseStats: {
    padding: "0.75rem 1rem",
  },

  statRow: {
    display: "flex",
    justifyContent: "space-between",
    padding: "0.25rem 0",
  },

  statLabel: {
    fontSize: "0.85rem",
    color: "#1A1A1A",
  },

  statValue: {
    fontSize: "0.85rem",
    fontWeight: 500,
    color: "#1A1A1A",
  },

  upcomingCourseCard: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "0.75rem",
    border: "1px solid #e0e0e0",
    borderRadius: 8,
    backgroundColor: "#fff",
    marginBottom: "0.5rem",
    cursor: "pointer",
  },

  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56,
    padding: "8px 16px",
    backgroundColor: "#fafafa",
    boxShadow: "0 -2px 4px rgba(0,0,0,0.06)",
  },

  footerInner: {
    maxWidth: 960,
    margin: "0 auto",
    display: "flex",
    justifyContent: "space-between",
    gap: 12,
  },
};