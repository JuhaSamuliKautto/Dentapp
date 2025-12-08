import { useNavigate } from "react-router-dom";

const mockCategories = [
  {
    id: 1,
    name: "Taitopaja",
    coursesTotal: 3,
    coursesIncomplete: 2,
  },
  {
    id: 2,
    name: "Kariologia",
    coursesTotal: 10,
    coursesIncomplete: 8,
  },
];

export default function StudentFrontPage() {
  const navigate = useNavigate();

  return (
    <div style={styles.container}>
      <div style={styles.categoriesList}>
        {mockCategories.map((category) => (
          <div
            key={category.id}
            style={styles.categoryCard}
            onClick={() =>
              navigate(`/student/course-select?category=${category.id}`)
            }
          >
            <div style={styles.categoryHeader}>
              <span style={styles.categoryName}>{category.name}</span>
            </div>
            <div style={styles.categoryStats}>
              <div style={styles.statRow}>
                <span style={styles.statLabel}>Kursseja yhteensä</span>
                <span style={styles.statValue}>{category.coursesTotal}</span>
              </div>
              <div style={styles.statRow}>
                <span style={styles.statLabel}>Kursseja suorittamatta</span>
                <span style={styles.statValue}>
                  {category.coursesIncomplete}
                </span>
              </div>
            </div>
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
    minHeight: "100vh",
    backgroundColor: "#fff",
    padding: "1rem",
    paddingBottom: "90px", // tilaa alapalkille
    boxSizing: "border-box",
  },
  categoriesList: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  categoryCard: {
    border: "1px solid #e0e0e0",
    borderRadius: "8px",
    backgroundColor: "#fff",
    cursor: "pointer",
    overflow: "hidden",
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
  categoryStats: {
    padding: "0.75rem 1rem",
  },
  statRow: {
    display: "flex",
    justifyContent: "space-between",
    padding: "0.25rem 0",
  },
  statLabel: {
    fontSize: "0.9rem",
    color: "#1A1A1A",
  },
  statValue: {
    fontSize: "0.9rem",
    fontWeight: 500,
    color: "#1A1A1A",
  },
};