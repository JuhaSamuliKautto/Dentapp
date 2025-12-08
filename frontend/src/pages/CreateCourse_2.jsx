import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function CreateCourse_2() {
  const navigate = useNavigate();
  const location = useLocation();
  const { courseId = "", courseName = "" } = location.state || {};

  // UUSI TILA: Valittujen korttien ID:t
  const [selectedCardIds, setSelectedCardIds] = useState([]);

  // Kovakoodattu mock-data korteista (tämä pitäisi normaalisti hakea API:sta)
  const existingCards = [
    { id: 1, name: "[Suorite 1]", date: "14.10." },
    { id: 2, name: "[Suorite 2]", date: "15.10." },
    { id: 3, name: "[Suorite 3]", date: "16.10." },
    { id: 4, name: "[Suorite 4]", date: "17.10." },
  ];

  /**
   * Lisää tai poistaa kortin ID:n valituista
   * @param {number} cardId kortin yksilöivä ID
   */
  const handleCardToggle = (cardId) => {
    setSelectedCardIds(prevIds => {
      if (prevIds.includes(cardId)) {
        // Poista valittu ID
        return prevIds.filter(id => id !== cardId);
      } else {
        // Lisää uusi ID
        return [...prevIds, cardId];
      }
    });
  };

  /**
   * Siirry seuraavaan vaiheeseen ja välitä KAIKKI tarvittavat tiedot
   */
  const handleNext = () => {
    // Tässä vaiheessa emme tee API-kutsua, vaan siirrämme tiedon eteenpäin.
    navigate("/teacher/create-course-3", {
      state: { 
        courseId, 
        courseName, 
        // LÄHETETÄÄN VALITUT ID:t seuraavaan vaiheeseen
        cardIds: selectedCardIds 
      }
    });
  };

  return (
    <>
      <div style={styles.page}>
        {/* ---------- YLÄOSA: otsikko + sisältö ---------- */}
        <div style={styles.head}>
          <div>
            <h1 style={styles.h1}>Suoritekortit kurssille</h1>
            <p style={styles.subtle}>
              Valitse tai luo suoritekortit, jotka kuuluvat kurssille.
            </p>

            {(courseName || courseId) && (
              <div style={styles.courseInfo}>
                {courseName && (
                  <div style={styles.courseNameLine}>{courseName}</div>
                )}
                {courseId && <div style={styles.courseIdLine}>{courseId}</div>}
              </div>
            )}
          </div>
        </div>

        <hr style={styles.divider} />

        {/* ylätoiminnot */}
        <div style={styles.buttonSection}>
          <ds-button
            ds-variant="secondary"
            onClick={() =>
              navigate("/teacher/create-card", { state: { courseId, courseName } })
            }
          >
            <span style={styles.buttonContent}>
              <custom-ds-icon ds-name="add" ds-size="1rem"></custom-ds-icon>
              Luo uusi suoritekortti
            </span>
          </ds-button>

          <ds-button
            ds-variant="secondary"
            onClick={() =>
              navigate("/teacher/card-selection", { state: { courseId, courseName } })
            }
          >
            <span style={styles.buttonContent}>
              <custom-ds-icon ds-name="archive" ds-size="1rem"></custom-ds-icon>
              Tuo suoritekortti
            </span>
          </ds-button>
        </div>

        {/* listaus */}
        <section style={{ marginTop: 24 }}>
          <h2 style={styles.h2}>Tämän kurssin valmiit suoritekortit</h2>
          <div style={styles.cardsList}>
            {existingCards.map((card) => (
              <div 
                key={card.id} 
                // KÄYTÄ toggle-funktiota klikatessa
                onClick={() => handleCardToggle(card.id)}
                // MUOKATTU: Anna tyyli valinnalle
                style={{
                    ...styles.cardRow,
                    border: selectedCardIds.includes(card.id) 
                      ? "2px solid #0056b3" // Sininen reuna valittuna
                      : "1px solid #ddd",
                    cursor: 'pointer',
                    padding: selectedCardIds.includes(card.id) ? '0.75rem 1rem' : '0.8125rem 1rem' // Korjaa pading reunan takia
                }}
              >
                <span style={styles.cardName}>{card.name}</span>
                <span style={styles.cardDate}>{card.date}</span>
              </div>
            ))}
          </div>
        </section>
      </div>

      {/* ---------- ALAPALKKI: Takaisin / Seuraava ---------- */}
      <div style={styles.footerBar}>
        <div style={styles.footerInner}>
          <ds-button
            ds-variant="secondary"
            onClick={() =>
              navigate("/teacher/create-course-1", { state: { courseId, courseName } })
            }
          >
            Takaisin
          </ds-button>

          <ds-button
            ds-variant="primary"
            onClick={handleNext} // <-- MUUTETTU: Kutsuu uutta funktiota
            // Poista valinta, jos yhtään korttia ei ole valittu (valinnainen)
            disabled={selectedCardIds.length === 0} 
          >
            Seuraava
          </ds-button>
        </div>
      </div>
    </>
  );
}

// TYYLIT PYSYVÄT SAMOINA, PAITSI pieni muutos cardRow:ssa
const styles = {
  page: {
    maxWidth: 960,
    width: "100%",
    margin: "0 auto",
    padding: "16px 16px 120px", // tilaa ala-palkille + tabbarille
    boxSizing: "border-box",
  },
  head: {
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "space-between",
    gap: 16,
    flexWrap: "wrap",
  },
  h1: {
    fontSize: 22,
    margin: "0 0 4px",
    color: "#00264d",
  },
  h2: {
    fontSize: 18,
    margin: "0 0 8px",
    color: "#00264d",
  },
  subtle: {
    margin: 0,
    color: "#444",
    fontSize: 14,
  },

  // UUSI: kurssin nimi & tunnus ilman laatikkoa, isompana
  courseInfo: {
    marginTop: 12,
    textAlign: "center",
    width: "100%",
  },

  courseNameLine: {
    fontSize: 18,
    fontWeight: 600,
    color: "#111827",
    marginBottom: 2,
    textAlign: "center",
  },

  courseIdLine: {
    fontSize: 16,
    fontWeight: 500,
    color: "#111827",
    textAlign: "center",
  },

  divider: {
    border: 0,
    borderTop: "1px solid #cbd5e1",
    margin: "12px 0 16px",
  },
  buttonSection: {
    display: "flex",
    flexDirection: "column",
    gap: 12,
    maxWidth: 480,
  },
  buttonContent: {
    display: "inline-flex",
    alignItems: "center",
    gap: "0.5rem",
    fontWeight: 600,
  },
  cardsList: {
    display: "flex",
    flexDirection: "column",
    gap: 8,
    maxWidth: 640,
  },
  cardRow: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "0.75rem 1rem",
    background: "#f9f9f9",
    borderRadius: 8,
    border: "1px solid #ddd",
    transition: 'border 0.15s ease-in-out', // Lisää animaatio
  },
  cardName: {
    fontSize: 15,
  },
  cardDate: {
    fontSize: 13,
    color: "#fff",
    backgroundColor: "#666",
    padding: "0.25rem 0.75rem",
    borderRadius: 4,
  },

  // kiinteä ala-palkki
  footerBar: {
    position: "fixed",
    left: 0,
    right: 0,
    bottom: 56, // tabbarin yläpuolelle
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