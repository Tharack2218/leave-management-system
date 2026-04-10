import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleRegister = async () => {
    // ✅ validation
    if (!name || !email || !password) {
      alert("All fields are required");
      return;
    }

    setLoading(true);

    try {
      const res = await fetch("http://localhost:8089/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          email,
          password,
        }),
      });

      let data;
      try {
        data = await res.text();
      } catch {
        data = "Registered!";
      }

      if (!res.ok) {
        alert(data);
        return;
      }

      alert("Registered successfully 🎉");

      // ✅ redirect to login
      navigate("/");

    } catch (err) {
      console.error(err);
      alert("Error registering");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Create Account</h2>

        <input
          style={styles.input}
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <input
          style={styles.input}
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          style={styles.input}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button
          style={styles.button}
          onClick={handleRegister}
          disabled={loading}
        >
          {loading ? "Registering..." : "Register"}
        </button>

        <p style={{ marginTop: "15px" }}>
          Already have an account?{" "}
          <span style={styles.link} onClick={() => navigate("/")}>
            Login
          </span>
        </p>
      </div>
    </div>
  );
}

export default Register;

// 🎨 Stylish UI
const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #667eea, #764ba2)",
  },
  card: {
    background: "white",
    padding: "30px",
    borderRadius: "12px",
    width: "320px",
    textAlign: "center",
    boxShadow: "0 8px 20px rgba(0,0,0,0.2)",
  },
  title: {
    marginBottom: "20px",
    color: "#333",
  },
  input: {
    width: "100%",
    padding: "10px",
    margin: "10px 0",
    borderRadius: "6px",
    border: "1px solid #ccc",
    outline: "none",
  },
  button: {
    width: "100%",
    padding: "10px",
    marginTop: "10px",
    background: "#667eea",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "bold",
  },
  link: {
    color: "#667eea",
    cursor: "pointer",
    fontWeight: "bold",
  },
};