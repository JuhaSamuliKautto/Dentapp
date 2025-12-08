import { useLocation, useNavigate } from "react-router-dom";

export default function CreateCourse_3() {
    const navigate = useNavigate();
    const location = useLocation();
    // Haetaan kurssin tiedot edellisistä vaiheista (courseId = kurssinTunnus, courseName = nimi)
    const { courseId = "", courseName = "" } = location.state || {};

    // HUOM: TÄMÄ CARDS-LISTA ON TÄLLÄ HETKELLÄ KOVAKOODATTUA MOCK-DATAA!
    // Oletetaan, että CreateCourse_2.jsx muokkaa tätä tulevaisuudessa.
    const cards = [
        { id: 1, name: "[Suorite 1]", date: "14.10." },
        { id: 2, name: "[Suorite 2]", date: "15.10." },
    ];

    /**
     * Käsittelee kurssin tallennuksen lähettämällä POST-pyynnön back-endille.
     */
    const handleSaveCourse = async () => {
        // 1. Kerää tiedot tallennusta varten, MUKAILLEN BACK-ENDIN DTO:TA:
        const courseData = {
            // DTO:n kenttä 'nimi'
            nimi: courseName, 
            // Back-endin vaatima vastuuopettajan tunnus (käytetään testiarvoa)
            vastuuopettajaUsername: "opettaja@uni.fi",
            // DTO:n kenttä suoritekorttien ID:ille
            suoritekorttiIdt: cards.map(c => c.id), 
            // Kurssin lyhytkoodi, eli Front-endin courseId
            kurssinTunnus: courseId, 
        };

        console.log("Lähetettävä kurssidata:", courseData);

        try {
            // Tarkista JWT-token
            const token = localStorage.getItem('token');
            if (!token) {
                alert("Kirjautumistokenia ei löydy. Kirjaudu sisään ensin.");
                return;
            }

            // *** DEBUGGAUSRIVI ***
            console.log(`Tokenin pituus LocalStoragesta: ${token.length}. Lähetetään pyyntö.`);

            // 2. Tee POST-pyyntö back-endin API:lle osoitteeseen /api/kurssi
            const response = await fetch('/api/kurssi', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // TÄMÄ ON KRIITTINEN! Authorization-header Bearer-etuliitteellä
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(courseData),
            });

            // 3. Käsittele vastaus
            if (!response.ok) {
                // Yritetään lukea virheviesti back-endiltä
                const errorText = await response.text(); 
                console.error("API Error Response:", errorText);
                
                throw new Error(`Tallennus epäonnistui (Status: ${response.status}). Palvelin vastasi: ${errorText.substring(0, 100)}...`);
            }

            const newCourse = await response.json();
            console.log("Kurssi tallennettu onnistuneesti:", newCourse);
            alert(`Kurssi "${newCourse.courseName || courseName}" luotu ja tallennettu!`);
            navigate("/teacher"); // Navigoi opettajan pääsivulle onnistumisen jälkeen

        } catch (error) {
            console.error("Kurssin luominen epäonnistui:", error);
            alert(`Kurssin luominen epäonnistui: ${error.message}`);
        }
    };


    return (
        <>
            <div style={styles.page}>
                <div style={styles.head}>
                    <div>
                        <h1 style={styles.h1}>Kurssin yhteenveto</h1>
                        <p style={styles.subtle}>
                            Tarkista kurssin suoritekortit ja lisää kurssi opiskelijoille.
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

                <section style={{ marginBottom: 24 }}>
                    <h2 style={styles.h2}>Suoritekortit</h2>
                    <ul style={styles.list}>
                        {cards.map((card) => (
                            <li key={card.id} style={styles.listItem}>
                                <span>{card.name}</span>
                                <span style={styles.date}>{card.date}</span>
                            </li>
                        ))}
                    </ul>
                </section>

                <section>
                    <h2 style={styles.h2}>Kurssin lisätyt opiskelijat</h2>
                    <p style={styles.noStudents}>(ei vielä lisätty opiskelijoille)</p>

                    <ds-button
                        ds-variant="primary"
                        onClick={() =>
                            navigate("/teacher/add-students", {
                                state: { courseId, courseName },
                            })
                        }
                    >
                        <span style={styles.buttonContent}>
                            <custom-ds-icon ds-name="add" ds-size="1rem"></custom-ds-icon>
                            Lisää kurssi opiskelijoille
                        </span>
                    </ds-button>
                </section>
            </div>

            <div style={styles.footerBar}>
                <div style={styles.footerInner}>
                    <ds-button
                        ds-variant="secondary"
                        onClick={() =>
                            navigate("/teacher/create-course-2", {
                                state: { courseId, courseName },
                            })
                        }
                    >
                        Takaisin
                    </ds-button>

                    <ds-button
                        ds-variant="primary"
                        onClick={handleSaveCourse} // <-- Kutsuu tallennusfunktiota
                    >
                        Tallenna
                    </ds-button>
                </div>
            </div>
        </>
    );
}

const styles = {
    // ... (tyylit pysyvät samoina)
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
    h2: { fontSize: 18, margin: "0 0 8px", color: "#00264d" },
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
    list: { listStyle: "none", padding: 0, margin: 0, maxWidth: 640 },
    listItem: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0.75rem 1rem",
        marginBottom: 8,
        background: "#f9f9f9",
        borderRadius: 8,
        border: "1px solid #ddd",
    },
    date: { marginLeft: "auto", paddingLeft: "1rem" },
    noStudents: { fontSize: 14, color: "#666", marginBottom: 12 },
    buttonContent: {
        display: "inline-flex",
        alignItems: "center",
        gap: "0.5rem",
        fontWeight: 600,
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