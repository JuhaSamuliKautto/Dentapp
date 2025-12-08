import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const mockYears = [
  {
    id: 1,
    label: "Vuosikurssi 2024",
    courses: [
      {
        id: 1,
        name: "Kurssin nimi",
        code: "Kurssin tunnus",
        group: "opiskelijaryhmän tunnus",
      },
    ],
  },
  {
    id: 2,
    label: "Vuosikurssi 2025",
    courses: [
      {
        id: 2,
        name: "Kurssin nimi",
        code: "Kurssin tunnus",
        group: "opiskelijaryhmän tunnus",
      },
      {
        id: 3,
        name: "Kurssin nimi 2",
        code: "Kurssin tunnus 2",
        group: "toinen opiskelijaryhmä",
      },
    ],
  },
  {
    id: 3,
    label: "Vuosikurssi 2026",
    courses: [],
  },
];

export default function TeacherReadyCourses() {
  const [openYearId, setOpenYearId] = useState(null);
  const navigate = useNavigate();

  const toggleYear = (id) => {
    setOpenYearId((prev) => (prev === id ? null : id));
  };

  return (
    <>
      <div style={styles.page}>
        <h1 style={styles.title}>Valmiit kurssit</h1>
        <hr style={styles.divider} />

        <div style={styles.yearList}>
          {mockYears.map((year) => {
            const isOpen = year.id === openYearId;
            return (
              <div key={year.id} style={styles.yearBlock}>
                {/* --- Accordion-pää --- */}
                <button
                  type="button"
                  style={styles.yearHeader}
                  onClick={() => toggleYear(year.id)}
                >
                  <span style={styles.chevron}>
                    <ds-icon
                      ds-name={isOpen ? "collapse_content" : "expand_content"}
                      ds-size="1.25rem"
                    ></ds-icon>
                  </span>
                  <span style={styles.yearLabel}>{year.label}</span>
                </button>

                {/* --- Sisältö: kurssikortit --- */}
                {isOpen && year.courses.length > 0 && (
                  <div style={styles.coursesWrapper}>
                    {year.courses.map((course) => (
                      <div key={course.id} style={styles.courseCard}>
                        <div style={styles.courseLine}>[{course.name}]</div>
                        <div style={styles.courseLine}>[{course.code}]</div>
                        <div style={styles.courseLine}>
                          Oppilaat: [{course.group}]
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            );
          })}
        </div>

        {/* Tilaa naville */}
        <div style={{ height: 16 }} />
      </div>

      {/* --- Takaisin-nappi footerissa --- */}
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
    padding: "16px 16px 120px", // tilaa footerille + navbarille
    backgroundColor: "#fff",
    minHeight: "100vh",
    boxSizing: "border-box",
  },
  title: {
    textAlign: "center",
    margin: "0 0 8px",
    fontSize: 18,
    color: "#1A1A1A",
  },
  divider: {
    border: 0,
    borderTop: "1px solid #cbd5e1",
    margin: "8px 0 16px",
  },
  yearList: {
    display: "flex",
    flexDirection: "column",
    gap: 4,
  },
  yearBlock: {
    marginBottom: 4,
  },
  yearHeader: {
    width: "100%",
    maxWidth: 480,
    margin: "0 auto",
    padding: "0.75rem 1rem",
    display: "flex",
    alignItems: "center",
    gap: 8,
    border: "none",
    borderBottom: "1px solid #e5e7eb",
    background: "#fff",
    cursor: "pointer",
    textAlign: "left",
  },
  chevron: {
    display: "flex",
    alignItems: "center",
  },
  yearLabel: {
    fontSize: 14,
  },
  coursesWrapper: {
    maxWidth: 480,
    margin: "0.5rem auto 0.75rem",
    display: "flex",
    flexDirection: "column",
    gap: 8,
  },
  courseCard: {
    background: "#f9f9f9",
    border: "1px solid #d4d4d4",
    borderRadius: 4,
    padding: "0.5rem 0.75rem",
    fontSize: 12,
  },
  courseLine: {
    lineHeight: 1.4,
  },
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56, // navin yläpuolelle
    padding: "8px 16px",
    background: "#fafafa",
    borderTop: "1px solid #cbd5e1",
    zIndex: 1000,
  },
  footerInner: {
    maxWidth: 960,
    margin: "0 auto",
  },
};