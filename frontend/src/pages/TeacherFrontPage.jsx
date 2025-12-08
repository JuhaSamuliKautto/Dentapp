import { useNavigate } from "react-router-dom";
import { MdSearch, MdAdd, MdContentCopy } from "react-icons/md";

export default function TeacherFrontPage() {
  const navigate = useNavigate();

  return (
    <div style={styles.container}>

      <h2 style={styles.title}>Tervetuloa opettaja</h2>

      <div
        style={styles.card}
        onClick={() => navigate("/teacher/student-lookup")}
        onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
        onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
      >
        <MdSearch size={40} style={styles.icon} />
        <span style={styles.text}>Hae oppilaan suoritukset</span>
      </div>

      <div
        style={styles.card}
        onClick={() => navigate("/teacher/create-course-1")}   // ⭐ UUSI LINKITYS ⭐
        onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
        onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
      >
        <MdAdd size={40} style={styles.icon} />
        <span style={styles.text}>Luo uusi kurssi</span>
      </div>

      <div
        style={styles.card}
        onClick={() => navigate("/teacher/ready-courses")}
        onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
        onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
      >
        <MdContentCopy size={40} style={styles.icon} />
        <span style={styles.text}>Tarkastele valmiita kursseja</span>
      </div>

    </div>
  );
}

const styles = {
  container: {
    paddingTop: "2rem",
    paddingLeft: "1rem",
    paddingRight: "1rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "1.2rem",
  },

  title: {
    fontSize: "1.4rem",
    fontWeight: 600,
    marginBottom: "1rem",
    textAlign: "center",
  },

  card: {
    width: "100%",
    maxWidth: "350px",
    backgroundColor: "#F2F2F2",
    border: "1px solid #C8C8C8",
    borderRadius: "10px",
    padding: "1.2rem",
    display: "flex",
    alignItems: "center",
    gap: "1.2rem",
    cursor: "pointer",
    transition: "transform 0.18s ease, box-shadow 0.18s ease",
  },

  icon: {
    color: "black",
    minWidth: "40px",
  },

  text: {
    fontSize: "1rem",
    fontWeight: 500,
  },
};



