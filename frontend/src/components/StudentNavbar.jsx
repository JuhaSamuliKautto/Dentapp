import { NavLink, useNavigate } from "react-router-dom";
import { MdHome, MdSchool, MdPerson, MdLogout } from "react-icons/md";

export default function StudentNavbar({ onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("role");
    onLogout();
    navigate("/");
  };

  return (
    <div style={styles.navbar}>
      <NavLink
        to="/student"
        style={({ isActive }) => ({
          ...styles.link,
          color: isActive ? "#000" : "#555",
        })}
      >
        <MdHome size={22} />
        <span>Etusivu</span>
      </NavLink>

      <NavLink
        to="/student/profile"
        style={({ isActive }) => ({
          ...styles.link,
          color: isActive ? "#000" : "#555",
        })}
      >
        <MdPerson size={22} />
        <span>Omat tiedot</span>
      </NavLink>

      <div style={styles.link} onClick={handleLogout}>
        <MdLogout size={22} />
        <span>Kirjaudu ulos</span>
      </div>
    </div>
  );
}

const styles = {
  navbar: {
    position: "fixed",
    bottom: 0,
    left: 0,
    right: 0,
    height: "65px",
    background: "#ffffff",
    borderTop: "1px solid #d0d0d0",
    display: "flex",
    justifyContent: "space-around",
    alignItems: "center",
    padding: "0 1rem",
    zIndex: 1000,
  },

  link: {
    textDecoration: "none",
    fontSize: "0.75rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "4px",
    color: "#555",
    cursor: "pointer",
  },
};
