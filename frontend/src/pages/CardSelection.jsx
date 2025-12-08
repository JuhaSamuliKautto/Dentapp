import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

// ---------- DS WRAPPERS (EI IMPORTTEJA) ----------

const DSCard = ({ heading, children, ...rest }) => (
  <ds-card
    {...(heading ? { "ds-heading": heading } : {})}
    {...rest}
    style={{ display: "block", maxWidth: 960, margin: "0 auto" }}
  >
    <div slot="content" style={{ padding: 24 }}>{children}</div>
  </ds-card>
);

const DSButton = ({ children, variant = "primary", icon, size, ...rest }) => (
  <ds-button
    ds-variant={variant}
    {...(size ? { "ds-size": size } : {})}
    {...rest}
  >
    <span
      style={{
        display: "inline-flex",
        alignItems: "center",
        gap: "0.5rem",
        fontWeight: 600,
      }}
    >
      {icon && (
        <custom-ds-icon ds-name={icon} ds-size="1rem"></custom-ds-icon>
      )}
      {children}
    </span>
  </ds-button>
);

export default function CardSelection() {
  const navigate = useNavigate();
  const [selectedCards, setSelectedCards] = useState([]);

  // Mock data - esimerkki suoritekorteista
  const availableCards = [
    {
      id: "h3-01",
      title: "Karieksen havainnointi",
      description: "Kariologian taitopajan perussuorite",
      course: "H3 Kariologia",
      version: "2024",
    },
    {
      id: "h3-02",
      title: "IRM/Liner poistaminen",
      description: "Vanhan IRM/Li -materiaalin poistoharjoitus",
      course: "H3 Kariologia",
      version: "2024",
    },
    {
      id: "h3-03",
      title: "Sidostaminen (bonding)",
      description: "Sidosaineen oikeaoppinen käyttö ja kovetus",
      course: "H3 Kariologia",
      version: "2024",
    },
    {
      id: "h3-04",
      title: "Muovin käsittely I",
      description: "Muovimateriaalien muotoilu ja työskentely",
      course: "H3 Kariologia",
      version: "2024",
    },
    {
      id: "h3-05",
      title: "I luokan kaviteetti (Frasaco)",
      description: "I-lk kaviteetin preparointi ja paikkaus",
      course: "H3 Kariologia",
      version: "2024",
    },
    {
      id: "h3-06",
      title: "II luokan kaviteetti d.16",
      description: "Säästävä ja perinteinen II-luokan kaviteetti",
      course: "H3 Kariologia",
      version: "2024",
    },
  ];

  const toggleCardSelection = (cardId) => {
    setSelectedCards((prev) =>
      prev.includes(cardId)
        ? prev.filter((id) => id !== cardId)
        : [...prev, cardId]
    );
  };

  const handleAddToCourse = () => {
    if (selectedCards.length === 0) {
      alert("Valitse vähintään yksi kortti");
      return;
    }
    // Tässä kohtaa myöhemmin lähetetään valinta backendille
    alert(`Lisätty ${selectedCards.length} korttia kurssille (demo)`);
    // ei navigatea → jäädään sivulle
  };

  return (
    <>
      {/* PÄÄSISÄLTÖ */}
      <div
        style={{
          minHeight: "calc(100vh - 0px)",
          padding: "16px 16px 120px", // tilaa footerille + tabbarille
          backgroundColor: "#f5f5f5",
          boxSizing: "border-box",
        }}
      >
        <div style={{ width: "100%", maxWidth: 960, margin: "0 auto" }}>
          {/* Sivun otsikko */}
          <div
            style={{
              marginBottom: 16,
            }}
          >
            <h1
              style={{
                fontSize: 22,
                margin: "0 0 4px",
                color: "#00264d",
              }}
            >
              Valitse suoritekortit
            </h1>
            <p
              style={{
                margin: 0,
                color: "#444",
                fontSize: 14,
              }}
            >
              Valitse kortit, jotka haluat liittää kurssille.
            </p>
          </div>

          {/* Saatavilla olevat kortit */}
          <DSCard heading="Käytettävissä olevat suoritekortit">
            <div style={{ marginTop: 8 }}>
              {availableCards.length === 0 ? (
                <div
                  style={{
                    padding: 12,
                    border: "1px dashed #d1d5db",
                    borderRadius: 8,
                    color: "#6b7280",
                    textAlign: "center",
                    fontSize: 14,
                  }}
                >
                  Ei suoritekortteja saatavilla. Luo ensin kortteja.
                </div>
              ) : (
                availableCards.map((card) => {
                  const isSelected = selectedCards.includes(card.id);
                  return (
                    <div
                      key={card.id}
                      onClick={() => toggleCardSelection(card.id)}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                        padding: 16,
                        borderRadius: 10,
                        marginBottom: 10,
                        border: "2px solid",
                        borderColor: isSelected ? "#00264d" : "#d1d5db",
                        backgroundColor: isSelected ? "#e6f2ff" : "#fff",
                        boxShadow: isSelected
                          ? "0 2px 8px rgba(0, 38, 77, 0.15)"
                          : "none",
                        cursor: "pointer",
                        transition: "all 0.15s ease",
                      }}
                    >
                      <div
                        style={{
                          display: "flex",
                          alignItems: "flex-start",
                          gap: 12,
                          flex: 1,
                        }}
                      >
                        <div style={{ paddingTop: 2 }}>
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => {}}
                            style={{
                              width: 18,
                              height: 18,
                              cursor: "pointer",
                            }}
                          />
                        </div>
                        <div style={{ flex: 1 }}>
                          <h3
                            style={{
                              margin: "0 0 4px",
                              fontSize: 16,
                              fontWeight: 600,
                              color: "#00264d",
                            }}
                          >
                            {card.title}
                          </h3>
                          <p
                            style={{
                              margin: "0 0 8px",
                              fontSize: 14,
                              color: "#4b5563",
                            }}
                          >
                            {card.description}
                          </p>
                          <div
                            style={{
                              display: "flex",
                              gap: 8,
                              flexWrap: "wrap",
                              fontSize: 12,
                            }}
                          >
                            <span
                              style={{
                                padding: "2px 8px",
                                borderRadius: 4,
                                backgroundColor: "#f3f4f6",
                                color: "#374151",
                              }}
                            >
                              📚 {card.course}
                            </span>
                            <span
                              style={{
                                padding: "2px 8px",
                                borderRadius: 4,
                                backgroundColor: "#eef2ff",
                                color: "#312e81",
                              }}
                            >
                              v{card.version}
                            </span>
                          </div>
                        </div>
                      </div>

                      <div style={{ display: "flex", alignItems: "center" }}>
                        <DSButton
                          variant="secondary"
                          size="small"
                          icon="visibility"
                          onClick={(e) => {
                            e.stopPropagation();
                            alert("Esikatselu (demo)");
                          }}
                        >
                          Esikatsele
                        </DSButton>
                      </div>
                    </div>
                  );
                })
              )}
            </div>
          </DSCard>

          {/* Valitut kortit */}
          {selectedCards.length > 0 && (
            <div style={{ marginTop: 16 }}>
              <DSCard heading={`Valitut kortit (${selectedCards.length})`}>
                <div
                  style={{
                    marginTop: 8,
                    display: "flex",
                    flexWrap: "wrap",
                    gap: 8,
                  }}
                >
                  {selectedCards.map((cardId) => {
                    const card = availableCards.find((c) => c.id === cardId);
                    return (
                      <div
                        key={cardId}
                        style={{
                          display: "inline-flex",
                          alignItems: "center",
                          gap: 6,
                          padding: "6px 12px",
                          backgroundColor: "#00264d",
                          color: "#fff",
                          borderRadius: 20,
                          fontSize: 14,
                        }}
                      >
                        <span>{card?.title}</span>
                        <button
                          type="button"
                          onClick={() => toggleCardSelection(cardId)}
                          aria-label="Poista kortti valinnasta"
                          style={{
                            background: "none",
                            border: "none",
                            color: "#fff",
                            fontSize: 16,
                            cursor: "pointer",
                            width: 20,
                            height: 20,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            borderRadius: "50%",
                          }}
                        >
                          ✕
                        </button>
                      </div>
                    );
                  })}
                </div>
              </DSCard>
            </div>
          )}
        </div>
      </div>

      {/* KIINTEÄ ALAPALKKI: Takaisin / Lisää kurssille */}
      <div
        style={{
          position: "fixed",
          left: 0,
          right: 0,
          bottom: 56, // tabbarin yläpuolelle
          padding: "8px 16px",
          backgroundColor: "#fafafa",
          borderTop: "1px solid #cbd5e1",
          zIndex: 1000,
          boxSizing: "border-box",
        }}
      >
        <div
          style={{
            maxWidth: 960,
            margin: "0 auto",
            display: "flex",
            justifyContent: "space-between",
            gap: 8,
          }}
        >
          <DSButton
            variant="secondary"
            icon="arrow-left"
            onClick={() => navigate("/teacher/create-course-2")}
          >
            Takaisin
          </DSButton>

          <DSButton
            icon="add"
            onClick={handleAddToCourse}
            disabled={selectedCards.length === 0}
          >
            Lisää kurssille ({selectedCards.length})
          </DSButton>
        </div>
      </div>
    </>
  );
}