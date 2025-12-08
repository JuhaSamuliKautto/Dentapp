// src/pages/StudentCoursesView.jsx
import { useNavigate, useSearchParams } from "react-router-dom";

const mockCourseData = {
  name: "[Kurssin nimi]",
  code: "[Kurssin tunnus]",
  performances: [
    { id: 1, name: "[Suorite 1]", date: "27.10.", status: "approved" },
    { id: 2, name: "[Suorite 2]", date: "28.10.", status: "pending" },
    { id: 3, name: "[Suorite 3]", date: "29.10.", status: "rejected" },
    { id: 4, name: "[Suorite 4]", date: "30.10.", status: "not_done" },
  ],
};

const statusColors = {
  approved: { bg: "#d4edda", border: "#28a745", dot: "#28a745" },
  pending: { bg: "#fff3cd", border: "#ffc107", dot: "#ffc107" },
  rejected: { bg: "#f8d7da", border: "#dc3545", dot: "#dc3545" },
  not_done: { bg: "#e9ecef", border: "#6c757d", dot: "#6c757d" },
};

const statusLabels = {
  approved: "Hyväksytty",
  pending: "Tehty, ei vielä hyväksytty",
  rejected: "Hylätty",
  not_done: "Ei tehty",
};

export default function StudentCoursesView() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  // course-id löytyy tarvittaessa searchParamsista

  return (
    <>
      <div style={styles.page}>
        {/* Kurssin otsikko */}
        <div style={styles.header}>
          <h1 style={styles.courseName}>{mockCourseData.name}</h1>
          <p style={styles.courseCode}>{mockCourseData.code}</p>
        </div>

        {/* Selite */}
        <div style={styles.legend}>
          {Object.entries(statusLabels).map(([key, label]) => (
            <div key={key} style={styles.legendItem}>
              <span
                style={{
                  ...styles.legendDot,
                  backgroundColor: statusColors[key].dot,
                }}
              />
              <span style={styles.legendLabel}>{label}</span>
            </div>
          ))}
        </div>

        {/* Suoritteet */}
        <div style={styles.performancesList}>
          {mockCourseData.performances.map((performance) => (
            <div
              key={performance.id}
              style={{
                ...styles.performanceCard,
                backgroundColor: statusColors[performance.status].bg,
                borderColor: statusColors[performance.status].border,
              }}
            >
              <span style={styles.performanceName}>
                {performance.name} {performance.date}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* Alapalkki: Takaisin (DS-nappi) */}
      <div style={styles.footerBar}>
        <div style={styles.footerInner}>
          <ds-button ds-variant="secondary" onClick={() => navigate(-1)}>
            Takaisin
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
    padding: "16px 16px 120px", // tilaa footerille + tabbarille
    boxSizing: "border-box",
    backgroundColor: "#fff",
    minHeight: "100vh",
  },
  header: {
    textAlign: "center",
    marginBottom: "1.5rem",
  },
  courseName: {
    fontSize: "1.25rem",
    fontWeight: 600,
    color: "#1A1A1A",
    margin: 0,
  },
  courseCode: {
    fontSize: "0.9rem",
    color: "#555",
    margin: 0,
  },
  legend: {
    display: "flex",
    flexDirection: "column",
    gap: "0.5rem",
    marginBottom: "1.5rem",
  },
  legendItem: {
    display: "flex",
    alignItems: "center",
    gap: "0.5rem",
  },
  legendDot: {
    width: 10,
    height: 10,
    borderRadius: "50%",
  },
  legendLabel: {
    fontSize: "0.9rem",
    color: "#1A1A1A",
  },
  performancesList: {
    display: "flex",
    flexDirection: "column",
    gap: "0.5rem",
  },
  performanceCard: {
    border: "1px solid transparent",
    borderRadius: 6,
    padding: "0.75rem 1rem",
  },
  performanceName: {
    fontSize: "0.95rem",
    color: "#1A1A1A",
  },

  // Footer samassa hengessä kuin CreateCourse-sivuilla
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56, // StudentNavbarin yläpuolelle
    padding: "8px 16px",
    backgroundColor: "#fafafa",
    boxShadow: "0 -2px 4px rgba(0,0,0,0.06)",
  },
  footerInner: {
    maxWidth: 960,
    margin: "0 auto",
    display: "flex",
    justifyContent: "flex-start", // ← nyt nappi vasemmassa reunassa
  },
};