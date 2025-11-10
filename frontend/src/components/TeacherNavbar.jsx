import { Link, useNavigate } from "react-router-dom";

export default function TeacherNavbar({ onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("role");
    onLogout();
    navigate("/");
  };

  return (
    <nav style={styles.nav}>
      <Link style={styles.link} to="/teacher">Home</Link>
      <Link style={styles.link} to="/teacher/classes">My Classes</Link>
      <button onClick={handleLogout} style={styles.button}>Logout</button>
    </nav>
  );
}

const styles = {
  nav: {
    display: "flex",
    gap: "1rem",
    background: "#1e40af",
    padding: "1rem 2rem",
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    zIndex: 1000,
    alignItems: "center",
    fontFamily: "Arial, sans-serif",
  },
  link: {
    color: "white",
    textDecoration: "none",
    fontWeight: "bold",
    padding: "0.5rem 1rem",
    borderRadius: "5px",
    transition: "background 0.2s",
  },
  button: {
    color: "white",
    background: "#dc2626",
    border: "none",
    padding: "0.5rem 1rem",
    cursor: "pointer",
    borderRadius: "5px",
    marginLeft: "auto",
    fontWeight: "bold",
  },
};


