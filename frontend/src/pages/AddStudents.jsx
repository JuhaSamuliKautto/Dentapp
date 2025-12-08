import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function AddStudents() {
  const navigate = useNavigate();
  const location = useLocation();
  const { courseId = "", courseName = "" } = location.state || {};
  const [searchQuery, setSearchQuery] = useState("");

  const handleAddStudents = () => {
    // tässä voit myöhemmin oikeasti lisätä opiskelijat
    navigate("/teacher/create-course-3", { state: { courseId, courseName } });
  };

  return (
    <>
      <div style={styles.page}>
        <div style={styles.head}>
          <div>
            <h1 style={styles.h1}>Lisää opiskelijat kurssille</h1>
            <p style={styles.subtle}>
              Hae opiskelijaryhmä tunnuksella ja liitä se kurssiin.
            </p>
            {(courseName || courseId) && (
              <p style={styles.courseLine}>
                {courseName && `[${courseName}]`}{" "}
                {courseId && `[${courseId}]`}
              </p>
            )}
          </div>
        </div>

        <hr style={styles.divider} />

        <div style={{ maxWidth: 480 }}>
          <label style={styles.label}>Hae opiskelijaryhmä tunnuksella</label>
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={styles.input}
            placeholder="Esim. DENT-OP1"
          />
        </div>
      </div>

      <div style={styles.footerBar}>
        <div style={styles.footerInner}>
          <ds-button
            ds-variant="secondary"
            onClick={() =>
              navigate("/teacher/create-course-3", {
                state: { courseId, courseName },
              })
            }
          >
            Takaisin
          </ds-button>

          <ds-button ds-variant="primary" onClick={handleAddStudents}>
            Lisää opiskelijat
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
  },
  head: {
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "space-between",
    gap: 16,
    flexWrap: "wrap",
  },
  h1: { fontSize: 22, margin: "0 0 4px", color: "#00264d" },
  subtle: { margin: 0, color: "#444", fontSize: 14 },
  courseLine: {
    margin: "8px 0 0",
    fontSize: 14,
    color: "#111827",
    fontWeight: 500,
  },
  divider: {
    border: 0,
    borderTop: "1px solid #cbd5e1",
    margin: "12px 0 16px",
  },
  label: {
    display: "block",
    fontSize: 14,
    fontWeight: 500,
    marginBottom: 6,
  },
  input: {
    width: "100%",
    padding: "0.6rem 0.75rem",
    border: "1px solid #aaa",
    borderRadius: 4,
    fontSize: "1rem",
    boxSizing: "border-box",
  },
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56,
    padding: "8px 16px",
    backgroundColor: "#fafafa",
    borderTop: "1px solid #cbd5e1",
    zIndex: 1000,
    boxSizing: "border-box",
  },
  footerInner: {
    maxWidth: 960,
    margin: "0 auto",
    display: "flex",
    justifyContent: "space-between",
    gap: 8,
  },
};