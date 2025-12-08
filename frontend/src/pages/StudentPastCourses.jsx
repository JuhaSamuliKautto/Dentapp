// src/pages/StudentPastCourses.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

// Sama rakenne kuin TeacherReadyCourses, mutta opiskelijan menneet kurssit
const mockYears = [
  {
    id: 1,
    label: "Vuosikurssi 2024",
    courses: [
      {
        id: 1,
        name: "Proteettinen hammashoito",
        code: "PROT-101",
        group: "Ryhmä A",
      },
      {
        id: 2,
        name: "Suukirurgia",
        code: "SUKI-201",
        group: "Ryhmä B",
      },
    ],
  },
  {
    id: 2,
    label: "Vuosikurssi 2023",
    courses: [
      {
        id: 3,
        name: "Kariologia",
        code: "KARI-101",
        group: "Ryhmä A",
      },
      {
        id: 4,
        name: "Parodontologia",
        code: "PARO-201",
        group: "Ryhmä C",
      },
      {
        id: 5,
        name: "Oikomishoito",
        code: "OIKO-301",
        group: "Ryhmä A",
      },
    ],
  },
  {
    id: 3,
    label: "Vuosikurssi 2022",
    courses: [],
  },
];

export default function StudentPastCourses() {
  const [openYearId, setOpenYearId] = useState(null);
  const navigate = useNavigate();

  const toggleYear = (id) => {
    setOpenYearId((prev) => (prev === id ? null : id));
  };

  return (
    <>
      <div style={styles.page}>
        <h1 style={styles.title}>Menneet kurssit</h1>
        <hr style={styles.divider} />

        <div style={styles.yearList}>
          {mockYears.map((year) => {
            const isOpen = year.id === openYearId;
            return (
              <div key={year.id} style={styles.yearBlock}>
                {/* Accordion-pää – sama layout kuin TeacherReadyCourses */}
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

                {/* Sisältö: kurssit samassa korttityylissä */}
                {isOpen && year.courses.length > 0 && (
                  <div style={styles.coursesWrapper}>
                    {year.courses.map((course) => (
                      <div key={course.id} style={styles.courseCard}>
                        <div style={styles.courseLine}>{course.name}</div>
                        <div style={styles.courseLine}>{course.code}</div>
                        <div style={styles.courseLine}>
                          Ryhmä: {course.group}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            );
          })}
        </div>

        {/* pieni tyhjä tila naville */}
        <div style={{ height: 16 }} />
      </div>

      {/* Takaisin-nappi footerissa – sama tyyli kuin TeacherReadyCourses */}
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
    padding: "16px 16px 120px", // tilaa footerille + StudentNavbarille
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
    // ei flex-endiä → nappi luonnollisesti vasemmassa reunassa
  },
};