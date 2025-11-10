import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();

    if (username === "student" && password === "student") {
      localStorage.setItem("role", "student");
      onLogin("student");
      navigate("/student");
    } else if (username === "teacher" && password === "teacher") {
      localStorage.setItem("role", "teacher");
      onLogin("teacher");
      navigate("/teacher");
    } else {
      alert("Invalid credentials. Try again!");
    }
  };

  return (
    <div style={styles.container}>
      <h1>Login</h1>
      <form onSubmit={handleLogin} style={styles.form}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>
    </div>
  );
}

const styles = {
  container: { display: "flex", flexDirection: "column", alignItems: "center", marginTop: "5rem" },
  form: { display: "flex", flexDirection: "column", gap: "1rem", width: "250px" },
};



