import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

// HUOM: Jos back-end ei ole samalla portilla (8080), 
// tässä koodissa pitää olla koko osoite!
// Oletetaan, että App.jsx tai muu konfiguraatio käsittelee http://localhost:8080-etuliitteen.

export default function LoginPage({ onLogin }) {
    const navigate = useNavigate();
    const { role } = useParams();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    /**
     * Käsittelee kirjautumislomakkeen lähetyksen.
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setIsLoading(true);

        const targetRole = role === "teacher-login" ? "teacher" : "student";
        const targetPath = role === "teacher-login" ? "/teacher" : "/student";

        // *** KORJAUS ALKAA TÄSTÄ ***
        // Aiemmin sallittu osoite Spring Securityssä oli /api/auth/login
        const endpointsToTry = [
            '/api/auth/login' // <-- AINOA NYT SALLITTU POLKU BACK-ENDISSÄ
        ];
        // *** KORJAUS PÄÄTTYY TÄHÄN ***

        let success = false;
        let finalError = null;

        for (const endpoint of endpointsToTry) {
            try {
                console.log(`Yritetään kirjautumista osoitteeseen: ${endpoint}`);

                const response = await fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username, password }),
                });

                if (response.ok) {
                    const data = await response.json();

                    if (data.token) {
                        // *** KRIITTINEN: TALLENNA TOKEN ***
                        localStorage.setItem("token", data.token);
                    }

                    localStorage.setItem("role", targetRole);
                    onLogin(targetRole);
                    navigate(targetPath);
                    success = true;
                    break; // Onnistui, poistu loopista

                } else if (response.status === 401 || response.status === 403) {
                    // 401 Unauthorized tai 403 Forbidden: Tunnus/salasana väärin
                    console.log(`Kirjautuminen ${endpoint}-osoitteeseen epäonnistui (Status ${response.status}).`);
                    finalError = "Virheelliset kirjautumistiedot. Tarkista tunnus ja salasana.";
                    break; // Ei tarvitse yrittää muita osoitteita, koska virhe on credentialeissa
                } else if (response.status === 404) {
                    // 404 Not Found: Osoite ei ole olemassa. Tässä tapauksessa pitäisi tapahtua vain kerran.
                    console.log(`Endpoint ${endpoint} ei löytynyt.`);
                } else {
                    // Muu virhe (esim. 500 Internal Server Error)
                    finalError = `Palvelin palautti virheen: ${response.status} ${response.statusText}`;
                    break;
                }

            } catch (error) {
                // Yhteysvirhe (esim. back-end ei ole päällä)
                console.error(`Yhteysvirhe osoitteeseen ${endpoint}:`, error);
                finalError = "Yhteys back-endiin epäonnistui. Varmista, että Spring Boot on käynnissä.";
                break;
            }
        }

        setIsLoading(false);

        if (!success) {
            // Näytä viimeisin tai yleisin virheviesti
            setError(finalError || "Kirjautuminen epäonnistui. Tarkista tunnus ja salasana (varmista, että käytät oikeita back-endin tunnuksia, esim. opettaja@uni.fi / password).");
        }
    };

    const titleText = role === "teacher-login" ? "Opettajan kirjautuminen" : "Oppilaan kirjautuminen";

    return (
        <div style={styles.wrapper}>
            <div style={styles.card}>
                <h3 style={styles.header}>{titleText}</h3>
                {/* NÄYTÄ VIRHEVIESTI */}
                {error && <div style={styles.errorMessage}>{error}</div>}
                <form onSubmit={handleSubmit} style={styles.form}>
                    <label style={styles.label}>Tunnus/sähköposti</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={styles.input}
                        required
                        disabled={isLoading}
                    />

                    <label style={styles.label}>Salasana</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={styles.input}
                        required
                        disabled={isLoading}
                    />

                    <ds-button
                        ds-variant="primary"
                        type="submit"
                        style={styles.loginButton}
                        disabled={isLoading}
                    >
                        {isLoading ? "Kirjaudutaan..." : "Kirjaudu"}
                    </ds-button>
                </form>
            </div>
        </div>
    );
}

const styles = {
    wrapper: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        backgroundColor: "var(--hy-gray)",
    },
    card: {
        backgroundColor: "#fff",
        borderRadius: "1rem",
        padding: "2rem",
        width: "90%",
        maxWidth: "350px",
        boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
    },
    header: {
        color: "#666",
        fontWeight: 500,
        fontSize: "0.9rem",
        marginBottom: "1rem",
    },
    form: {
        display: "flex",
        flexDirection: "column",
        gap: "1rem",
    },
    label: {
        textAlign: "left",
        fontWeight: 500,
        fontSize: "0.9rem",
    },
    input: {
        width: "100%",
        padding: "0.5rem",
        border: "1px solid #aaa",
        borderRadius: "4px",
        fontSize: "1rem",
    },
    loginButton: {
        backgroundColor: "var(--hy-blue)",
        fontWeight: 600,
        marginTop: "0.5rem",
    },
    errorMessage: {
        color: '#dc3545',
        backgroundColor: '#f8d7da',
        border: '1px solid #f5c6cb',
        borderRadius: '0.25rem',
        padding: '0.75rem',
        marginBottom: '1rem',
        fontSize: '0.9rem',
    }
};