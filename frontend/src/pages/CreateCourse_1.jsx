import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CreateCourse_1() {
  const navigate = useNavigate();
  const [courseId, setCourseId] = useState("");
  const [courseName, setCourseName] = useState("");

  const goNext = () => {
    if (!courseId || !courseName) {
      // annetaan selaimen required-validoinnin hoitaa varsinaisesti
      return;
    }
    navigate("/teacher/create-course-2", {
      state: { courseId, courseName },
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    goNext();
  };

  return (
    <>
      <div style={styles.page}>
        <div style={styles.head}>
          <div>
            <h1 style={styles.h1}>Luo uusi kurssi</h1>
            <p style={styles.subtle}>
              Anna kurssin tunnus ja nimi jatkaaksesi suoritekorttien valintaan.
            </p>
          </div>
        </div>

        <hr style={styles.divider} />

        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.fieldGroup}>
            <label style={styles.label}>Kurssin tunnus</label>
            <input
              type="text"
              value={courseId}
              onChange={(e) => setCourseId(e.target.value)}
              style={styles.input}
              placeholder="Esim. DENT101"
              required
            />
          </div>

          <div style={styles.fieldGroup}>
            <label style={styles.label}>Kurssin nimi</label>
            <input
              type="text"
              value={courseName}
              onChange={(e) => setCourseName(e.target.value)}
              style={styles.input}
              placeholder="Esim. Hampaiden anatomia"
              required
            />
          </div>
        </form>
      </div>

      {/* footer-napit tabbarin yläpuolelle */}
      <div style={styles.footerBar}>
        <div style={styles.footerInner}>
          <ds-button
            ds-variant="secondary"
            onClick={() => navigate("/teacher")}
          >
            Peruuta
          </ds-button>

          <ds-button ds-variant="primary" onClick={goNext}>
            Seuraava
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
  },
  head: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 16,
    flexWrap: "wrap",
  },
  h1: {
    fontSize: 22,
    margin: "0 0 4px",
    color: "#00264d",
  },
  subtle: {
    margin: 0,
    color: "#444",
    fontSize: 14,
  },
  divider: {
    border: 0,
    borderTop: "1px solid #cbd5e1",
    margin: "12px 0 16px",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: 16,
    maxWidth: 480,
  },
  fieldGroup: {
    display: "flex",
    flexDirection: "column",
    gap: 6,
  },
  label: {
    fontWeight: 500,
    fontSize: 14,
  },
  input: {
    width: "100%",
    padding: "0.6rem 0.75rem",
    border: "1px solid #aaa",
    borderRadius: 4,
    fontSize: "1rem",
  },
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56, // about tabbar height
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