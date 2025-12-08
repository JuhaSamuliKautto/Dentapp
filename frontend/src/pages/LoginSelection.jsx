import { useNavigate } from "react-router-dom";
import hyLogo from "../assets/HY_tunnus_FI_BW.svg";

export default function LoginSelection() {
  const navigate = useNavigate();

  return (
    <div style={styles.container}>
      {/* HY-logo */}
      <img src={hyLogo} alt="Helsingin yliopisto" style={styles.logo} />

      {/* App title */}
      <h1 style={styles.title}>Dentapp</h1>
      <h2 style={styles.subtitle}>Tervetuloa</h2>

      {/* Buttons */}
      <div style={styles.buttonContainer}>
        <ds-button ds-variant="primary" onClick={() => navigate("/teacher-login")} style={styles.button}>
          Kirjaudu opettajana
        </ds-button>
        <ds-button ds-variant="primary" onClick={() => navigate("/student-login")} style={styles.button}>
          Kirjaudu oppilaana
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
    justifyContent: "center",
    minHeight: "100vh",
    backgroundColor: "#fff",
    textAlign: "center",
    padding: "1.5rem",
  },
  logo: {
    width: "90px",
    height: "90px",
    marginBottom: "1rem",
  },
  title: {
    fontWeight: 800,
    fontSize: "3.0rem",
    color: "#1A1A1A",
    marginBottom: "0.3rem",
  },
  subtitle: {
    fontWeight: 500,
    fontSize: "1.5rem",
    color: "#1A1A1A",
    marginBottom: "2rem",
  },
  buttonContainer: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
    width: "80%",
    maxWidth: "260px",
  },
  button: {
    backgroundColor: "#0058A3",
    fontWeight: 600,
  },
};

