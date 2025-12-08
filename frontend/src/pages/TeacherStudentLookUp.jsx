// src/pages/TeacherStudentLookUp.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function TeacherStudentLookUp() {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState("");

  const handleSearch = () => {
    if (searchValue.trim()) {
      navigate(
        `/teacher/student-performance?search=${encodeURIComponent(searchValue)}`
      );
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <div style={styles.container}>
      {/* Hakukuvake */}
      <div style={styles.iconContainer}>
        <custom-ds-icon
          ds-name="search"
          ds-size="4rem"
          ds-color="#1A1A1A"
        ></custom-ds-icon>
      </div>

      {/* QR-koodilla haku */}
      <div style={styles.qrCard}>
        <custom-ds-icon
          ds-name="location_on"
          ds-size="2rem"
          ds-color="#1A1A1A"
        ></custom-ds-icon>
        <span style={styles.qrText}>Hae QR-koodilla</span>
      </div>

      {/* Hakukenttä */}
      <div style={styles.inputSection}>
        <label style={styles.label}>Kirjoita nimi tai oppilasnumero</label>
        <input
          type="text"
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Maija Meikäläinen"
          style={styles.input}
        />
      </div>

      {/* Hae-nappi (oikeaan reunaan) */}
      <div style={styles.searchButtonRow}>
        <ds-button ds-variant="primary" onClick={handleSearch}>
          Hae
        </ds-button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    minHeight: "calc(100vh - 150px)",
    backgroundColor: "#fff",
    padding: "2rem 1.5rem",
  },

  iconContainer: {
    marginBottom: "2rem",
    marginTop: "1rem",
  },

  qrCard: {
    display: "flex",
    alignItems: "center",
    gap: "1rem",
    backgroundColor: "#fff",
    border: "1px solid #e0e0e0",
    borderRadius: "8px",
    padding: "1rem 1.5rem",
    width: "100%",
    maxWidth: "300px",
    marginBottom: "2rem",
    cursor: "pointer",
    boxShadow: "0 2px 4px rgba(0,0,0,0.05)",
  },

  qrText: {
    fontSize: "1rem",
    color: "#1A1A1A",
    fontWeight: 500,
  },

  inputSection: {
    width: "100%",
    maxWidth: "300px",
    marginBottom: "auto",
  },

  label: {
    display: "block",
    fontSize: "0.875rem",
    color: "#666",
    marginBottom: "0.5rem",
  },

  input: {
    width: "100%",
    padding: "0.875rem 1rem",
    fontSize: "1rem",
    border: "1px solid #e0e0e0",
    borderRadius: "8px",
    outline: "none",
  },

  /* UUSI: nappi oikeaan reunaan */
  searchButtonRow: {
    width: "100%",
    maxWidth: "300px",
    marginTop: "2rem",
    display: "flex",
    justifyContent: "flex-end", // ← tärkeä
  },
};